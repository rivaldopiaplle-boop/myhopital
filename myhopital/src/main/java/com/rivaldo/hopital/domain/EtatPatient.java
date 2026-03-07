package com.rivaldo.hopital.domain;

/**
 * @Fichier EtatPatient.java
 * @Objectif Lister les etats possibles d'un patient.
 * @Couche Domain
 */
public enum EtatPatient {
    // Etape 1 : patient actif
    ACTIF,
    // Etape 2 : patient inactif
    INACTIF,
    // Etape 3 : patient decede
    DECEDE
}
