package com.rivaldo.hopital.repository;

import com.rivaldo.hopital.domain.AdminNotification;
import com.rivaldo.hopital.domain.Adresse;
import com.rivaldo.hopital.domain.Consultation;
import com.rivaldo.hopital.domain.DirectorApproval;
import com.rivaldo.hopital.domain.DocumentMedical;
import com.rivaldo.hopital.domain.DossierMedical;
import com.rivaldo.hopital.domain.DocumentType;
import com.rivaldo.hopital.domain.EtatDossier;
import com.rivaldo.hopital.domain.EtatPatient;
import com.rivaldo.hopital.domain.Medecin;
import com.rivaldo.hopital.domain.MoyenContact;
import com.rivaldo.hopital.domain.PasswordResetToken;
import com.rivaldo.hopital.domain.Patient;
import com.rivaldo.hopital.domain.RendezVous;
import com.rivaldo.hopital.domain.Role;
import com.rivaldo.hopital.domain.Sexe;
import com.rivaldo.hopital.domain.Specialite;
import com.rivaldo.hopital.domain.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @Fichier DatabaseRepository.java
 * @Objectif Lire et ecrire les donnees dans la base.
 * @Couche Repository
 */
public class DatabaseRepository {
    private final DatabaseManager databaseManager;

    /**
     * @Objectif Construire le repository SQL.
     * @param databaseManager gestionnaire JDBC
     */
    public DatabaseRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * @Objectif Verifier si des utilisateurs existent deja.
     * @return true si au moins un utilisateur
     */
    public boolean hasAnyUsers() {
        String sql = "SELECT COUNT(*) FROM utilisateurs";
        try (Statement statement = connection().createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException ex) {
            throw new IllegalStateException("Lecture utilisateurs impossible.", ex);
        }
    }

    /**
     * @Objectif Charger toutes les donnees en memoire.
     * @param dataStore stockage en memoire
     */
    public void loadAll(DataStore dataStore) {
        // Etape 1 : vider les listes pour eviter les doublons
        dataStore.getUtilisateurs().clear();
        dataStore.getPatients().clear();
        dataStore.getMedecins().clear();
        dataStore.getDossiers().clear();
        dataStore.getRendezVous().clear();
        dataStore.getConsultations().clear();
        dataStore.getDocuments().clear();
        dataStore.getDirectorApprovals().clear();
        dataStore.getNotifications().clear();
        dataStore.getResetTokens().clear();

        // Etape 2 : charger les utilisateurs
        loadUtilisateurs(dataStore);
        // Etape 3 : charger les medecins
        loadMedecins(dataStore);
        // Etape 4 : charger les patients
        loadPatients(dataStore);
        // Etape 5 : charger les dossiers et relier aux patients
        loadDossiers(dataStore);
        // Etape 6 : charger les rendez-vous
        loadRendezVous(dataStore);
        // Etape 7 : charger les consultations
        loadConsultations(dataStore);
        // Etape 8 : charger les documents
        loadDocuments(dataStore);
        // Etape 9 : charger approvals/notifications/tokens
        loadDirectorApprovals(dataStore);
        loadNotifications(dataStore);
        loadResetTokens(dataStore);
    }

    /**
     * @Objectif Inserer un utilisateur en base.
     * @param user utilisateur a inserer
     */
    public void insertUtilisateur(Utilisateur user) {
        // Etape 1 : definir la requete d'insertion
        String sql = "INSERT INTO utilisateurs (id, nom, prenom, email, mot_de_passe, role, date_naissance, "
            + "telephone, sexe, adresse_rue, adresse_code_postal, adresse_ville, adresse_pays, moyen_contact, "
            + "primary_director, approved) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            // Etape 2 : binder les champs utilisateur
            bindUtilisateur(ps, user);
            // Etape 3 : executer l'insertion
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Insertion utilisateur impossible.", ex);
        }
    }

    /**
     * @Objectif Mettre a jour un utilisateur en base.
     * @param user utilisateur a mettre a jour
     */
    public void updateUtilisateur(Utilisateur user) {
        // Etape 1 : definir la requete de mise a jour
        String sql = "UPDATE utilisateurs SET nom = ?, prenom = ?, email = ?, mot_de_passe = ?, role = ?, "
            + "date_naissance = ?, telephone = ?, sexe = ?, adresse_rue = ?, adresse_code_postal = ?, "
            + "adresse_ville = ?, adresse_pays = ?, moyen_contact = ?, primary_director = ?, approved = ? "
            + "WHERE id = ?";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            // Etape 2 : binder les champs utilisateur
            ps.setString(1, user.getNom());
            ps.setString(2, user.getPrenom());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getMotDePasse());
            ps.setString(5, user.getRole() != null ? user.getRole().name() : null);
            ps.setString(6, user.getDateNaissance() != null ? user.getDateNaissance().toString() : null);
            ps.setString(7, user.getTelephone());
            ps.setString(8, user.getSexe() != null ? user.getSexe().name() : null);
            bindAdresse(ps, user.getAdresse(), 9);
            ps.setString(13, user.getMoyenContact() != null ? user.getMoyenContact().name() : null);
            ps.setInt(14, user.isPrimaryDirector() ? 1 : 0);
            ps.setInt(15, user.isApproved() ? 1 : 0);
            ps.setString(16, user.getId());
            // Etape 3 : executer la mise a jour
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Mise a jour utilisateur impossible.", ex);
        }
    }

    /**
     * @Objectif Inserer une demande d'approbation directeur.
     * @param approval demande d'approbation
     */
    public void insertDirectorApproval(DirectorApproval approval) {
        // Etape 1 : definir la requete d'insertion
        String sql = "INSERT INTO director_approvals (user_id, code, created_at) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            // Etape 2 : binder les champs
            ps.setString(1, approval.getUserId());
            ps.setString(2, approval.getCode());
            ps.setString(3, approval.getCreatedAt().toString());
            // Etape 3 : executer l'insertion
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Insertion approval impossible.", ex);
        }
    }

    /**
     * @Objectif Supprimer une demande d'approbation directeur.
     * @param userId identifiant utilisateur
     */
    public void deleteDirectorApproval(String userId) {
        // Etape 1 : definir la requete de suppression
        String sql = "DELETE FROM director_approvals WHERE user_id = ?";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            // Etape 2 : binder l'identifiant
            ps.setString(1, userId);
            // Etape 3 : executer la suppression
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Suppression approval impossible.", ex);
        }
    }

    /**
     * @Objectif Inserer une notification admin.
     * @param notification notification a inserer
     */
    public void insertAdminNotification(AdminNotification notification) {
        // Etape 1 : definir la requete d'insertion
        String sql = "INSERT INTO admin_notifications (id, message, created_at) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            // Etape 2 : binder les champs
            ps.setString(1, notification.getId());
            ps.setString(2, notification.getMessage());
            ps.setString(3, notification.getCreatedAt().toString());
            // Etape 3 : executer l'insertion
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Insertion notification impossible.", ex);
        }
    }

    /**
     * @Objectif Inserer un token de reinitialisation.
     * @param token token a inserer
     */
    public void insertResetToken(PasswordResetToken token) {
        // Etape 1 : definir la requete d'insertion
        String sql = "INSERT INTO reset_tokens (email, code, created_at) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            // Etape 2 : binder les champs
            ps.setString(1, token.getEmail());
            ps.setString(2, token.getCode());
            ps.setString(3, token.getCreatedAt().toString());
            // Etape 3 : executer l'insertion
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Insertion token impossible.", ex);
        }
    }

    /**
     * @Objectif Supprimer un token de reinitialisation.
     * @param email email lie au token
     * @param code code du token
     */
    public void deleteResetToken(String email, String code) {
        // Etape 1 : definir la requete de suppression
        String sql = "DELETE FROM reset_tokens WHERE email = ? AND code = ?";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            // Etape 2 : binder les parametres
            ps.setString(1, email);
            ps.setString(2, code);
            // Etape 3 : executer la suppression
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Suppression token impossible.", ex);
        }
    }

    /**
     * @Objectif Inserer un patient en base.
     * @param patient patient a inserer
     */
    public void insertPatient(Patient patient) {
        // Etape 1 : definir la requete d'insertion
        String sql = "INSERT INTO patients (id, dossier_number, nom, prenom, date_naissance, sexe, telephone, "
            + "email, adresse_rue, adresse_code_postal, adresse_ville, adresse_pays, moyen_contact, etat) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            // Etape 2 : binder les champs patient
            ps.setString(1, patient.getId());
            ps.setString(2, patient.getDossierNumber());
            ps.setString(3, patient.getNom());
            ps.setString(4, patient.getPrenom());
            ps.setString(5, patient.getDateNaissance() != null ? patient.getDateNaissance().toString() : null);
            ps.setString(6, patient.getSexe() != null ? patient.getSexe().name() : null);
            ps.setString(7, patient.getTelephone());
            ps.setString(8, patient.getEmail());
            bindAdresse(ps, patient.getAdresse(), 9);
            ps.setString(13, patient.getMoyenContact() != null ? patient.getMoyenContact().name() : null);
            ps.setString(14, patient.getEtat() != null ? patient.getEtat().name() : null);
            // Etape 3 : executer l'insertion
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Insertion patient impossible.", ex);
        }
    }

    /**
     * @Objectif Mettre a jour un patient en base.
     * @param patient patient a mettre a jour
     */
    public void updatePatient(Patient patient) {
        // Etape 1 : definir la requete de mise a jour
        String sql = "UPDATE patients SET dossier_number = ?, nom = ?, prenom = ?, date_naissance = ?, sexe = ?, "
            + "telephone = ?, email = ?, adresse_rue = ?, adresse_code_postal = ?, adresse_ville = ?, "
            + "adresse_pays = ?, moyen_contact = ?, etat = ? WHERE id = ?";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            // Etape 2 : binder les champs patient
            ps.setString(1, patient.getDossierNumber());
            ps.setString(2, patient.getNom());
            ps.setString(3, patient.getPrenom());
            ps.setString(4, patient.getDateNaissance() != null ? patient.getDateNaissance().toString() : null);
            ps.setString(5, patient.getSexe() != null ? patient.getSexe().name() : null);
            ps.setString(6, patient.getTelephone());
            ps.setString(7, patient.getEmail());
            bindAdresse(ps, patient.getAdresse(), 8);
            ps.setString(12, patient.getMoyenContact() != null ? patient.getMoyenContact().name() : null);
            ps.setString(13, patient.getEtat() != null ? patient.getEtat().name() : null);
            ps.setString(14, patient.getId());
            // Etape 3 : executer la mise a jour
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Mise a jour patient impossible.", ex);
        }
    }

    /**
     * @Objectif Supprimer un patient et ses dependances.
     * @param patientId identifiant patient
     */
    public void deletePatient(String patientId) {
        try {
            // Etape 1 : demarrer la transaction
            connection().setAutoCommit(false);
            // Etape 2 : supprimer les dependances
            deleteById("consultations", "patient_id", patientId);
            deleteById("documents", "patient_id", patientId);
            deleteById("rendezvous", "patient_id", patientId);
            deleteById("dossiers", "patient_id", patientId);
            // Etape 3 : supprimer le patient
            deleteById("patients", "id", patientId);
            // Etape 4 : valider la transaction
            connection().commit();
        } catch (SQLException ex) {
            rollbackQuietly();
            throw new IllegalStateException("Suppression patient impossible.", ex);
        } finally {
            resetAutoCommit();
        }
    }

    /**
     * @Objectif Inserer un medecin en base.
     * @param medecin medecin a inserer
     */
    public void insertMedecin(Medecin medecin) {
        // Etape 1 : definir la requete d'insertion
        String sql = "INSERT INTO medecins (id, matricule, nom, prenom, date_naissance, sexe, telephone, email, "
            + "adresse_rue, adresse_code_postal, adresse_ville, adresse_pays, moyen_contact, specialite) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            // Etape 2 : binder les champs medecin
            ps.setString(1, medecin.getId());
            ps.setString(2, medecin.getMatricule());
            ps.setString(3, medecin.getNom());
            ps.setString(4, medecin.getPrenom());
            ps.setString(5, medecin.getDateNaissance() != null ? medecin.getDateNaissance().toString() : null);
            ps.setString(6, medecin.getSexe() != null ? medecin.getSexe().name() : null);
            ps.setString(7, medecin.getTelephone());
            ps.setString(8, medecin.getEmail());
            bindAdresse(ps, medecin.getAdresse(), 9);
            ps.setString(13, medecin.getMoyenContact() != null ? medecin.getMoyenContact().name() : null);
            ps.setString(14, medecin.getSpecialite() != null ? medecin.getSpecialite().name() : null);
            // Etape 3 : executer l'insertion
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Insertion medecin impossible.", ex);
        }
    }

    /**
     * @Objectif Mettre a jour un medecin en base.
     * @param medecin medecin a mettre a jour
     */
    public void updateMedecin(Medecin medecin) {
        // Etape 1 : definir la requete de mise a jour
        String sql = "UPDATE medecins SET matricule = ?, nom = ?, prenom = ?, date_naissance = ?, sexe = ?, "
            + "telephone = ?, email = ?, adresse_rue = ?, adresse_code_postal = ?, adresse_ville = ?, "
            + "adresse_pays = ?, moyen_contact = ?, specialite = ? WHERE id = ?";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            // Etape 2 : binder les champs medecin
            ps.setString(1, medecin.getMatricule());
            ps.setString(2, medecin.getNom());
            ps.setString(3, medecin.getPrenom());
            ps.setString(4, medecin.getDateNaissance() != null ? medecin.getDateNaissance().toString() : null);
            ps.setString(5, medecin.getSexe() != null ? medecin.getSexe().name() : null);
            ps.setString(6, medecin.getTelephone());
            ps.setString(7, medecin.getEmail());
            bindAdresse(ps, medecin.getAdresse(), 8);
            ps.setString(12, medecin.getMoyenContact() != null ? medecin.getMoyenContact().name() : null);
            ps.setString(13, medecin.getSpecialite() != null ? medecin.getSpecialite().name() : null);
            ps.setString(14, medecin.getId());
            // Etape 3 : executer la mise a jour
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Mise a jour medecin impossible.", ex);
        }
    }

    /**
     * @Objectif Supprimer un medecin et ses dependances.
     * @param medecinId identifiant medecin
     */
    public void deleteMedecin(String medecinId) {
        try {
            // Etape 1 : demarrer la transaction
            connection().setAutoCommit(false);
            // Etape 2 : supprimer les dependances
            deleteById("consultations", "medecin_id", medecinId);
            deleteById("documents", "medecin_id", medecinId);
            deleteById("rendezvous", "medecin_id", medecinId);
            // Etape 3 : supprimer le medecin
            deleteById("medecins", "id", medecinId);
            // Etape 4 : valider la transaction
            connection().commit();
        } catch (SQLException ex) {
            rollbackQuietly();
            throw new IllegalStateException("Suppression medecin impossible.", ex);
        } finally {
            resetAutoCommit();
        }
    }

    /**
     * @Objectif Inserer un dossier medical en base.
     * @param dossier dossier a inserer
     */
    public void insertDossier(DossierMedical dossier) {
        // Etape 1 : definir la requete d'insertion
        String sql = "INSERT INTO dossiers (id, patient_id, date_ouverture, etat) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            // Etape 2 : binder les champs dossier
            ps.setString(1, dossier.getId());
            ps.setString(2, dossier.getPatientId());
            ps.setString(3, dossier.getDateOuverture() != null ? dossier.getDateOuverture().toString() : null);
            ps.setString(4, dossier.getEtat() != null ? dossier.getEtat().name() : null);
            // Etape 3 : executer l'insertion
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Insertion dossier impossible.", ex);
        }
    }

    /**
     * @Objectif Mettre a jour un dossier medical en base.
     * @param dossier dossier a mettre a jour
     */
    public void updateDossier(DossierMedical dossier) {
        // Etape 1 : definir la requete de mise a jour
        String sql = "UPDATE dossiers SET patient_id = ?, date_ouverture = ?, etat = ? WHERE id = ?";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            // Etape 2 : binder les champs dossier
            ps.setString(1, dossier.getPatientId());
            ps.setString(2, dossier.getDateOuverture() != null ? dossier.getDateOuverture().toString() : null);
            ps.setString(3, dossier.getEtat() != null ? dossier.getEtat().name() : null);
            ps.setString(4, dossier.getId());
            // Etape 3 : executer la mise a jour
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Mise a jour dossier impossible.", ex);
        }
    }

    /**
     * @Objectif Supprimer un dossier medical et ses dependances.
     * @param dossierId identifiant dossier
     */
    public void deleteDossier(String dossierId) {
        try {
            // Etape 1 : demarrer la transaction
            connection().setAutoCommit(false);
            // Etape 2 : supprimer les dependances
            deleteById("consultations", "dossier_id", dossierId);
            deleteById("documents", "dossier_id", dossierId);
            // Etape 3 : supprimer le dossier
            deleteById("dossiers", "id", dossierId);
            // Etape 4 : valider la transaction
            connection().commit();
        } catch (SQLException ex) {
            rollbackQuietly();
            throw new IllegalStateException("Suppression dossier impossible.", ex);
        } finally {
            resetAutoCommit();
        }
    }

    /**
     * @Objectif Inserer un rendez-vous en base.
     * @param rdv rendez-vous a inserer
     */
    public void insertRendezVous(RendezVous rdv) {
        // Etape 1 : definir la requete d'insertion
        String sql = "INSERT INTO rendezvous (id, patient_id, medecin_id, date_heure) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            // Etape 2 : binder les champs rendez-vous
            ps.setString(1, rdv.getId());
            ps.setString(2, rdv.getPatientId());
            ps.setString(3, rdv.getMedecinId());
            ps.setString(4, rdv.getDateHeure().toString());
            // Etape 3 : executer l'insertion
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Insertion rendez-vous impossible.", ex);
        }
    }

    /**
     * @Objectif Mettre a jour un rendez-vous en base.
     * @param rdv rendez-vous a mettre a jour
     */
    public void updateRendezVous(RendezVous rdv) {
        // Etape 1 : definir la requete de mise a jour
        String sql = "UPDATE rendezvous SET patient_id = ?, medecin_id = ?, date_heure = ? WHERE id = ?";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            // Etape 2 : binder les champs rendez-vous
            ps.setString(1, rdv.getPatientId());
            ps.setString(2, rdv.getMedecinId());
            ps.setString(3, rdv.getDateHeure().toString());
            ps.setString(4, rdv.getId());
            // Etape 3 : executer la mise a jour
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Mise a jour rendez-vous impossible.", ex);
        }
    }

    /**
     * @Objectif Supprimer un rendez-vous.
     * @param rdvId identifiant rendez-vous
     */
    public void deleteRendezVous(String rdvId) {
        try {
            // Etape 1 : supprimer par identifiant
            deleteById("rendezvous", "id", rdvId);
        } catch (SQLException ex) {
            throw new IllegalStateException("Suppression rendez-vous impossible.", ex);
        }
    }

    /**
     * @Objectif Inserer une consultation en base.
     * @param consultation consultation a inserer
     */
    public void insertConsultation(Consultation consultation) {
        // Etape 1 : definir la requete d'insertion
        String sql = "INSERT INTO consultations (id, patient_id, medecin_id, dossier_id, date, diagnostic, "
            + "prescription) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            // Etape 2 : binder les champs consultation
            ps.setString(1, consultation.getId());
            ps.setString(2, consultation.getPatientId());
            ps.setString(3, consultation.getMedecinId());
            ps.setString(4, consultation.getDossierId());
            ps.setString(5, consultation.getDate() != null ? consultation.getDate().toString() : null);
            ps.setString(6, consultation.getDiagnostic());
            ps.setString(7, consultation.getPrescription());
            // Etape 3 : executer l'insertion
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Insertion consultation impossible.", ex);
        }
    }

    /**
     * @Objectif Mettre a jour une consultation en base.
     * @param consultation consultation a mettre a jour
     */
    public void updateConsultation(Consultation consultation) {
        // Etape 1 : definir la requete de mise a jour
        String sql = "UPDATE consultations SET patient_id = ?, medecin_id = ?, dossier_id = ?, date = ?, "
            + "diagnostic = ?, prescription = ? WHERE id = ?";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            // Etape 2 : binder les champs consultation
            ps.setString(1, consultation.getPatientId());
            ps.setString(2, consultation.getMedecinId());
            ps.setString(3, consultation.getDossierId());
            ps.setString(4, consultation.getDate() != null ? consultation.getDate().toString() : null);
            ps.setString(5, consultation.getDiagnostic());
            ps.setString(6, consultation.getPrescription());
            ps.setString(7, consultation.getId());
            // Etape 3 : executer la mise a jour
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Mise a jour consultation impossible.", ex);
        }
    }

    /**
     * @Objectif Supprimer une consultation.
     * @param consultationId identifiant consultation
     */
    public void deleteConsultation(String consultationId) {
        try {
            // Etape 1 : supprimer par identifiant
            deleteById("consultations", "id", consultationId);
        } catch (SQLException ex) {
            throw new IllegalStateException("Suppression consultation impossible.", ex);
        }
    }

    /**
     * @Objectif Inserer un document medical en base.
     * @param document document a inserer
     */
    public void insertDocument(DocumentMedical document) {
        // Etape 1 : definir la requete d'insertion
        String sql = "INSERT INTO documents (id, patient_id, medecin_id, dossier_id, nom, type, date_ajout) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            // Etape 2 : binder les champs document
            ps.setString(1, document.getId());
            ps.setString(2, document.getPatientId());
            ps.setString(3, document.getMedecinId());
            ps.setString(4, document.getDossierId());
            ps.setString(5, document.getNom());
            ps.setString(6, document.getType() != null ? document.getType().name() : null);
            ps.setString(7, document.getDateAjout() != null ? document.getDateAjout().toString() : null);
            // Etape 3 : executer l'insertion
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Insertion document impossible.", ex);
        }
    }

    /**
     * @Objectif Mettre a jour un document medical en base.
     * @param document document a mettre a jour
     */
    public void updateDocument(DocumentMedical document) {
        // Etape 1 : definir la requete de mise a jour
        String sql = "UPDATE documents SET patient_id = ?, medecin_id = ?, dossier_id = ?, nom = ?, type = ?, "
            + "date_ajout = ? WHERE id = ?";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            // Etape 2 : binder les champs document
            ps.setString(1, document.getPatientId());
            ps.setString(2, document.getMedecinId());
            ps.setString(3, document.getDossierId());
            ps.setString(4, document.getNom());
            ps.setString(5, document.getType() != null ? document.getType().name() : null);
            ps.setString(6, document.getDateAjout() != null ? document.getDateAjout().toString() : null);
            ps.setString(7, document.getId());
            // Etape 3 : executer la mise a jour
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Mise a jour document impossible.", ex);
        }
    }

    /**
     * @Objectif Supprimer un document medical.
     * @param documentId identifiant document
     */
    public void deleteDocument(String documentId) {
        try {
            // Etape 1 : supprimer par identifiant
            deleteById("documents", "id", documentId);
        } catch (SQLException ex) {
            throw new IllegalStateException("Suppression document impossible.", ex);
        }
    }

    /**
     * @Objectif Charger les utilisateurs depuis la base.
     * @param dataStore stockage en memoire
     */
    private void loadUtilisateurs(DataStore dataStore) {
        // Etape 1 : definir la requete de lecture
        String sql = "SELECT * FROM utilisateurs";
        try (Statement statement = connection().createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            // Etape 2 : convertir chaque ligne en utilisateur
            while (rs.next()) {
                Utilisateur user = new Utilisateur(
                    rs.getString("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("email"),
                    rs.getString("mot_de_passe"),
                    Role.valueOf(rs.getString("role")),
                    parseDate(rs.getString("date_naissance")),
                    rs.getString("telephone"),
                    parseEnum(rs.getString("sexe"), Sexe.class),
                    buildAdresse(rs),
                    parseEnum(rs.getString("moyen_contact"), MoyenContact.class)
                );
                // Etape 3 : appliquer les flags et ajouter au store
                user.setPrimaryDirector(rs.getInt("primary_director") == 1);
                user.setApproved(rs.getInt("approved") == 1);
                dataStore.getUtilisateurs().add(user);
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Chargement utilisateurs impossible.", ex);
        }
    }

    /**
     * @Objectif Charger les medecins depuis la base.
     * @param dataStore stockage en memoire
     */
    private void loadMedecins(DataStore dataStore) {
        // Etape 1 : definir la requete de lecture
        String sql = "SELECT * FROM medecins";
        try (Statement statement = connection().createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            // Etape 2 : convertir chaque ligne en medecin
            while (rs.next()) {
                Medecin medecin = new Medecin(
                    rs.getString("id"),
                    rs.getString("matricule"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    parseDate(rs.getString("date_naissance")),
                    parseEnum(rs.getString("sexe"), Sexe.class),
                    rs.getString("telephone"),
                    rs.getString("email"),
                    buildAdresse(rs),
                    parseEnum(rs.getString("moyen_contact"), MoyenContact.class),
                    parseEnum(rs.getString("specialite"), Specialite.class)
                );
                // Etape 3 : ajouter au store
                dataStore.getMedecins().add(medecin);
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Chargement medecins impossible.", ex);
        }
    }

    /**
     * @Objectif Charger les patients depuis la base.
     * @param dataStore stockage en memoire
     */
    private void loadPatients(DataStore dataStore) {
        // Etape 1 : definir la requete de lecture
        String sql = "SELECT * FROM patients";
        try (Statement statement = connection().createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            // Etape 2 : convertir chaque ligne en patient
            while (rs.next()) {
                Patient patient = new Patient(
                    rs.getString("id"),
                    rs.getString("dossier_number"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    parseDate(rs.getString("date_naissance")),
                    parseEnum(rs.getString("sexe"), Sexe.class),
                    rs.getString("telephone"),
                    rs.getString("email"),
                    buildAdresse(rs),
                    parseEnum(rs.getString("moyen_contact"), MoyenContact.class),
                    parseEnum(rs.getString("etat"), EtatPatient.class)
                );
                // Etape 3 : ajouter au store
                dataStore.getPatients().add(patient);
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Chargement patients impossible.", ex);
        }
    }

    /**
     * @Objectif Charger les dossiers et les rattacher aux patients.
     * @param dataStore stockage en memoire
     */
    private void loadDossiers(DataStore dataStore) {
        // Etape 1 : definir la requete de lecture
        String sql = "SELECT * FROM dossiers";
        // Etape 2 : construire un index des patients
        Map<String, Patient> patientsById = new HashMap<>();
        for (Patient patient : dataStore.getPatients()) {
            patientsById.put(patient.getId(), patient);
        }
        try (Statement statement = connection().createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            // Etape 3 : convertir chaque ligne en dossier
            while (rs.next()) {
                DossierMedical dossier = new DossierMedical(
                    rs.getString("id"),
                    rs.getString("patient_id"),
                    parseDate(rs.getString("date_ouverture")),
                    parseEnum(rs.getString("etat"), EtatDossier.class)
                );
                dataStore.getDossiers().add(dossier);
                // Etape 4 : rattacher le dossier au patient
                Patient patient = patientsById.get(dossier.getPatientId());
                if (patient != null) {
                    patient.addDossier(dossier);
                }
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Chargement dossiers impossible.", ex);
        }
    }

    /**
     * @Objectif Charger les rendez-vous depuis la base.
     * @param dataStore stockage en memoire
     */
    private void loadRendezVous(DataStore dataStore) {
        // Etape 1 : definir la requete de lecture
        String sql = "SELECT * FROM rendezvous";
        try (Statement statement = connection().createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            // Etape 2 : convertir chaque ligne en rendez-vous
            while (rs.next()) {
                RendezVous rdv = new RendezVous(
                    rs.getString("id"),
                    rs.getString("patient_id"),
                    rs.getString("medecin_id"),
                    LocalDateTime.parse(rs.getString("date_heure"))
                );
                dataStore.getRendezVous().add(rdv);
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Chargement rendez-vous impossible.", ex);
        }
    }

    /**
     * @Objectif Charger les consultations depuis la base.
     * @param dataStore stockage en memoire
     */
    private void loadConsultations(DataStore dataStore) {
        // Etape 1 : definir la requete de lecture
        String sql = "SELECT * FROM consultations";
        try (Statement statement = connection().createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            // Etape 2 : convertir chaque ligne en consultation
            while (rs.next()) {
                Consultation consultation = new Consultation(
                    rs.getString("id"),
                    rs.getString("patient_id"),
                    rs.getString("medecin_id"),
                    rs.getString("dossier_id"),
                    parseDateTime(rs.getString("date")),
                    rs.getString("diagnostic"),
                    rs.getString("prescription")
                );
                dataStore.getConsultations().add(consultation);
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Chargement consultations impossible.", ex);
        }
    }

    /**
     * @Objectif Charger les documents medicaux depuis la base.
     * @param dataStore stockage en memoire
     */
    private void loadDocuments(DataStore dataStore) {
        // Etape 1 : definir la requete de lecture
        String sql = "SELECT * FROM documents";
        try (Statement statement = connection().createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            // Etape 2 : convertir chaque ligne en document
            while (rs.next()) {
                DocumentMedical document = new DocumentMedical(
                    rs.getString("id"),
                    rs.getString("patient_id"),
                    rs.getString("medecin_id"),
                    rs.getString("dossier_id"),
                    rs.getString("nom"),
                    parseEnum(rs.getString("type"), DocumentType.class),
                    parseDate(rs.getString("date_ajout"))
                );
                dataStore.getDocuments().add(document);
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Chargement documents impossible.", ex);
        }
    }

    /**
     * @Objectif Charger les demandes d'approbation directeurs.
     * @param dataStore stockage en memoire
     */
    private void loadDirectorApprovals(DataStore dataStore) {
        // Etape 1 : definir la requete de lecture
        String sql = "SELECT * FROM director_approvals";
        try (Statement statement = connection().createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            // Etape 2 : convertir chaque ligne en demande
            while (rs.next()) {
                DirectorApproval approval = new DirectorApproval(
                    rs.getString("user_id"),
                    rs.getString("code"),
                    LocalDateTime.parse(rs.getString("created_at"))
                );
                dataStore.getDirectorApprovals().add(approval);
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Chargement approvals impossible.", ex);
        }
    }

    /**
     * @Objectif Charger les notifications admin.
     * @param dataStore stockage en memoire
     */
    private void loadNotifications(DataStore dataStore) {
        // Etape 1 : definir la requete de lecture
        String sql = "SELECT * FROM admin_notifications";
        try (Statement statement = connection().createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            // Etape 2 : convertir chaque ligne en notification
            while (rs.next()) {
                AdminNotification notification = new AdminNotification(
                    rs.getString("id"),
                    rs.getString("message"),
                    LocalDateTime.parse(rs.getString("created_at"))
                );
                dataStore.getNotifications().add(notification);
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Chargement notifications impossible.", ex);
        }
    }

    /**
     * @Objectif Charger les tokens de reinitialisation.
     * @param dataStore stockage en memoire
     */
    private void loadResetTokens(DataStore dataStore) {
        // Etape 1 : definir la requete de lecture
        String sql = "SELECT * FROM reset_tokens";
        try (Statement statement = connection().createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            // Etape 2 : convertir chaque ligne en token
            while (rs.next()) {
                PasswordResetToken token = new PasswordResetToken(
                    rs.getString("email"),
                    rs.getString("code"),
                    LocalDateTime.parse(rs.getString("created_at"))
                );
                dataStore.getResetTokens().add(token);
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Chargement tokens impossible.", ex);
        }
    }

    /**
     * @Objectif Binder un utilisateur dans un PreparedStatement.
     * @param ps statement a remplir
     * @param user utilisateur
     * @throws SQLException erreur SQL
     */
    private void bindUtilisateur(PreparedStatement ps, Utilisateur user) throws SQLException {
        // Etape 1 : renseigner les champs simples
        ps.setString(1, user.getId());
        ps.setString(2, user.getNom());
        ps.setString(3, user.getPrenom());
        ps.setString(4, user.getEmail());
        ps.setString(5, user.getMotDePasse());
        ps.setString(6, user.getRole() != null ? user.getRole().name() : null);
        ps.setString(7, user.getDateNaissance() != null ? user.getDateNaissance().toString() : null);
        ps.setString(8, user.getTelephone());
        ps.setString(9, user.getSexe() != null ? user.getSexe().name() : null);
        // Etape 2 : renseigner l'adresse et les champs restants
        bindAdresse(ps, user.getAdresse(), 10);
        ps.setString(14, user.getMoyenContact() != null ? user.getMoyenContact().name() : null);
        ps.setInt(15, user.isPrimaryDirector() ? 1 : 0);
        ps.setInt(16, user.isApproved() ? 1 : 0);
    }

    /**
     * @Objectif Binder une adresse dans un PreparedStatement.
     * @param ps statement a remplir
     * @param adresse adresse a binder
     * @param startIndex index de depart
     * @throws SQLException erreur SQL
     */
    private void bindAdresse(PreparedStatement ps, Adresse adresse, int startIndex) throws SQLException {
        if (adresse == null) {
            // Etape 1 : renseigner des valeurs nulles
            ps.setString(startIndex, null);
            ps.setString(startIndex + 1, null);
            ps.setString(startIndex + 2, null);
            ps.setString(startIndex + 3, null);
            return;
        }
        // Etape 2 : renseigner les champs d'adresse
        ps.setString(startIndex, adresse.getRue());
        ps.setString(startIndex + 1, adresse.getCodePostal());
        ps.setString(startIndex + 2, adresse.getVille());
        ps.setString(startIndex + 3, adresse.getPays());
    }

    /**
     * @Objectif Construire une adresse depuis un ResultSet.
     * @param rs resultat SQL
     * @return adresse construite
     * @throws SQLException erreur SQL
     */
    private Adresse buildAdresse(ResultSet rs) throws SQLException {
        // Etape 1 : lire les champs d'adresse
        return new Adresse(
            rs.getString("adresse_rue"),
            rs.getString("adresse_code_postal"),
            rs.getString("adresse_ville"),
            rs.getString("adresse_pays")
        );
    }

    /**
     * @Objectif Convertir une valeur texte en enum.
     * @param value valeur en base
     * @param enumType type d'enum
     * @return enum ou null
     */
    private <T extends Enum<T>> T parseEnum(String value, Class<T> enumType) {
        // Etape 1 : verifier la valeur
        if (value == null || value.isBlank()) {
            return null;
        }
        // Etape 2 : convertir la valeur en enum
        return Enum.valueOf(enumType, value);
    }

    /**
     * @Objectif Convertir une date texte en LocalDate.
     * @param value valeur en base
     * @return date ou null
     */
    private LocalDate parseDate(String value) {
        // Etape 1 : verifier la valeur
        if (value == null || value.isBlank()) {
            return null;
        }
        // Etape 2 : parser la date
        return LocalDate.parse(value);
    }

    /**
     * @Objectif Convertir une date-heure texte en LocalDateTime.
     * @param value valeur en base
     * @return date-heure ou null
     */
    private LocalDateTime parseDateTime(String value) {
        // Etape 1 : verifier la valeur
        if (value == null || value.isBlank()) {
            return null;
        }
        // Etape 2 : parser la date-heure
        return LocalDateTime.parse(value);
    }

    /**
     * @Objectif Acceder a la connexion JDBC active.
     * @return connexion JDBC
     */
    private Connection connection() {
        return databaseManager.getConnection();
    }

    /**
     * @Objectif Supprimer des enregistrements par identifiant.
     * @param table table cible
     * @param column colonne de filtrage
     * @param value valeur a filtrer
     * @throws SQLException erreur SQL
     */
    private void deleteById(String table, String column, String value) throws SQLException {
        // Etape 1 : definir la requete de suppression
        String sql = "DELETE FROM " + table + " WHERE " + column + " = ?";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            // Etape 2 : binder la valeur
            ps.setString(1, value);
            // Etape 3 : executer la suppression
            ps.executeUpdate();
        }
    }

    /**
     * @Objectif Annuler la transaction en cas d'erreur.
     */
    private void rollbackQuietly() {
        try {
            // Etape 1 : rollback silencieux
            connection().rollback();
        } catch (SQLException ignore) {
        }
    }

    /**
     * @Objectif Revenir au mode auto-commit.
     */
    private void resetAutoCommit() {
        try {
            // Etape 1 : reinitialiser auto-commit
            connection().setAutoCommit(true);
        } catch (SQLException ignore) {
        }
    }
}
