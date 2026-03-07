package com.rivaldo.hopital.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @Fichier Patient.java
 * @Objectif Representer un patient dans le systeme.
 * @Couche Domain
 */
public class Patient {
    // Identifiant unique du patient
    private String id;
    // Numero de dossier administratif
    private String dossierNumber;
    // Nom du patient
    private String nom;
    // Prenom du patient
    private String prenom;
    // Date de naissance
    private LocalDate dateNaissance;
    // Sexe
    private Sexe sexe;
    // Telephone
    private String telephone;
    // Email
    private String email;
    // Adresse postale
    private Adresse adresse;
    // Moyen de contact prefere
    private MoyenContact moyenContact;
    // Etat du patient (actif, inactif, etc.)
    private EtatPatient etat;
    // Liste des dossiers medicaux du patient
    private final List<DossierMedical> dossiers = new ArrayList<>();

    /**
     * @Objectif Constructeur vide (utile pour les frameworks et tests).
     */
    public Patient() {
        // Constructeur vide: rien a initialiser ici
    }

    /**
     * @Objectif Construire un patient complet.
     */
    public Patient(String id, String dossierNumber, String nom, String prenom, LocalDate dateNaissance,
                   Sexe sexe, String telephone, String email, Adresse adresse, MoyenContact moyenContact,
                   EtatPatient etat) {
        // Etape 1: stocker l'id
        this.id = id;
        // Etape 2: stocker le numero de dossier
        this.dossierNumber = dossierNumber;
        // Etape 3: stocker le nom
        this.nom = nom;
        // Etape 4: stocker le prenom
        this.prenom = prenom;
        // Etape 5: stocker la date de naissance
        this.dateNaissance = dateNaissance;
        // Etape 6: stocker le sexe
        this.sexe = sexe;
        // Etape 7: stocker le telephone
        this.telephone = telephone;
        // Etape 8: stocker l'email
        this.email = email;
        // Etape 9: stocker l'adresse
        this.adresse = adresse;
        // Etape 10: stocker le moyen de contact
        this.moyenContact = moyenContact;
        // Etape 11: stocker l'etat du patient
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
     * @Objectif Lire le numero de dossier.
     * @return numero de dossier
     */
    public String getDossierNumber() {
        // Retourner le numero de dossier
        return dossierNumber;
    }

    /**
     * @Objectif Modifier le numero de dossier.
     * @param dossierNumber nouveau numero
     */
    public void setDossierNumber(String dossierNumber) {
        // Mettre a jour le numero de dossier
        this.dossierNumber = dossierNumber;
    }

    /**
     * @Objectif Lire le nom.
     * @return nom
     */
    public String getNom() {
        // Retourner le nom
        return nom;
    }

    /**
     * @Objectif Modifier le nom.
     * @param nom nouveau nom
     */
    public void setNom(String nom) {
        // Mettre a jour le nom
        this.nom = nom;
    }

    /**
     * @Objectif Lire le prenom.
     * @return prenom
     */
    public String getPrenom() {
        // Retourner le prenom
        return prenom;
    }

    /**
     * @Objectif Modifier le prenom.
     * @param prenom nouveau prenom
     */
    public void setPrenom(String prenom) {
        // Mettre a jour le prenom
        this.prenom = prenom;
    }

    /**
     * @Objectif Lire la date de naissance.
     * @return date de naissance
     */
    public LocalDate getDateNaissance() {
        // Retourner la date de naissance
        return dateNaissance;
    }

    /**
     * @Objectif Modifier la date de naissance.
     * @param dateNaissance nouvelle date
     */
    public void setDateNaissance(LocalDate dateNaissance) {
        // Mettre a jour la date de naissance
        this.dateNaissance = dateNaissance;
    }

    /**
     * @Objectif Lire le sexe.
     * @return sexe
     */
    public Sexe getSexe() {
        // Retourner le sexe
        return sexe;
    }

    /**
     * @Objectif Modifier le sexe.
     * @param sexe nouveau sexe
     */
    public void setSexe(Sexe sexe) {
        // Mettre a jour le sexe
        this.sexe = sexe;
    }

    /**
     * @Objectif Lire le telephone.
     * @return telephone
     */
    public String getTelephone() {
        // Retourner le telephone
        return telephone;
    }

    /**
     * @Objectif Modifier le telephone.
     * @param telephone nouveau telephone
     */
    public void setTelephone(String telephone) {
        // Mettre a jour le telephone
        this.telephone = telephone;
    }

    /**
     * @Objectif Lire l'email.
     * @return email
     */
    public String getEmail() {
        // Retourner l'email
        return email;
    }

    /**
     * @Objectif Modifier l'email.
     * @param email nouvel email
     */
    public void setEmail(String email) {
        // Mettre a jour l'email
        this.email = email;
    }

    /**
     * @Objectif Lire l'adresse.
     * @return adresse
     */
    public Adresse getAdresse() {
        // Retourner l'adresse
        return adresse;
    }

    /**
     * @Objectif Modifier l'adresse.
     * @param adresse nouvelle adresse
     */
    public void setAdresse(Adresse adresse) {
        // Mettre a jour l'adresse
        this.adresse = adresse;
    }

    /**
     * @Objectif Lire le moyen de contact prefere.
     * @return moyen de contact
     */
    public MoyenContact getMoyenContact() {
        // Retourner le moyen de contact
        return moyenContact;
    }

    /**
     * @Objectif Modifier le moyen de contact prefere.
     * @param moyenContact nouveau moyen
     */
    public void setMoyenContact(MoyenContact moyenContact) {
        // Mettre a jour le moyen de contact
        this.moyenContact = moyenContact;
    }

    /**
     * @Objectif Lire l'etat.
     * @return etat
     */
    public EtatPatient getEtat() {
        // Retourner l'etat
        return etat;
    }

    /**
     * @Objectif Modifier l'etat.
     * @param etat nouvel etat
     */
    public void setEtat(EtatPatient etat) {
        // Mettre a jour l'etat
        this.etat = etat;
    }

    /**
     * @Objectif Lire la liste des dossiers.
     * @return dossiers
     */
    public List<DossierMedical> getDossiers() {
        // Retourner la liste des dossiers
        return dossiers;
    }

    /**
     * @Objectif Ajouter un dossier au patient.
     * @param dossier dossier a ajouter
     */
    public void addDossier(DossierMedical dossier) {
        // Etape 1: Verifier que le dossier existe
        if (dossier != null) {
            // Etape 2: Ajouter le dossier a la liste
            dossiers.add(dossier);
        }
    }
}
