package com.rivaldo.hopital.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Fichier DateTimeUtils.java
 * @Objectif Utilitaires pour generer des creneaux date/heure.
 * @Couche Util
 */
public final class DateTimeUtils {
    private static final LocalTime[] DEFAULT_TIMES = new LocalTime[]{
            LocalTime.of(9, 0),
            LocalTime.of(10, 0),
            LocalTime.of(11, 0),
            LocalTime.of(14, 0),
            LocalTime.of(15, 0)
    };

    private DateTimeUtils() {
    // Constructeur prive: on empeche l'instanciation
    }

    /**
     * @Objectif Construire des creneaux sur N jours.
     * @param startDate date de depart
     * @param days nombre de jours
     * @return liste des creneaux
     */
    public static List<LocalDateTime> buildSlots(LocalDate startDate, int days) {
        // Etape 1: Creer la liste de creneaux
        List<LocalDateTime> slots = new ArrayList<>();
        // Etape 2: Boucler sur les jours
        for (int i = 0; i < days; i++) {
            // Etape 3: Calculer la date du jour courant
            LocalDate day = startDate.plusDays(i);
            // Etape 4: Pour chaque heure par defaut
            for (LocalTime time : DEFAULT_TIMES) {
                // Etape 5: Ajouter le creneau (jour + heure)
                slots.add(LocalDateTime.of(day, time));
            }
        }
        // Etape 6: Retourner la liste des creneaux
        return slots;
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Les creneaux etaient dupliques dans l'UI.
Cause: Pas d'utilitaire commun.
Consequence: Incoherences entre ecrans.
Solution: Centraliser la generation de creneaux ici.
Pourquoi: Garantir des listes identiques partout.
Comment: Boucle sur jours + heures fixes.
*/
