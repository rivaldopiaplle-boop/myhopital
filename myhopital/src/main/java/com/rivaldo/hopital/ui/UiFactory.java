package com.rivaldo.hopital.ui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * @Fichier UiFactory.java
 * @Objectif Creer des petits morceaux d'UI reutilisables.
 * @Couche UI
 */
public final class UiFactory {
    /**
     * @Objectif Bloquer l'instanciation de la classe utilitaire.
     */
    private UiFactory() {
    // Etape 1 : empecher l'instanciation de la classe utilitaire
    }

    /**
     * @Objectif Construire une ligne avec un label et un champ.
     * @param label texte du label
     * @param control champ ou controle
     * @return ligne horizontale
     */
    public static HBox labeledLine(String label, Node control) {
        // Etape 1 : creer le label a gauche
        Label fieldLabel = new Label(label);
        // Etape 2 : autoriser le retour a la ligne du label
        fieldLabel.setWrapText(true);
        // Etape 3 : appliquer la classe CSS du label
        fieldLabel.getStyleClass().add("form-label");
        // Etape 4 : construire la ligne horizontale label + champ
        HBox line = new HBox(10, fieldLabel, control);
        // Etape 5 : laisser le champ grandir si l'espace existe
        HBox.setHgrow(control, Priority.ALWAYS);
        // Etape 6 : ajouter un padding vertical leger
        line.setPadding(new Insets(2, 0, 2, 0));
        // Etape 7 : retourner la ligne
        return line;
    }
}
