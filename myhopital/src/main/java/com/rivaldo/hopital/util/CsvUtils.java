package com.rivaldo.hopital.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @Fichier CsvUtils.java
 * @Objectif Lire des fichiers CSV simples depuis les ressources.
 * @Couche Util
 */
public final class CsvUtils {
    private CsvUtils() {
    // Constructeur prive: on empeche l'instanciation
    }

    /**
     * @Objectif Charger les lignes d'un CSV depuis les ressources.
     * @param resourcePath chemin ressource
     * @return lignes
     */
    public static List<String> readResourceLines(String resourcePath) {
        // Etape 1: Creer une liste vide
        List<String> lines = new ArrayList<>();
        // Etape 2: Ouvrir le fichier ressource
        try (InputStream stream = CsvUtils.class.getResourceAsStream(resourcePath)) {
            if (stream == null) {
                // Etape 3: Si le fichier n'existe pas, on retourne une liste vide
                return lines;
            }
            // Etape 4: Lire le fichier ligne par ligne
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Etape 5: Nettoyer les espaces
                    String trimmed = line.trim();
                    // Etape 6: Ignorer les lignes vides ou commentaires
                    if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {
                        // Etape 7: Ajouter la ligne propre
                        lines.add(trimmed);
                    }
                }
            }
        } catch (IOException ignored) {
            // Etape 8: En cas d'erreur, on retourne ce qu'on a
            return lines;
        }
        // Etape 9: Retourner la liste finale
        return lines;
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Les donnees pre-remplies etaient codees en dur.
Cause: Aucun lecteur de CSV.
Consequence: Maintenance difficile.
Solution: Utilitaire de lecture CSV depuis resources.
Pourquoi: Simplifier l'ajout de donnees.
Comment: Lire lignes non vides et ignorer les commentaires.
*/
