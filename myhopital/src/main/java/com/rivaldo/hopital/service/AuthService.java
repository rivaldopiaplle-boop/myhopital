package com.rivaldo.hopital.service;

import com.rivaldo.hopital.domain.AdminNotification;
import com.rivaldo.hopital.domain.DirectorApproval;
import com.rivaldo.hopital.domain.PasswordResetToken;
import com.rivaldo.hopital.domain.Role;
import com.rivaldo.hopital.domain.Utilisateur;
import com.rivaldo.hopital.exception.ValidationException;
import com.rivaldo.hopital.repository.DataStore;
import com.rivaldo.hopital.repository.DatabaseRepository;
import com.rivaldo.hopital.util.IdGenerator;
import com.rivaldo.hopital.util.RandomCodeUtils;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @Fichier AuthService.java
 * @Objectif Gérer l'authentification et l'inscription des utilisateurs.
 * @Couche Service
 */
public class AuthService {
    private final DataStore dataStore;
    private final ValidationService validationService;
    private final DatabaseRepository databaseRepository;

    /**
     * @Objectif Construire le service d'authentification.
     * @param dataStore stockage en memoire
     * @param validationService outil de validation
     * @param databaseRepository acces base de donnees
     */
    public AuthService(DataStore dataStore, ValidationService validationService,
                       DatabaseRepository databaseRepository) {
        this.dataStore = dataStore;
        this.validationService = validationService;
        this.databaseRepository = databaseRepository;
    }

    /**
     * @Objectif Inscrire un nouvel utilisateur si l'email est unique.
     * @param nom nom
     * @param prenom prenom
     * @param email email de connexion
     * @param motDePasse mot de passe
     * @param role role
     * @param dateNaissance date de naissance
     * @param telephone telephone
     * @param sexe sexe
     * @param adresse adresse
     * @param moyenContact moyen de contact prefere
     * @return utilisateur cree
     */
    public Utilisateur register(String nom, String prenom, String email, String motDePasse, Role role,
                                java.time.LocalDate dateNaissance, String telephone,
                                com.rivaldo.hopital.domain.Sexe sexe,
                                com.rivaldo.hopital.domain.Adresse adresse,
                                com.rivaldo.hopital.domain.MoyenContact moyenContact) {
        // Étape 1: Valider le format de l'email
        // On vérifie que l'email respecte le format standard (exemple@domaine.com)
        if (!validationService.isEmailValid(email)) {
            // Si l'email n'est pas valide, on lance une erreur
            throw new ValidationException("Email invalide.");
        }
        
        // Étape 2: Vérifier que l'email n'est pas déjà utilisé
        // On cherche dans la base de données si cet email existe déjà
        if (findByEmail(email).isPresent()) {
            // Si l'email existe déjà, on lance une erreur
            throw new ValidationException("Utilisateur deja inscrit.");
        }
        
        // Étape 3: Créer le nouvel utilisateur
        // On crée un objet Utilisateur avec toutes les informations fournies
        // IdGenerator.newId() génère un identifiant unique (comme un numéro de série)
        Utilisateur user = new Utilisateur(IdGenerator.newId(), nom, prenom, email, motDePasse, role,
                dateNaissance, telephone, sexe, adresse, moyenContact);
        
        // Étape 4: Gestion spéciale pour les directeurs
        // Si c'est un DIRECTEUR et qu'il y a déjà un directeur principal
        if (role == Role.DIRECTEUR && hasPrimaryDirector()) {
            // Le nouveau directeur doit être approuvé par le directeur principal
            user.setApproved(false);
            
            // Générer un code d'approbation à 6 chiffres (exemple: 123456)
            String code = RandomCodeUtils.numericCode(6);
            
            // Enregistrer la demande d'approbation avec le code
            DirectorApproval approval = new DirectorApproval(user.getId(), code, LocalDateTime.now());
            dataStore.getDirectorApprovals().add(approval);
            databaseRepository.insertDirectorApproval(approval);
            
            // Créer une notification pour le directeur principal
            AdminNotification notification = new AdminNotification(IdGenerator.newId(),
                "Nouveau directeur a approuver: " + user.getEmail() + " | Code: " + code,
                LocalDateTime.now());
            dataStore.getNotifications().add(notification);
            databaseRepository.insertAdminNotification(notification);
        }
        
        // Étape 5: Ajouter l'utilisateur dans la liste de tous les utilisateurs
        dataStore.getUtilisateurs().add(user);
        databaseRepository.insertUtilisateur(user);
        
        // Étape 6: Retourner l'utilisateur créé
        return user;
    }

    /**
     * @Objectif Valider une connexion via email et mot de passe.
     * @param email email de connexion
     * @param motDePasse mot de passe
     * @return utilisateur trouve
     */
    public Optional<Utilisateur> login(String email, String motDePasse) {
        // ⚠️ CORRECTION: On nettoie les espaces avant/après
        // Pourquoi ? Si l'utilisateur tape "admin " au lieu de "admin", ça ne marcherait pas
        String cleanEmail = email != null ? email.trim() : "";
        String cleanPassword = motDePasse != null ? motDePasse.trim() : "";
        
        // On cherche dans TOUS les utilisateurs enregistrés
        return dataStore.getUtilisateurs().stream()
                // Filtre 1: L'email doit correspondre (sans tenir compte des majuscules/minuscules)
                // Exemple: "Admin@Hopital.com" = "admin@hopital.com"
                .filter(user -> user.getEmail().equalsIgnoreCase(cleanEmail))
                
                // Filtre 2: Le mot de passe doit être EXACTEMENT identique
                // ⚠️ ATTENTION: "admin" ≠ "Admin" (les majuscules comptent ici)
                // ✅ CORRECTION: On compare avec le mot de passe nettoyé (sans espaces)
                .filter(user -> user.getMotDePasse().trim().equals(cleanPassword))
                
                // Filtre 3: L'utilisateur doit être approuvé
                // OU ne pas être un directeur (les autres rôles n'ont pas besoin d'approbation)
                .filter(user -> user.isApproved() || user.getRole() != Role.DIRECTEUR)
                
                // Résultat: On retourne le PREMIER utilisateur qui correspond à tous les filtres
                // Si aucun utilisateur ne correspond, on retourne Optional.empty()
                .findFirst();
    }

    /**
     * @Objectif Rechercher un utilisateur par email.
     * @param email email
     * @return utilisateur trouve
     */
    public Optional<Utilisateur> findByEmail(String email) {
        // On parcourt tous les utilisateurs
        return dataStore.getUtilisateurs().stream()
                // On compare les emails (sans tenir compte des majuscules)
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                // On retourne le premier utilisateur trouvé (ou rien si aucun)
                .findFirst();
    }

    /**
     * @Objectif Declarer un directeur principal.
     * @param user directeur principal
     */
    public void markAsPrimaryDirector(Utilisateur user) {
        // Marquer cet utilisateur comme directeur principal
        // Le directeur principal a tous les droits et n'a pas besoin d'approbation
        user.setPrimaryDirector(true);
        
        // L'approuver automatiquement
        user.setApproved(true);
        // Persister la mise a jour
        databaseRepository.updateUtilisateur(user);
    }

    /**
     * @Objectif Approuver un directeur via code.
     * @param code code d'approbation
     * @return true si approuve
     */
    public boolean approveDirector(String code) {
        // Étape 1: Chercher la demande d'approbation correspondant au code
        Optional<DirectorApproval> approval = dataStore.getDirectorApprovals().stream()
                // On compare le code fourni avec les codes enregistrés
                .filter(entry -> entry.getCode().equals(code))
                // On récupère la première correspondance
                .findFirst();
        
        // Étape 2: Si aucune demande ne correspond, on retourne false
        if (approval.isEmpty()) {
            return false;
        }
        
        // Étape 3: Récupérer l'ID de l'utilisateur à approuver
        String userId = approval.get().getUserId();
        
        // Étape 4: Chercher l'utilisateur et l'approuver
        dataStore.getUtilisateurs().stream()
                // On cherche l'utilisateur par son ID
                .filter(user -> user.getId().equals(userId))
                // On récupère le premier (normalement il n'y en a qu'un seul)
                .findFirst()
                // Si on le trouve, on l'approuve
                .ifPresent(user -> {
                    user.setApproved(true);
                    databaseRepository.updateUtilisateur(user);
                });
        
        // Étape 5: Supprimer la demande d'approbation (elle n'est plus nécessaire)
        dataStore.getDirectorApprovals().remove(approval.get());
        databaseRepository.deleteDirectorApproval(userId);
        
        // Étape 6: Retourner true pour indiquer que l'approbation a réussi
        return true;
    }

    /**
     * @Objectif Demander un code de reinitialisation mot de passe.
     * @param email email
     * @return code genere
     */
    public String requestPasswordReset(String email) {
        // Étape 1: Vérifier que l'email existe dans la base de données
        Optional<Utilisateur> user = findByEmail(email);
        
        // Si l'email n'existe pas, on lance une erreur
        if (user.isEmpty()) {
            throw new ValidationException("Email inconnu.");
        }
        
        // Étape 2: Générer un code de réinitialisation à 6 chiffres
        // Exemple: 123456, 987654, etc.
        String code = RandomCodeUtils.numericCode(6);
        
        // Étape 3: Enregistrer le code avec l'email et l'heure actuelle
        // Comme ça, on peut vérifier plus tard que le code correspond bien à cet email
        PasswordResetToken token = new PasswordResetToken(email, code, LocalDateTime.now());
        dataStore.getResetTokens().add(token);
        databaseRepository.insertResetToken(token);
        
        // Étape 4: Retourner le code (pour l'afficher à l'utilisateur ou l'envoyer par email)
        return code;
    }

    /**
     * @Objectif Valider un code de reinitialisation et changer le mot de passe.
     * @param email email
     * @param code code
     * @param newPassword nouveau mot de passe
     * @return true si ok
     */
    public boolean resetPassword(String email, String code, String newPassword) {
        // Étape 1: Chercher le code de réinitialisation correspondant à l'email et au code
        Optional<PasswordResetToken> token = dataStore.getResetTokens().stream()
                // On vérifie que l'email correspond (sans tenir compte des majuscules)
                .filter(entry -> entry.getEmail().equalsIgnoreCase(email) && entry.getCode().equals(code))
                // On récupère le premier (normalement il n'y en a qu'un seul)
                .findFirst();
        
        // Étape 2: Si le code n'existe pas ou ne correspond pas, on retourne false
        if (token.isEmpty()) {
            return false;
        }
        
        // Étape 3: Changer le mot de passe de l'utilisateur
        findByEmail(email).ifPresent(user -> {
            user.setMotDePasse(newPassword);
            databaseRepository.updateUtilisateur(user);
        });
        
        // Étape 4: Supprimer le code (il ne peut être utilisé qu'une seule fois)
        dataStore.getResetTokens().remove(token.get());
        databaseRepository.deleteResetToken(email, code);
        
        // Étape 5: Retourner true pour indiquer que la réinitialisation a réussi
        return true;
    }

    /**
     * @Objectif Changer le mot de passe d'un utilisateur.
     * @param email email
     * @param current mot de passe actuel
     * @param next nouveau mot de passe
     * @return true si ok
     */
    public boolean changePassword(String email, String current, String next) {
        // Étape 1: Chercher l'utilisateur par email
        Optional<Utilisateur> user = findByEmail(email);
        
        // ⚠️ CORRECTION: On nettoie les espaces pour éviter les erreurs
        String cleanCurrent = current != null ? current.trim() : "";
        String cleanNext = next != null ? next.trim() : "";
        
        // Étape 2: Vérifier que l'utilisateur existe ET que le mot de passe actuel est correct
        // ⚠️ IMPORTANT: On utilise equals() qui compare EXACTEMENT
        // "admin" ≠ "Admin" (les majuscules comptent)
        // ✅ CORRECTION: On compare avec les mots de passe nettoyés
        if (user.isEmpty() || !user.get().getMotDePasse().trim().equals(cleanCurrent)) {
            // Si l'utilisateur n'existe pas OU si le mot de passe actuel est incorrect
            return false;
        }
        
        // Étape 3: Changer le mot de passe (on stocke le mot de passe nettoyé)
        user.get().setMotDePasse(cleanNext);
        databaseRepository.updateUtilisateur(user.get());
        
        // Étape 4: Retourner true pour indiquer que le changement a réussi
        return true;
    }

    /**
     * @Objectif Verifier si un directeur principal existe deja.
     * @return true si un directeur principal existe
     */
    private boolean hasPrimaryDirector() {
        // On parcourt tous les utilisateurs
        return dataStore.getUtilisateurs().stream()
                // On cherche un utilisateur qui est:
                // 1. DIRECTEUR
                // 2. ET directeur principal
                .anyMatch(user -> user.getRole() == Role.DIRECTEUR && user.isPrimaryDirector());
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Les erreurs d'inscription n'etaient pas qualifiees.
Cause: Utilisation d'exceptions generiques.
Consequence: L'UI ne savait pas distinguer les erreurs metier.
Solution: Lever ValidationException pour les cas metier.
Pourquoi: Offrir un retour utilisateur clair.
Comment: Validation dans le service avant creation.
*/
