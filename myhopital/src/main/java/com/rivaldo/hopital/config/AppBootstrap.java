package com.rivaldo.hopital.config;

import com.rivaldo.hopital.domain.Adresse;
import com.rivaldo.hopital.domain.MoyenContact;
import com.rivaldo.hopital.domain.Role;
import com.rivaldo.hopital.domain.Sexe;
import com.rivaldo.hopital.domain.Utilisateur;
import com.rivaldo.hopital.repository.DataStore;
import com.rivaldo.hopital.service.AuthService;
import com.rivaldo.hopital.service.ConsultationService;
import com.rivaldo.hopital.service.DocumentMedicalService;
import com.rivaldo.hopital.service.MedecinService;
import com.rivaldo.hopital.service.PatientService;
import com.rivaldo.hopital.service.RendezVousService;
import com.rivaldo.hopital.service.ValidationService;

import java.time.LocalDate;

/**
 * @Fichier AppBootstrap.java
 * @Objectif Charger des donnees de demo.
 * @Couche Config
 */
public class AppBootstrap {
    private final DataStore dataStore;
    private final ValidationService validationService;
    private final AuthService authService;
    private final MedecinService medecinService;
    private final PatientService patientService;
    private final RendezVousService rendezVousService;
    private final ConsultationService consultationService;
    private final DocumentMedicalService documentMedicalService;

    /**
     * @Objectif Construire le chargeur de donnees.
     * @param dataStore stockage en memoire
     * @param validationService outil de validation
     * @param authService service d'authentification
     * @param medecinService service medecins
     * @param patientService service patients
     */
    public AppBootstrap(DataStore dataStore, ValidationService validationService, AuthService authService,
                        MedecinService medecinService, PatientService patientService,
                        RendezVousService rendezVousService, ConsultationService consultationService,
                        DocumentMedicalService documentMedicalService) {
        this.dataStore = dataStore;
        this.validationService = validationService;
        this.authService = authService;
        this.medecinService = medecinService;
        this.patientService = patientService;
        this.rendezVousService = rendezVousService;
        this.consultationService = consultationService;
        this.documentMedicalService = documentMedicalService;
    }

    /**
     * @Objectif Charger les utilisateurs, medecins et patients de demo.
     */
    public void load() {
        // Etape 1: Creation du compte admin principal (directeur)
        // Ce compte sert de base pour administrer l'app
        Utilisateur admin = authService.register("Admin", "Root", "admin@hopital.local", "admin", Role.DIRECTEUR,
            LocalDate.of(1980, 1, 1), "000000000", Sexe.AUTRE,
            new Adresse("Rue Admin", "00000", "Ville", "Pays"), MoyenContact.EMAIL);
        // Etape 2: On marque cet admin comme directeur principal
        // Cela lui donne le droit d'approuver d'autres directeurs
        authService.markAsPrimaryDirector(admin);
        // Etape 3: Creation d'un compte gestionnaire
        authService.register("Agent", "Accueil", "agent@hopital.local", "agent", Role.GESTIONNAIRE,
            LocalDate.of(1990, 2, 2), "000000001", Sexe.AUTRE,
            new Adresse("Rue Agent", "00000", "Ville", "Pays"), MoyenContact.SMS);
        // Etape 4: Creation d'un compte medecin
        authService.register("Docteur", "Alpha", "medecin@hopital.local", "medecin", Role.MEDECIN,
            LocalDate.of(1985, 3, 3), "000000002", Sexe.AUTRE,
            new Adresse("Rue Medecin", "00000", "Ville", "Pays"), MoyenContact.APPEL);

        // Etape 5: Chargement des donnees de demo via CSV
        // Ces donnees remplissent les tableaux pour tester
        DataCsvLoader loader = new DataCsvLoader(dataStore, patientService, medecinService,
            rendezVousService, consultationService, documentMedicalService);
        loader.loadAll();
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Aucun compte n'existait au demarrage.
Cause: Pas de chargement initial.
Consequence: Tests UI impossibles sans saisie manuelle.
Solution: Ajouter un bootstrap de donnees.
Pourquoi: Accelere les scenarios de demo.
Comment: Creer quelques utilisateurs et entites.
*/
