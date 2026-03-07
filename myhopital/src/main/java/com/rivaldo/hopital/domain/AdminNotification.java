package com.rivaldo.hopital.domain;

import java.time.LocalDateTime;

/**
 * @Fichier AdminNotification.java
 * @Objectif Notifier le directeur principal d'une action sensible.
 * @Couche Domain
 */
public class AdminNotification {
    // Identifiant unique de la notification
    private final String id;
    // Texte a afficher a l'admin
    private final String message;
    // Date et heure de creation
    private final LocalDateTime createdAt;

    /**
     * @Objectif Construire une notification admin.
     * @param id identifiant
     * @param message message
     * @param createdAt date
     */
    public AdminNotification(String id, String message, LocalDateTime createdAt) {
        // Etape 1: stocker l'id
        this.id = id;
        // Etape 2: stocker le message
        this.message = message;
        // Etape 3: stocker la date de creation
        this.createdAt = createdAt;
    }

    /**
     * @Objectif Lire l'identifiant.
     * @return identifiant
     */
    public String getId() {
        // Retourner l'id
        return id;
    }

    /**
     * @Objectif Lire le message.
     * @return message
     */
    public String getMessage() {
        // Retourner le message
        return message;
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
Probleme: Le directeur principal n'etait pas informe des actions sensibles.
Cause: Aucune file de notifications.
Consequence: Validation tardive et manque de controle.
Solution: Creer une notification en memoire.
Pourquoi: Informer vite le responsable.
Comment: Message + date + id.
*/
