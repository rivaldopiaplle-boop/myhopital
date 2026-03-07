package com.rivaldo.hopital.exception;

/**
 * @Fichier HospitalException.java
 * @Objectif Exception metier generique pour l'application hopital.
 * @Couche Exception
 */
public class HospitalException extends RuntimeException {
    /**
     * @Objectif Construire une exception avec un message.
     * @param message message d'erreur
     */
    public HospitalException(String message) {
        // Etape 1: Appeler le constructeur parent avec le message
        super(message);
    }

    /**
     * @Objectif Construire une exception avec message et cause.
     * @param message message d'erreur
     * @param cause cause racine
     */
    public HospitalException(String message, Throwable cause) {
        // Etape 1: Appeler le constructeur parent avec message + cause
        super(message, cause);
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Les erreurs metier etaient melangees aux exceptions techniques.
Cause: Pas de type commun pour signaler un echec fonctionnel.
Consequence: Difficile de gerer proprement les alertes UI.
Solution: Une exception metier de base partagee par tout le projet.
Pourquoi: Centraliser les erreurs metier et simplifier les captures.
Comment: Creer une classe RuntimeException dediee.
*/
