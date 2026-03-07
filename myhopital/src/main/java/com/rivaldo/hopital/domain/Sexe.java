package com.rivaldo.hopital.domain;

/**
 * @Fichier Sexe.java
 * @Objectif Lister les valeurs possibles pour le sexe.
 * @Couche Domain
 */
public enum Sexe {
    HOMME("H"),
    FEMME("F"),
    AUTRE("A");

    private final String code;

    Sexe(String code) {
        // Stocker le code court
        this.code = code;
    }

    public String getCode() {
    /**
     * @Objectif Lire le code court.
     * @return code
     */
        // Retourner le code court
        return code;
    }
}
