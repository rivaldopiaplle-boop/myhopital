package com.rivaldo.hopital.domain;

import java.time.LocalDateTime;

/**
 * @Fichier Consultation.java
 * @Objectif Representer une consultation entre un patient et un medecin.
 * @Couche Domain
 */
public class Consultation {
    // Identifiant unique de la consultation
    private String id;
    // Identifiant du patient concerne
    private String patientId;
    // Identifiant du medecin concerne
    private String medecinId;
    // Identifiant du dossier medical lie
    private String dossierId;
    // Date et heure de la consultation
    private LocalDateTime date;
    // Diagnostic saisi par le medecin
    private String diagnostic;
    // Prescription saisi par le medecin
    private String prescription;

    /**
     * @Objectif Constructeur vide (utile pour les frameworks et tests).
     */
    public Consultation() {
        // Constructeur vide: rien a initialiser ici
    }

    /**
     * @Objectif Construire une consultation complete.
     */
    public Consultation(String id, String patientId, String medecinId, String dossierId, LocalDateTime date,
                        String diagnostic, String prescription) {
        // Etape 1: stocker l'id
        this.id = id;
        // Etape 2: stocker l'id patient
        this.patientId = patientId;
        // Etape 3: stocker l'id medecin
        this.medecinId = medecinId;
        // Etape 4: stocker l'id dossier
        this.dossierId = dossierId;
        // Etape 5: stocker la date
        this.date = date;
        // Etape 6: stocker le diagnostic
        this.diagnostic = diagnostic;
        // Etape 7: stocker la prescription
        this.prescription = prescription;
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
     * @Objectif Lire la date.
     * @return date
     */
    public LocalDateTime getDate() {
        // Retourner la date
        return date;
    }

    /**
     * @Objectif Modifier la date.
     * @param date nouvelle date
     */
    public void setDate(LocalDateTime date) {
        // Mettre a jour la date
        this.date = date;
    }

    /**
     * @Objectif Lire le diagnostic.
     * @return diagnostic
     */
    public String getDiagnostic() {
        // Retourner le diagnostic
        return diagnostic;
    }

    /**
     * @Objectif Modifier le diagnostic.
     * @param diagnostic nouveau diagnostic
     */
    public void setDiagnostic(String diagnostic) {
        // Mettre a jour le diagnostic
        this.diagnostic = diagnostic;
    }

    /**
     * @Objectif Lire la prescription.
     * @return prescription
     */
    public String getPrescription() {
        // Retourner la prescription
        return prescription;
    }

    /**
     * @Objectif Modifier la prescription.
     * @param prescription nouvelle prescription
     */
    public void setPrescription(String prescription) {
        // Mettre a jour la prescription
        this.prescription = prescription;
    }
}
