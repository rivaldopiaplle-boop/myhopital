package com.rivaldo.hopital.domain;

/**
 * @Fichier EtatDossier.java
 * @Objectif Lister les etats possibles d'un dossier medical.
 * @Couche Domain
 */
public enum EtatDossier {
    // Etape 1 : dossier ouvert et actif
    OUVERT,
    // Etape 2 : dossier ferme
    FERME,
    // Etape 3 : dossier archive
    ARCHIVE
}
