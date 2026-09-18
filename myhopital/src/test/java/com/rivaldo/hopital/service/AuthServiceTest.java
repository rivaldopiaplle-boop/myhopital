package com.rivaldo.hopital.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.rivaldo.hopital.config.DatabaseConfig;
import com.rivaldo.hopital.domain.Adresse;
import com.rivaldo.hopital.domain.MoyenContact;
import com.rivaldo.hopital.domain.Role;
import com.rivaldo.hopital.domain.Sexe;
import com.rivaldo.hopital.domain.Utilisateur;
import com.rivaldo.hopital.exception.ValidationException;
import com.rivaldo.hopital.repository.DataStore;
import com.rivaldo.hopital.repository.DatabaseManager;
import com.rivaldo.hopital.repository.DatabaseRepository;
import com.rivaldo.hopital.util.MotDePasse;
import java.nio.file.Path;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Le service d'authentification, contre une vraie base SQLite temporaire. */
class AuthServiceTest {

    @TempDir
    Path dossier;

    private DatabaseManager manager;
    private DataStore store;
    private DatabaseRepository repository;
    private AuthService auth;

    @BeforeEach
    void preparer() {
        manager = new DatabaseManager(new DatabaseConfig(
            "jdbc:sqlite:" + dossier.resolve("test.db"), null, null, "org.sqlite.JDBC"));
        manager.connect();
        manager.createTables();
        repository = new DatabaseRepository(manager);
        store = new DataStore();
        auth = new AuthService(store, new ValidationService(), repository);
    }

    @AfterEach
    void fermer() {
        manager.close(); // sinon Windows refuse de supprimer le fichier temporaire
    }

    private Utilisateur inscrire(String email, String motDePasse, Role role) {
        return auth.register("Nom", "Prenom", email, motDePasse, role,
            LocalDate.of(1990, 1, 1), "000000000", Sexe.AUTRE,
            new Adresse("Rue", "29200", "Brest", "France"), MoyenContact.EMAIL);
    }

    @Test
    void leMotDePasseEstStockeHache() {
        Utilisateur agent = inscrire("agent@hopital.local", "agent", Role.GESTIONNAIRE);
        assertTrue(MotDePasse.estHache(agent.getMotDePasse()));
        assertFalse(agent.getMotDePasse().contains("agent"));
    }

    @Test
    void laConnexionAccepteLeBonMotDePasseSeulement() {
        inscrire("agent@hopital.local", "agent", Role.GESTIONNAIRE);
        assertTrue(auth.login("agent@hopital.local", "agent").isPresent());
        assertTrue(auth.login("AGENT@hopital.local", " agent ").isPresent(), "email sans casse, espaces retires");
        assertFalse(auth.login("agent@hopital.local", "Agent").isPresent());
        assertFalse(auth.login("inconnu@hopital.local", "agent").isPresent());
    }

    @Test
    void unAncienMotDePasseEnClairEstHacheALaPremiereConnexion() {
        Utilisateur ancien = inscrire("medecin@hopital.local", "provisoire", Role.MEDECIN);
        ancien.setMotDePasse("medecin"); // comme dans une base creee avant le hachage
        repository.updateUtilisateur(ancien);

        assertTrue(auth.login("medecin@hopital.local", "medecin").isPresent());
        assertTrue(MotDePasse.estHache(ancien.getMotDePasse()));
        assertTrue(auth.login("medecin@hopital.local", "medecin").isPresent(), "toujours valide une fois hache");
    }

    @Test
    void changerSonMotDePasseExigeLActuel() {
        inscrire("agent@hopital.local", "agent", Role.GESTIONNAIRE);
        assertFalse(auth.changePassword("agent@hopital.local", "faux", "nouveau"));
        assertTrue(auth.changePassword("agent@hopital.local", "agent", "nouveau"));
        assertFalse(auth.login("agent@hopital.local", "agent").isPresent());
        assertTrue(auth.login("agent@hopital.local", "nouveau").isPresent());
    }

    @Test
    void unSecondDirecteurAttendLApprobation() {
        Utilisateur principal = inscrire("admin@hopital.local", "admin", Role.DIRECTEUR);
        auth.markAsPrimaryDirector(principal);
        inscrire("second@hopital.local", "second", Role.DIRECTEUR);

        assertFalse(auth.login("second@hopital.local", "second").isPresent());
        assertEquals(1, store.getDirectorApprovals().size());
        String code = store.getDirectorApprovals().get(0).getCode();
        assertTrue(auth.approveDirector(code));
        assertTrue(auth.login("second@hopital.local", "second").isPresent());
    }

    @Test
    void unEmailInvalideOuDejaPrisEstRefuse() {
        inscrire("agent@hopital.local", "agent", Role.GESTIONNAIRE);
        assertThrows(ValidationException.class, () -> inscrire("pas-un-email", "x", Role.GESTIONNAIRE));
        assertThrows(ValidationException.class, () -> inscrire("agent@hopital.local", "x", Role.GESTIONNAIRE));
    }
}
