package com.rivaldo.hopital.domain;

/**
 * @Fichier Utilisateur.java
 * @Objectif Representer un utilisateur du systeme.
 * @Couche Domain
 */
public class Utilisateur {
    // Identifiant unique
    private String id;
    // Nom
    private String nom;
    // Prenom
    private String prenom;
    // Email de connexion
    private String email;
    // Mot de passe
    private String motDePasse;
    // Role (directeur, medecin, gestionnaire)
    private Role role;
    // Date de naissance
    private java.time.LocalDate dateNaissance;
    // Telephone
    private String telephone;
    // Sexe
    private Sexe sexe;
    // Adresse
    private Adresse adresse;
    // Moyen de contact prefere
    private MoyenContact moyenContact;
    // Indique si c'est le directeur principal
    private boolean primaryDirector;
    // Indique si l'utilisateur est approuve
    private boolean approved;

    /**
     * @Objectif Constructeur par defaut.
     */
    public Utilisateur() {
        // Constructeur vide: rien a initialiser ici
    }

    /**
     * @Objectif Construire un utilisateur complet.
     * @param id identifiant
     * @param nom nom
     * @param prenom prenom
     * @param email email
     * @param motDePasse mot de passe
     * @param role role
     * @param dateNaissance date de naissance
     * @param telephone telephone
     * @param sexe sexe
     * @param adresse adresse
     * @param moyenContact moyen de contact
     */
    public Utilisateur(String id, String nom, String prenom, String email, String motDePasse, Role role,
                       java.time.LocalDate dateNaissance, String telephone, Sexe sexe, Adresse adresse,
                       MoyenContact moyenContact) {
        // Etape 1: stocker l'id
        this.id = id;
        // Etape 2: stocker le nom
        this.nom = nom;
        // Etape 3: stocker le prenom
        this.prenom = prenom;
        // Etape 4: stocker l'email
        this.email = email;
        // Etape 5: stocker le mot de passe
        this.motDePasse = motDePasse;
        // Etape 6: stocker le role
        this.role = role;
        // Etape 7: stocker la date de naissance
        this.dateNaissance = dateNaissance;
        // Etape 8: stocker le telephone
        this.telephone = telephone;
        // Etape 9: stocker le sexe
        this.sexe = sexe;
        // Etape 10: stocker l'adresse
        this.adresse = adresse;
        // Etape 11: stocker le moyen de contact
        this.moyenContact = moyenContact;
        // Etape 12: initialiser directeur principal a false
        this.primaryDirector = false;
        // Etape 13: initialiser approuve a true
        this.approved = true;
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
     * @Objectif Lire le mot de passe.
     * @return mot de passe
     */
    public String getMotDePasse() {
        // Retourner le mot de passe
        return motDePasse;
    }

    /**
     * @Objectif Modifier le mot de passe.
     * @param motDePasse nouveau mot de passe
     */
    public void setMotDePasse(String motDePasse) {
        // Mettre a jour le mot de passe
        this.motDePasse = motDePasse;
    }

    /**
     * @Objectif Lire le role.
     * @return role
     */
    public Role getRole() {
        // Retourner le role
        return role;
    }

    /**
     * @Objectif Modifier le role.
     * @param role nouveau role
     */
    public void setRole(Role role) {
        // Mettre a jour le role
        this.role = role;
    }

    /**
     * @Objectif Lire la date de naissance.
     * @return date de naissance
     */
    public java.time.LocalDate getDateNaissance() {
        // Retourner la date de naissance
        return dateNaissance;
    }

    /**
     * @Objectif Modifier la date de naissance.
     * @param dateNaissance nouvelle date
     */
    public void setDateNaissance(java.time.LocalDate dateNaissance) {
        // Mettre a jour la date de naissance
        this.dateNaissance = dateNaissance;
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
     * @Objectif Savoir si l'utilisateur est directeur principal.
     * @return true si directeur principal
     */
    public boolean isPrimaryDirector() {
        // Retourner true si directeur principal
        return primaryDirector;
    }

    /**
     * @Objectif Modifier le statut directeur principal.
     * @param primaryDirector nouveau statut
     */
    public void setPrimaryDirector(boolean primaryDirector) {
        // Mettre a jour le statut directeur principal
        this.primaryDirector = primaryDirector;
    }

    /**
     * @Objectif Savoir si l'utilisateur est approuve.
     * @return true si approuve
     */
    public boolean isApproved() {
        // Retourner true si approuve
        return approved;
    }

    /**
     * @Objectif Modifier le statut d'approbation.
     * @param approved nouveau statut
     */
    public void setApproved(boolean approved) {
        // Mettre a jour le statut d'approbation
        this.approved = approved;
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Les informations utilisateur etaient minimales.
Cause: Pas de champs complets a l'inscription.
Consequence: Donnees de contact manquantes.
Solution: Ajouter naissance, contact et adresse.
Pourquoi: Assurer un suivi fiable.
Comment: Champs supplementaires + getters/setters.
*/
