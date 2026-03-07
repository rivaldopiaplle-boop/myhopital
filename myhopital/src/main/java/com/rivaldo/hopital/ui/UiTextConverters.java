package com.rivaldo.hopital.ui;

import com.rivaldo.hopital.domain.Medecin;
import com.rivaldo.hopital.domain.Patient;
import javafx.util.StringConverter;

/**
 * @Fichier UiTextConverters.java
 * @Objectif Convertisseurs d'affichage pour les ComboBox.
 * @Couche UI
 */
public final class UiTextConverters {
    /**
     * @Objectif Bloquer l'instanciation de la classe utilitaire.
     */
    private UiTextConverters() {
        // Etape 1 : empecher l'instanciation de la classe utilitaire
    }

    /**
     * @Objectif Convertisseur pour medecins.
     * @return convertisseur
     */
    public static StringConverter<Medecin> medecinConverter() {
        // Etape 1 : creer un convertisseur pour les medecins
        return new StringConverter<>() {
            @Override
            public String toString(Medecin medecin) {
                // Etape 2 : verifier si la valeur est vide
                if (medecin == null) {
                    // Etape 3 : retourner une chaine vide
                    return "";
                }
                // Etape 4 : construire l'affichage lisible nom + prenom
                return medecin.getNom() + " " + medecin.getPrenom();
            }

            @Override
            public Medecin fromString(String string) {
                // Etape 5 : conversion inverse non utilisee
                return null;
            }
        };
    }

    /**
     * @Objectif Convertisseur pour patients.
     * @return convertisseur
     */
    public static StringConverter<Patient> patientConverter() {
        // Etape 1 : creer un convertisseur pour les patients
        return new StringConverter<>() {
            @Override
            public String toString(Patient patient) {
                // Etape 2 : verifier si la valeur est vide
                if (patient == null) {
                    // Etape 3 : retourner une chaine vide
                    return "";
                }
                // Etape 4 : construire l'affichage lisible nom + prenom
                return patient.getNom() + " " + patient.getPrenom();
            }

            @Override
            public Patient fromString(String string) {
                // Etape 5 : conversion inverse non utilisee
                return null;
            }
        };
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Les ComboBox affichaient des objets bruts.
Cause: Aucun convertisseur texte.
Consequence: Interface difficile a lire.
Solution: Convertisseurs qui affichent nom et prenom.
Pourquoi: Rendre la selection claire.
Comment: StringConverter personnalise.
*/
