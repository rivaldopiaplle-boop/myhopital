package com.rivaldo.hopital.util;

import java.util.Random;

/**
 * @Fichier RandomCodeUtils.java
 * @Objectif Generer des codes aleatoires simples.
 * @Couche Util
 */
public final class RandomCodeUtils {
    private static final Random RANDOM = new Random();

    private RandomCodeUtils() {
    // Constructeur prive: on empeche l'instanciation
    }

    /**
     * @Objectif Generer un code numerique.
     * @param length longueur
     * @return code
     */
    public static String numericCode(int length) {
        // Etape 1: Creer un builder pour construire le code
        StringBuilder builder = new StringBuilder();
        // Etape 2: Ajouter un chiffre a chaque tour
        for (int i = 0; i < length; i++) {
            // Etape 3: Choisir un chiffre entre 0 et 9
            builder.append(RANDOM.nextInt(10));
        }
        // Etape 4: Retourner le code final
        return builder.toString();
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Besoin d'un code simple pour approbation et reset.
Cause: Aucun utilitaire commun.
Consequence: Duplication possible.
Solution: Generateur de code numerique.
Pourquoi: Standardiser les codes temporaires.
Comment: Random + boucle.
*/
