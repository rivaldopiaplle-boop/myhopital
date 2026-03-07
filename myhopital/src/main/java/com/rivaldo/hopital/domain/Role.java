package com.rivaldo.hopital.domain;

/**
 * @Fichier Role.java
 * @Objectif Definir les roles utilisateurs.
 * @Couche Domain
 */
public enum Role {
    DIRECTEUR("Directeur"),
    GESTIONNAIRE("Gestionnaire"),
    MEDECIN("Medecin");

    private final String label;

    Role(String label) {
        // Stocker le label lisible
        this.label = label;
    }

    public String getLabel() {
        // Retourner le label
        return label;
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Les droits n'etaient pas classes.
Cause: Absence de roles explicites.
Consequence: Acces non controle.
Solution: Enum de roles avec libelles.
Pourquoi: Simplifier le filtrage UI et la securite.
Comment: Valeurs fixes + label.
*/
