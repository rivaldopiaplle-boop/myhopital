package com.rivaldo.hopital.domain;

import java.time.LocalDateTime;

/**
 * @Fichier DirectorApproval.java
 * @Objectif Stocker un code d'approbation pour un directeur.
 * @Couche Domain
 */
public class DirectorApproval {
    // Identifiant de l'utilisateur a approuver
    private final String userId;
    // Code d'approbation
    private final String code;
    // Date et heure de creation
    private final LocalDateTime createdAt;

    /**
     * @Objectif Construire une demande d'approbation.
     * @param userId utilisateur
     * @param code code
     * @param createdAt date
     */
    public DirectorApproval(String userId, String code, LocalDateTime createdAt) {
        // Etape 1: stocker l'id utilisateur
        this.userId = userId;
        // Etape 2: stocker le code
        this.code = code;
        // Etape 3: stocker la date de creation
        this.createdAt = createdAt;
    }

    /**
     * @Objectif Lire l'id utilisateur.
     * @return id utilisateur
     */
    public String getUserId() {
        // Retourner l'id utilisateur
        return userId;
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
Probleme: Les nouveaux directeurs n'etaient pas valides.
Cause: Aucune approbation par le principal.
Consequence: Risque de compte non controle.
Solution: Code d'approbation par le directeur principal.
Pourquoi: Garder un controle fort.
Comment: Stocker userId + code.
*/
