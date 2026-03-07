package com.rivaldo.hopital.repository;

import com.rivaldo.hopital.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Fichier DatabaseManager.java
 * @Objectif Gerer la connexion SQLite et initialiser le schema.
 * @Couche Repository
 */
public class DatabaseManager {
    private final DatabaseConfig databaseConfig;
    private Connection connection;

    /**
     * @Objectif Construire le gestionnaire de base.
     * @param databaseConfig configuration JDBC
     */
    public DatabaseManager(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    /**
     * @Objectif Ouvrir la connexion a la base.
     */
    public void connect() {
        try {
            // Etape 1 : charger le driver JDBC
            Class.forName(databaseConfig.getDriver());
            // Etape 2 : ouvrir la connexion
            if (databaseConfig.getUser() == null || databaseConfig.getUser().isBlank()) {
                connection = DriverManager.getConnection(databaseConfig.getUrl());
            } else {
                connection = DriverManager.getConnection(
                    databaseConfig.getUrl(),
                    databaseConfig.getUser(),
                    databaseConfig.getPassword());
            }
        } catch (ClassNotFoundException | SQLException ex) {
            throw new IllegalStateException("Connexion base de donnees impossible.", ex);
        }
    }

    /**
     * @Objectif Fermer la connexion.
     */
    public void close() {
        if (connection == null) {
            return;
        }
        try {
            // Etape 1 : fermer la connexion active
            connection.close();
        } catch (SQLException ex) {
            throw new IllegalStateException("Fermeture base de donnees impossible.", ex);
        }
    }

    /**
     * @Objectif Acceder a la connexion JDBC.
     * @return connexion active
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * @Objectif Creer les tables si elles n'existent pas.
     */
    public void createTables() {
        // Etape 1 : activer les cles etrangeres
        executeStatement("PRAGMA foreign_keys = ON");

        // Etape 2 : table utilisateurs
        executeStatement("CREATE TABLE IF NOT EXISTS utilisateurs ("
            + "id TEXT PRIMARY KEY,"
            + "nom TEXT NOT NULL,"
            + "prenom TEXT NOT NULL,"
            + "email TEXT UNIQUE NOT NULL,"
            + "mot_de_passe TEXT NOT NULL,"
            + "role TEXT NOT NULL,"
            + "date_naissance TEXT,"
            + "telephone TEXT,"
            + "sexe TEXT,"
            + "adresse_rue TEXT,"
            + "adresse_code_postal TEXT,"
            + "adresse_ville TEXT,"
            + "adresse_pays TEXT,"
            + "moyen_contact TEXT,"
            + "primary_director INTEGER DEFAULT 0,"
            + "approved INTEGER DEFAULT 1"
            + ")");

        // Etape 3 : table medecins
        executeStatement("CREATE TABLE IF NOT EXISTS medecins ("
            + "id TEXT PRIMARY KEY,"
            + "matricule TEXT,"
            + "nom TEXT NOT NULL,"
            + "prenom TEXT NOT NULL,"
            + "date_naissance TEXT,"
            + "sexe TEXT,"
            + "telephone TEXT,"
            + "email TEXT,"
            + "adresse_rue TEXT,"
            + "adresse_code_postal TEXT,"
            + "adresse_ville TEXT,"
            + "adresse_pays TEXT,"
            + "moyen_contact TEXT,"
            + "specialite TEXT"
            + ")");

        // Etape 4 : table patients
        executeStatement("CREATE TABLE IF NOT EXISTS patients ("
            + "id TEXT PRIMARY KEY,"
            + "dossier_number TEXT,"
            + "nom TEXT NOT NULL,"
            + "prenom TEXT NOT NULL,"
            + "date_naissance TEXT,"
            + "sexe TEXT,"
            + "telephone TEXT,"
            + "email TEXT,"
            + "adresse_rue TEXT,"
            + "adresse_code_postal TEXT,"
            + "adresse_ville TEXT,"
            + "adresse_pays TEXT,"
            + "moyen_contact TEXT,"
            + "etat TEXT"
            + ")");

        // Etape 5 : table dossiers
        executeStatement("CREATE TABLE IF NOT EXISTS dossiers ("
            + "id TEXT PRIMARY KEY,"
            + "patient_id TEXT NOT NULL,"
            + "date_ouverture TEXT,"
            + "etat TEXT,"
            + "FOREIGN KEY(patient_id) REFERENCES patients(id)"
            + ")");

        // Etape 6 : table rendezvous
        executeStatement("CREATE TABLE IF NOT EXISTS rendezvous ("
            + "id TEXT PRIMARY KEY,"
            + "patient_id TEXT NOT NULL,"
            + "medecin_id TEXT NOT NULL,"
            + "date_heure TEXT NOT NULL,"
            + "FOREIGN KEY(patient_id) REFERENCES patients(id),"
            + "FOREIGN KEY(medecin_id) REFERENCES medecins(id)"
            + ")");

        // Etape 7 : table consultations
        executeStatement("CREATE TABLE IF NOT EXISTS consultations ("
            + "id TEXT PRIMARY KEY,"
            + "patient_id TEXT NOT NULL,"
            + "medecin_id TEXT NOT NULL,"
            + "dossier_id TEXT NOT NULL,"
            + "date TEXT,"
            + "diagnostic TEXT,"
            + "prescription TEXT,"
            + "FOREIGN KEY(patient_id) REFERENCES patients(id),"
            + "FOREIGN KEY(medecin_id) REFERENCES medecins(id),"
            + "FOREIGN KEY(dossier_id) REFERENCES dossiers(id)"
            + ")");

        // Etape 8 : table documents
        executeStatement("CREATE TABLE IF NOT EXISTS documents ("
            + "id TEXT PRIMARY KEY,"
            + "patient_id TEXT NOT NULL,"
            + "medecin_id TEXT NOT NULL,"
            + "dossier_id TEXT NOT NULL,"
            + "nom TEXT,"
            + "type TEXT,"
            + "date_ajout TEXT,"
            + "FOREIGN KEY(patient_id) REFERENCES patients(id),"
            + "FOREIGN KEY(medecin_id) REFERENCES medecins(id),"
            + "FOREIGN KEY(dossier_id) REFERENCES dossiers(id)"
            + ")");

        // Etape 9 : table approvals directeurs
        executeStatement("CREATE TABLE IF NOT EXISTS director_approvals ("
            + "user_id TEXT PRIMARY KEY,"
            + "code TEXT NOT NULL,"
            + "created_at TEXT NOT NULL,"
            + "FOREIGN KEY(user_id) REFERENCES utilisateurs(id)"
            + ")");

        // Etape 10 : table notifications admin
        executeStatement("CREATE TABLE IF NOT EXISTS admin_notifications ("
            + "id TEXT PRIMARY KEY,"
            + "message TEXT NOT NULL,"
            + "created_at TEXT NOT NULL"
            + ")");

        // Etape 11 : table reset tokens
        executeStatement("CREATE TABLE IF NOT EXISTS reset_tokens ("
            + "email TEXT NOT NULL,"
            + "code TEXT NOT NULL,"
            + "created_at TEXT NOT NULL"
            + ")");
    }

    /**
     * @Objectif Executer une requete SQL simple.
     * @param sql requete a executer
     */
    private void executeStatement(String sql) {
        try (Statement statement = connection.createStatement()) {
            // Etape 1 : executer la requete SQL
            statement.execute(sql);
        } catch (SQLException ex) {
            throw new IllegalStateException("Creation du schema impossible.", ex);
        }
    }
}
