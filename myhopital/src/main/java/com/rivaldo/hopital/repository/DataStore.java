package com.rivaldo.hopital.repository;

import com.rivaldo.hopital.domain.AdminNotification;
import com.rivaldo.hopital.domain.Consultation;
import com.rivaldo.hopital.domain.DirectorApproval;
import com.rivaldo.hopital.domain.DocumentMedical;
import com.rivaldo.hopital.domain.DossierMedical;
import com.rivaldo.hopital.domain.Medecin;
import com.rivaldo.hopital.domain.PasswordResetToken;
import com.rivaldo.hopital.domain.Patient;
import com.rivaldo.hopital.domain.RendezVous;
import com.rivaldo.hopital.domain.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @Fichier DataStore.java
 * @Objectif Stockage en memoire pour l'application.
 * @Couche Repository
 */
public class DataStore {
    // Liste des patients
    private final ObservableList<Patient> patients = FXCollections.observableArrayList();
    // Liste des medecins
    private final ObservableList<Medecin> medecins = FXCollections.observableArrayList();
    // Liste des rendez-vous
    private final ObservableList<RendezVous> rendezVous = FXCollections.observableArrayList();
    // Liste des consultations
    private final ObservableList<Consultation> consultations = FXCollections.observableArrayList();
    // Liste des documents medicaux
    private final ObservableList<DocumentMedical> documents = FXCollections.observableArrayList();
    // Liste des dossiers medicaux
    private final ObservableList<DossierMedical> dossiers = FXCollections.observableArrayList();
    // Liste des utilisateurs (connexion, roles)
    private final ObservableList<Utilisateur> utilisateurs = FXCollections.observableArrayList();
    // Notifications admin (approbations)
    private final ObservableList<AdminNotification> notifications = FXCollections.observableArrayList();
    // Demandes d'approbation des directeurs
    private final ObservableList<DirectorApproval> directorApprovals = FXCollections.observableArrayList();
    // Codes temporaires de reinitialisation
    private final ObservableList<PasswordResetToken> resetTokens = FXCollections.observableArrayList();

    /**
     * @Objectif Acceder a la liste des patients.
     * @return patients
     */
    public ObservableList<Patient> getPatients() {
        // Retourner la liste des patients
        return patients;
    }

    /**
     * @Objectif Acceder a la liste des medecins.
     * @return medecins
     */
    public ObservableList<Medecin> getMedecins() {
        // Retourner la liste des medecins
        return medecins;
    }

    /**
     * @Objectif Acceder a la liste des rendez-vous.
     * @return rendez-vous
     */
    public ObservableList<RendezVous> getRendezVous() {
        // Retourner la liste des rendez-vous
        return rendezVous;
    }

    /**
     * @Objectif Acceder a la liste des consultations.
     * @return consultations
     */
    public ObservableList<Consultation> getConsultations() {
        // Retourner la liste des consultations
        return consultations;
    }

    /**
     * @Objectif Acceder a la liste des documents.
     * @return documents
     */
    public ObservableList<DocumentMedical> getDocuments() {
        // Retourner la liste des documents
        return documents;
    }

    /**
     * @Objectif Acceder a la liste des dossiers.
     * @return dossiers
     */
    public ObservableList<DossierMedical> getDossiers() {
        // Retourner la liste des dossiers
        return dossiers;
    }

    /**
     * @Objectif Acceder a la liste des utilisateurs.
     * @return utilisateurs
     */
    public ObservableList<Utilisateur> getUtilisateurs() {
        // Retourner la liste des utilisateurs
        return utilisateurs;
    }

    /**
     * @Objectif Acceder aux notifications admin.
     * @return notifications
     */
    public ObservableList<AdminNotification> getNotifications() {
        // Retourner la liste des notifications
        return notifications;
    }

    /**
     * @Objectif Acceder aux demandes d'approbation.
     * @return demandes
     */
    public ObservableList<DirectorApproval> getDirectorApprovals() {
        // Retourner la liste des demandes d'approbation
        return directorApprovals;
    }

    /**
     * @Objectif Acceder aux tokens de reinitialisation.
     * @return tokens
     */
    public ObservableList<PasswordResetToken> getResetTokens() {
        // Retourner la liste des tokens de reset
        return resetTokens;
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Besoin d'un stockage simple avant la BD.
Cause: Le projet est en phase de prototypage.
Consequence: Les donnees sont perdues au redemarrage.
Solution: Utiliser un DataStore en memoire.
Pourquoi: Accelere la mise en place de l'UI.
Comment: ObservableList pour synchroniser l'interface.
*/
