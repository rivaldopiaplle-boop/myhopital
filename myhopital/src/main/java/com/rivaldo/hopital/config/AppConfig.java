package com.rivaldo.hopital.config;

/**
 * @Fichier AppConfig.java
 * @Objectif Regrouper la configuration de l'application.
 * @Couche Config
 */
public class AppConfig {
    // Configuration base de donnees
    private final DatabaseConfig databaseConfig;

    /**
     * @Objectif Construire la config applicative.
     * @param databaseConfig config base de donnees
     */
    public AppConfig(DatabaseConfig databaseConfig) {
        // Etape 1: On garde la config base de donnees
        // Cette config sera reutilisee par d'autres classes
        this.databaseConfig = databaseConfig;
    }

    public DatabaseConfig getDatabaseConfig() {
        // Etape 1: Retourner la config de base
        return databaseConfig;
    }

    /**
     * @Objectif Charger une configuration par defaut basee sur des variables d'environnement.
     * @return config applicative
     */
    public static AppConfig loadDefault() {
        // Etape 1: Lire les variables systeme si elles existent
        // Sinon, on utilise des valeurs par defaut
        String url = System.getProperty("DB_URL", "jdbc:sqlite:myhopital.db");
        String user = System.getProperty("DB_USER", "");
        String password = System.getProperty("DB_PASSWORD", "");
        String driver = System.getProperty("DB_DRIVER", "org.sqlite.JDBC");
        // Etape 2: Construire la config avec ces valeurs
        return new AppConfig(new DatabaseConfig(url, user, password, driver));
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Aucun point central pour les parametres globaux.
Cause: Les valeurs etaient codees en dur.
Consequence: Risque d'erreurs en production.
Solution: Une classe AppConfig qui charge des valeurs par defaut.
Pourquoi: Faciliter le passage vers MySQL.
Comment: Lire les variables systeme puis construire la config.
*/
