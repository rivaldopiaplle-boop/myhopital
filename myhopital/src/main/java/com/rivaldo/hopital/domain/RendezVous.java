package com.rivaldo.hopital.domain;

import java.time.LocalDateTime;

/**
 * @Fichier RendezVous.java
 * @Objectif Representer un rendez-vous patient/medecin.
 * @Couche Domain
 */
public class RendezVous {
    // Identifiant unique du rendez-vous
    private String id;
    // Identifiant du patient
    private String patientId;
    // Identifiant du medecin
    private String medecinId;
    // Date et heure du rendez-vous
    private LocalDateTime dateHeure;

    /**
     * @Objectif Constructeur vide (utile pour les frameworks et tests).
     */
    public RendezVous() {
        // Constructeur vide: rien a initialiser ici
    }

    /**
     * @Objectif Construire un rendez-vous complet.
     */
    public RendezVous(String id, String patientId, String medecinId, LocalDateTime dateHeure) {
        // Etape 1: stocker l'id
        this.id = id;
        // Etape 2: stocker l'id patient
        this.patientId = patientId;
        // Etape 3: stocker l'id medecin
        this.medecinId = medecinId;
        // Etape 4: stocker la date/heure
        this.dateHeure = dateHeure;
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
     * @Objectif Lire la date/heure.
     * @return date/heure
     */
    public LocalDateTime getDateHeure() {
        // Retourner la date/heure
        return dateHeure;
    }

    /**
     * @Objectif Modifier la date/heure.
     * @param dateHeure nouvelle date/heure
     */
    public void setDateHeure(LocalDateTime dateHeure) {
        // Mettre a jour la date/heure
        this.dateHeure = dateHeure;
    }
}
