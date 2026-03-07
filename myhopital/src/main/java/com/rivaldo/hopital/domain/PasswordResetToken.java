package com.rivaldo.hopital.domain;

import java.time.LocalDateTime;

/**
 * @Fichier PasswordResetToken.java
 * @Objectif Stocker un code de reinitialisation mot de passe.
 * @Couche Domain
 */
public class PasswordResetToken {
    // Email concerne par la reinitialisation
    private final String email;
    // Code de reinitialisation
    private final String code;
    // Date et heure de creation du code
    private final LocalDateTime createdAt;

    /**
     * @Objectif Construire un token de reinitialisation.
     * @param email email
     * @param code code
     * @param createdAt date
     */
    public PasswordResetToken(String email, String code, LocalDateTime createdAt) {
        // Etape 1: stocker l'email
        this.email = email;
        // Etape 2: stocker le code
        this.code = code;
        // Etape 3: stocker la date de creation
        this.createdAt = createdAt;
    }

    /**
     * @Objectif Lire l'email.
     * @return email
     */
    public String getEmail() {
        // Retourner l'email
        return email;
    }

    /**
     * @Objectif Lire le code.
     * @return code
     */
    public String getCode() {
        // Retourner le code
        return code;
    }

    /**
     * @Objectif Lire la date de creation.
     * @return date
     */
    public LocalDateTime getCreatedAt() {
        // Retourner la date de creation
        return createdAt;
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Reinitialisation sans code securise.
Cause: Aucun token temporaire.
Consequence: Risque de changements non autorises.
Solution: Token aleatoire avec date.
Pourquoi: Ajouter une verification simple.
Comment: Stocker email + code.
*/
