package com.rivaldo.hopital.config;

/**
 * @Fichier DatabaseConfig.java
 * @Objectif Centraliser les parametres de connexion JDBC.
 * @Couche Config
 */
public class DatabaseConfig {
    // URL JDBC (adresse de la base)
    private final String url;
    // Nom d'utilisateur
    private final String user;
    // Mot de passe
    private final String password;
    // Nom du driver JDBC
    private final String driver;

    /**
     * @Objectif Construire la config base de donnees.
     * @param url url JDBC
     * @param user utilisateur
     * @param password mot de passe
     * @param driver driver JDBC
     */
    public DatabaseConfig(String url, String user, String password, String driver) {
        // Etape 1: Stocker l'URL de connexion
        this.url = url;
        // Etape 2: Stocker l'utilisateur
        this.user = user;
        // Etape 3: Stocker le mot de passe
        this.password = password;
        // Etape 4: Stocker le nom du driver
        this.driver = driver;
    }

    /**
     * @Objectif Lire l'URL JDBC.
     * @return url
     */
    public String getUrl() {
        // Etape 1: Retourner l'URL
        return url;
    }

    /**
     * @Objectif Lire l'utilisateur.
     * @return user
     */
    public String getUser() {
        // Etape 1: Retourner l'utilisateur
        return user;
    }

    /**
     * @Objectif Lire le mot de passe.
     * @return mot de passe
     */
    public String getPassword() {
        // Etape 1: Retourner le mot de passe
        return password;
    }

    /**
     * @Objectif Lire le driver JDBC.
     * @return driver
     */
    public String getDriver() {
        // Etape 1: Retourner le driver
        return driver;
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Les parametres MySQL etaient disperses.
Cause: Absence de classe de configuration dediee.
Consequence: Difficultes pour changer d'environnement.
Solution: Une classe qui regroupe URL, user, mot de passe, driver.
Pourquoi: Centraliser et preparer la migration vers la BD.
Comment: Injecter cette config dans le contexte applicatif.
*/
