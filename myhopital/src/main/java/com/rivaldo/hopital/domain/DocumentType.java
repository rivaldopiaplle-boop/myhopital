package com.rivaldo.hopital.domain;

/**
 * @Fichier DocumentType.java
 * @Objectif Lister les types de documents medicaux.
 * @Couche Domain
 */
public enum DocumentType {
    // Etape 1 : document d'examen medical
    EXAMEN,
    // Etape 2 : document de prescription
    ORDONNANCE,
    // Etape 3 : document de rapport
    RAPPORT,
    // Etape 4 : piece d'identite
    IDENTITE
}
