package com.rivaldo.hopital.util;

import java.util.UUID;

/**
 * @Fichier IdGenerator.java
 * @Objectif Generer des identifiants uniques.
 * @Couche Util
 */
public final class IdGenerator {
    private IdGenerator() {
    // Constructeur prive: on empeche l'instanciation
    }

    /**
     * @Objectif Generer un identifiant unique.
     * @return identifiant
     */
    public static String newId() {
        // Etape 1: Generer un UUID aleatoire
        // Etape 2: Le convertir en texte
        return UUID.randomUUID().toString();
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Les identifiants etaient crees a plusieurs endroits.
Cause: Pas d'utilitaire central.
Consequence: Risque de collisions et duplication.
Solution: Une classe unique pour generer les ids.
Pourquoi: Uniformiser les identifiants metier.
Comment: Utiliser UUID.
*/
