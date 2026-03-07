package com.rivaldo.hopital.ui;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 * @Fichier UiCrudSupport.java
 * @Objectif Factoriser la creation des barres de boutons CRUD.
 * @Couche UI
 */
public interface UiCrudSupport {
    /**
     * @Objectif Construire une barre de boutons.
     * @param buttons boutons
     * @return boite horizontale
     */
    default HBox buildActionBar(Button... buttons) {
        // Etape 1 : construire une barre horizontale avec un espacement fixe
        return new HBox(10, buttons);
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Chaque ecran recreait ses boutons.
Cause: Pas de standard commun.
Consequence: Incoherences de layout.
Solution: Interface avec methode par defaut.
Pourquoi: Uniformiser les barres d'actions.
Comment: HBox avec espacement fixe.
*/
