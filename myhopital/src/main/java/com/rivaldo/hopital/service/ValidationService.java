package com.rivaldo.hopital.service;

import java.util.List;
import java.util.Random;

/**
 * @Fichier ValidationService.java
 * @Objectif Fournir des validations simples pour l'UI et les services.
 * @Couche Service
 */
public class ValidationService {
    private static final List<String> WELCOME_MESSAGES = List.of(
            "Bienvenue, votre compte est actif.",
            "Inscription confirmee, bon travail.",
            "Compte cree, vous pouvez vous connecter.",
            "Bienvenue a l'hopital, acces autorise."
    );

    private final Random random = new Random();

    /**
     * @Objectif Verifier qu'une valeur n'est pas vide.
     * @param value valeur a verifier
     * @return true si valide
     */
    public boolean isRequiredValid(String value) {
        // Etape 1: Verifier que la valeur existe
        // Etape 2: Verifier qu'elle n'est pas vide
        return value != null && !value.trim().isEmpty();
    }

    /**
     * @Objectif Valider un email de base.
     * @param email email a verifier
     * @return true si valide
     */
    public boolean isEmailValid(String email) {
        // Etape 1: Verifier que l'email est present
        // Etape 2: Verifier qu'il contient @
        return isRequiredValid(email) && email.contains("@");
    }

    /**
     * @Objectif Generer un message d'accueil aleatoire.
     * @return message
     */
    public String randomWelcomeMessage() {
        // Etape 1: Tirer un index aleatoire dans la liste
        int index = random.nextInt(WELCOME_MESSAGES.size());
        // Etape 2: Retourner le message a cet index
        return WELCOME_MESSAGES.get(index);
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Les validations etait dispersees.
Cause: Pas de service commun.
Consequence: Regles dupliquees et incoherentes.
Solution: Un service simple reutilisable.
Pourquoi: Centraliser et stabiliser les controles.
Comment: Methodes utilitaires partagees.
*/
