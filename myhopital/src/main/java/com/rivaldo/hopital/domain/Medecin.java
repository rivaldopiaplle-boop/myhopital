package com.rivaldo.hopital.domain;

import java.time.LocalDate;

/**
 * @Fichier Medecin.java
 * @Objectif Representer un medecin dans le systeme.
 * @Couche Domain
 */
public class Medecin {
    // Identifiant unique du medecin
    private String id;
    // Matricule interne de l'hopital
    private String matricule;
    // Nom du medecin
    private String nom;
    // Prenom du medecin
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
    // Specialite medicale
    private Specialite specialite;

    /**
     * @Objectif Constructeur vide (utile pour les frameworks et tests).
     */
    public Medecin() {
        // Constructeur vide: rien a initialiser ici
    }

    /**
     * @Objectif Construire un medecin complet.
     */
    public Medecin(String id, String matricule, String nom, String prenom, LocalDate dateNaissance, Sexe sexe,
                   String telephone, String email, Adresse adresse, MoyenContact moyenContact, Specialite specialite) {
        // Etape 1: stocker l'id
        this.id = id;
        // Etape 2: stocker le matricule
        this.matricule = matricule;
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
        // Etape 11: stocker la specialite
        this.specialite = specialite;
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
     * @Objectif Lire le matricule.
     * @return matricule
     */
    public String getMatricule() {
        // Retourner le matricule
        return matricule;
    }

    /**
     * @Objectif Modifier le matricule.
     * @param matricule nouveau matricule
     */
    public void setMatricule(String matricule) {
        // Mettre a jour le matricule
        this.matricule = matricule;
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
     * @Objectif Lire la specialite.
     * @return specialite
     */
    public Specialite getSpecialite() {
        // Retourner la specialite
        return specialite;
    }

    /**
     * @Objectif Modifier la specialite.
     * @param specialite nouvelle specialite
     */
    public void setSpecialite(Specialite specialite) {
        // Mettre a jour la specialite
        this.specialite = specialite;
    }
}
