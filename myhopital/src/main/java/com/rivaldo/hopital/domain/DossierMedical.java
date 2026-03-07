package com.rivaldo.hopital.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @Fichier DossierMedical.java
 * @Objectif Representer un dossier medical d'un patient.
 * @Couche Domain
 */
public class DossierMedical {
    // Identifiant unique du dossier
    private String id;
    // Identifiant du patient lie a ce dossier
    private String patientId;
    // Date d'ouverture du dossier
    private LocalDate dateOuverture;
    // Etat du dossier (ouvert, ferme, archive)
    private EtatDossier etat;
    // Liste des consultations liees au dossier
    private final List<Consultation> consultations = new ArrayList<>();
    // Liste des documents lies au dossier
    private final List<DocumentMedical> documents = new ArrayList<>();

    /**
     * @Objectif Constructeur vide (utile pour les frameworks et tests).
     */
    public DossierMedical() {
        // Constructeur vide: rien a initialiser ici
    }

    /**
     * @Objectif Construire un dossier medical complet.
     */
    public DossierMedical(String id, String patientId, LocalDate dateOuverture, EtatDossier etat) {
        // Etape 1: stocker l'id
        this.id = id;
        // Etape 2: stocker l'id patient
        this.patientId = patientId;
        // Etape 3: stocker la date d'ouverture
        this.dateOuverture = dateOuverture;
        // Etape 4: stocker l'etat
        this.etat = etat;
    }

    /**
     * @Objectif Lire l'identifiant.
     * @return identifiant
     */
    public String getId() {
        // Retourner l'id
        return id;
    }

    /**
     * @Objectif Modifier l'identifiant.
     * @param id nouvel identifiant
     */
    public void setId(String id) {
        // Mettre a jour l'id
        this.id = id;
    }

    /**
     * @Objectif Lire l'id du patient.
     * @return id patient
     */
    public String getPatientId() {
        // Retourner l'id patient
        return patientId;
    }

    /**
     * @Objectif Modifier l'id du patient.
     * @param patientId nouvel id patient
     */
    public void setPatientId(String patientId) {
        // Mettre a jour l'id patient
        this.patientId = patientId;
    }

    /**
     * @Objectif Lire la date d'ouverture.
     * @return date d'ouverture
     */
    public LocalDate getDateOuverture() {
        // Retourner la date d'ouverture
        return dateOuverture;
    }

    /**
     * @Objectif Modifier la date d'ouverture.
     * @param dateOuverture nouvelle date
     */
    public void setDateOuverture(LocalDate dateOuverture) {
        // Mettre a jour la date d'ouverture
        this.dateOuverture = dateOuverture;
    }

    /**
     * @Objectif Lire l'etat du dossier.
     * @return etat
     */
    public EtatDossier getEtat() {
        // Retourner l'etat
        return etat;
    }

    /**
     * @Objectif Modifier l'etat du dossier.
     * @param etat nouvel etat
     */
    public void setEtat(EtatDossier etat) {
        // Mettre a jour l'etat
        this.etat = etat;
    }

    /**
     * @Objectif Lire la liste des consultations.
     * @return consultations
     */
    public List<Consultation> getConsultations() {
        // Retourner la liste des consultations
        return consultations;
    }

    /**
     * @Objectif Lire la liste des documents.
     * @return documents
     */
    public List<DocumentMedical> getDocuments() {
        // Retourner la liste des documents
        return documents;
    }

    /**
     * @Objectif Ajouter une consultation dans le dossier.
     * @param consultation consultation a ajouter
     */
    public void addConsultation(Consultation consultation) {
        // Etape 1: Verifier que la consultation existe
        if (consultation != null) {
            // Etape 2: Ajouter la consultation a la liste
            consultations.add(consultation);
        }
    }

    /**
     * @Objectif Ajouter un document dans le dossier.
     * @param document document a ajouter
     */
    public void addDocument(DocumentMedical document) {
        // Etape 1: Verifier que le document existe
        if (document != null) {
            // Etape 2: Ajouter le document a la liste
            documents.add(document);
        }
    }
}
