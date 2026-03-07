package com.rivaldo.hopital.domain;

/**
 * @Fichier Adresse.java
 * @Objectif Regrouper une adresse postale simple.
 * @Couche Domain
 */
public class Adresse {
    // Rue et numero, exemple: "10 Rue de Paris"
    private String rue;
    // Code postal, exemple: "75000"
    private String codePostal;
    // Ville, exemple: "Paris"
    private String ville;
    // Pays, exemple: "France"
    private String pays;

    /**
     * @Objectif Constructeur vide (utile pour les frameworks et tests).
     */
    public Adresse() {
        // Constructeur vide: rien a initialiser ici
    }

    /**
     * @Objectif Construire une adresse complete.
     * @param rue rue et numero
     * @param codePostal code postal
     * @param ville ville
     * @param pays pays
     */
    public Adresse(String rue, String codePostal, String ville, String pays) {
        // Etape 1: stocker la rue
        this.rue = rue;
        // Etape 2: stocker le code postal
        this.codePostal = codePostal;
        // Etape 3: stocker la ville
        this.ville = ville;
        // Etape 4: stocker le pays
        this.pays = pays;
    }

    /**
     * @Objectif Lire la rue.
     * @return rue
     */
    public String getRue() {
        // Retourner la rue
        return rue;
    }

    /**
     * @Objectif Modifier la rue.
     * @param rue nouvelle rue
     */
    public void setRue(String rue) {
        // Mettre a jour la rue
        this.rue = rue;
    }

    /**
     * @Objectif Lire le code postal.
     * @return code postal
     */
    public String getCodePostal() {
        // Retourner le code postal
        return codePostal;
    }

    /**
     * @Objectif Modifier le code postal.
     * @param codePostal nouveau code postal
     */
    public void setCodePostal(String codePostal) {
        // Mettre a jour le code postal
        this.codePostal = codePostal;
    }

    /**
     * @Objectif Lire la ville.
     * @return ville
     */
    public String getVille() {
        // Retourner la ville
        return ville;
    }

    /**
     * @Objectif Modifier la ville.
     * @param ville nouvelle ville
     */
    public void setVille(String ville) {
        // Mettre a jour la ville
        this.ville = ville;
    }

    /**
     * @Objectif Lire le pays.
     * @return pays
     */
    public String getPays() {
        // Retourner le pays
        return pays;
    }

    /**
     * @Objectif Modifier le pays.
     * @param pays nouveau pays
     */
    public void setPays(String pays) {
        // Mettre a jour le pays
        this.pays = pays;
    }
}
