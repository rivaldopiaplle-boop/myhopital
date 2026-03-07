package com.rivaldo.hopital;

import com.rivaldo.hopital.app.HospitalApplication;

/**
 * @Fichier Main.java
 * @Objectif Point d'entree de l'application desktop.
 * @Couche App
 */
public class Main {
    /**
     * @Objectif Lancer l'application JavaFX.
     * @param args arguments de ligne de commande
     */
    public static void main(String[] args) {
        // Etape 1: On appelle le lanceur JavaFX
        // HospitalApplication est la classe qui construit toute l'app
        // args contient les parametres de la ligne de commande
        HospitalApplication.launch(HospitalApplication.class, args);
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Aucun point d'entree clair.
Cause: Main minimal sans intention.
Consequence: Difficile pour un debutant de lancer l'app.
Solution: Un main explicite qui lance JavaFX.
Pourquoi: Simplifier l'execution.
Comment: Appel direct a HospitalApplication.launch.
*/