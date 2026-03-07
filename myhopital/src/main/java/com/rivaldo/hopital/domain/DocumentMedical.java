package com.rivaldo.hopital.domain;

import java.time.LocalDate;

/**
 * @Fichier DocumentMedical.java
 * @Objectif Representer un document medical dans un dossier.
 * @Couche Domain
 */
public class DocumentMedical {
    // Identifiant unique du document
    private String id;
    // Identifiant du patient concerne
    private String patientId;
    // Identifiant du medecin qui a cree le document
    private String medecinId;
    // Identifiant du dossier medical
    private String dossierId;
    // Nom du document (ex: "radio thorax")
    private String nom;
    // Type du document (examen, ordonnance...)
    private DocumentType type;
    // Date d'ajout du document
    private LocalDate dateAjout;

    /**
     * @Objectif Constructeur vide (utile pour les frameworks et tests).
     */
    public DocumentMedical() {
        // Constructeur vide: rien a initialiser ici
    }

    /**
     * @Objectif Construire un document medical complet.
     */
    public DocumentMedical(String id, String patientId, String medecinId, String dossierId, String nom,
                           DocumentType type, LocalDate dateAjout) {
        // Etape 1: stocker l'id
        this.id = id;
        // Etape 2: stocker l'id patient
        this.patientId = patientId;
        // Etape 3: stocker l'id medecin
        this.medecinId = medecinId;
        // Etape 4: stocker l'id dossier
        this.dossierId = dossierId;
        // Etape 5: stocker le nom du document
        this.nom = nom;
        // Etape 6: stocker le type de document
        this.type = type;
        // Etape 7: stocker la date d'ajout
        this.dateAjout = dateAjout;
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
     * @Objectif Lire l'id du medecin.
     * @return id medecin
     */
    public String getMedecinId() {
        // Retourner l'id medecin
        return medecinId;
    }

    /**
     * @Objectif Modifier l'id du medecin.
     * @param medecinId nouvel id medecin
     */
    public void setMedecinId(String medecinId) {
        // Mettre a jour l'id medecin
        this.medecinId = medecinId;
    }

    /**
     * @Objectif Lire l'id du dossier.
     * @return id dossier
     */
    public String getDossierId() {
        // Retourner l'id dossier
        return dossierId;
    }

    /**
     * @Objectif Modifier l'id du dossier.
     * @param dossierId nouvel id dossier
     */
    public void setDossierId(String dossierId) {
        // Mettre a jour l'id dossier
        this.dossierId = dossierId;
    }

    /**
     * @Objectif Lire le nom du document.
     * @return nom
     */
    public String getNom() {
        // Retourner le nom
        return nom;
    }

    /**
     * @Objectif Modifier le nom du document.
     * @param nom nouveau nom
     */
    public void setNom(String nom) {
        // Mettre a jour le nom
        this.nom = nom;
    }

    /**
     * @Objectif Lire le type du document.
     * @return type
     */
    public DocumentType getType() {
        // Retourner le type
        return type;
    }

    /**
     * @Objectif Modifier le type du document.
     * @param type nouveau type
     */
    public void setType(DocumentType type) {
        // Mettre a jour le type
        this.type = type;
    }

    /**
     * @Objectif Lire la date d'ajout.
     * @return date d'ajout
     */
    public LocalDate getDateAjout() {
        // Retourner la date d'ajout
        return dateAjout;
    }

    /**
     * @Objectif Modifier la date d'ajout.
     * @param dateAjout nouvelle date
     */
    public void setDateAjout(LocalDate dateAjout) {
        // Mettre a jour la date d'ajout
        this.dateAjout = dateAjout;
    }
}
