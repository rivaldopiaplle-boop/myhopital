package com.rivaldo.hopital.exception;

/**
 * @Fichier ValidationException.java
 * @Objectif Exception levee quand une regle de validation echoue.
 * @Couche Exception
 */
public class ValidationException extends HospitalException {
    /**
     * @Objectif Construire une exception de validation.
     * @param message message d'erreur
     */
    public ValidationException(String message) {
        // Etape 1: Appeler le constructeur parent avec le message
        super(message);
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Les validations etaient signalees avec IllegalArgumentException.
Cause: Pas d'exception metier specifique pour l'UI.
Consequence: Messages moins clairs et difficiles a filtrer.
Solution: Une exception de validation dediee.
Pourquoi: Afficher des erreurs lisibles et centraliser la gestion.
Comment: Etendre HospitalException.
*/
