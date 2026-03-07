package com.rivaldo.hopital.app;

import com.rivaldo.hopital.config.AppBootstrap;
import com.rivaldo.hopital.config.AppConfig;
import com.rivaldo.hopital.domain.Utilisateur;
import com.rivaldo.hopital.repository.DataStore;
import com.rivaldo.hopital.repository.DatabaseManager;
import com.rivaldo.hopital.repository.DatabaseRepository;
import com.rivaldo.hopital.service.AuthService;
import com.rivaldo.hopital.service.ConsultationService;
import com.rivaldo.hopital.service.DossierMedicalService;
import com.rivaldo.hopital.service.DocumentMedicalService;
import com.rivaldo.hopital.service.MedecinService;
import com.rivaldo.hopital.service.PatientService;
import com.rivaldo.hopital.service.RendezVousService;
import com.rivaldo.hopital.service.ValidationService;
import com.rivaldo.hopital.ui.DashboardView;
import com.rivaldo.hopital.ui.LoginView;
import com.rivaldo.hopital.ui.RegisterView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @Fichier HospitalApplication.java
 * @Objectif Lancer l'application JavaFX de l'hopital.
 * @Couche App
 */
public class HospitalApplication extends Application {
    private Stage stage;
    private AppConfig appConfig;
    private DataStore dataStore;
    private DatabaseManager databaseManager;
    private DatabaseRepository databaseRepository;
    private ValidationService validationService;
    private AuthService authService;
    private PatientService patientService;
    private MedecinService medecinService;
    private DossierMedicalService dossierMedicalService;
    private RendezVousService rendezVousService;
    private ConsultationService consultationService;
    private DocumentMedicalService documentMedicalService;

    @Override
    /**
     * @Objectif Demarrer l'application JavaFX.
     * @param primaryStage fenetre principale
     */
    public void start(Stage primaryStage) {
        // Etape 1: On garde une reference vers la fenetre principale
        // La Stage est la fenetre principale de JavaFX
        this.stage = primaryStage;
        // Etape 2: On initialise tous les services
        // Cela prepare toutes les regles metier et le stockage
        initServices();
        // Etape 3: On affiche l'ecran de connexion
        // L'utilisateur voit directement la page Login
        showLogin();
    }

    /**
     * @Objectif Initialiser les services et les donnees.
     */
    private void initServices() {
        // Etape 1: Chargement de la configuration
        // Utile pour preparer la base de donnees plus tard
        appConfig = AppConfig.loadDefault();
        // Etape 2: Creation du stockage en memoire
        // DataStore contient toutes les listes en RAM
        dataStore = new DataStore();
        // Etape 2 bis: Initialiser la base de donnees
        databaseManager = new DatabaseManager(appConfig.getDatabaseConfig());
        databaseManager.connect();
        databaseManager.createTables();
        databaseRepository = new DatabaseRepository(databaseManager);
        // Etape 3: Service de validation
        // Utilise pour verifier email, champs obligatoires, etc.
        validationService = new ValidationService();
        // Etape 4: Services metier
        // Chaque service gere un domaine precis
        dossierMedicalService = new DossierMedicalService(dataStore, databaseRepository);
        patientService = new PatientService(dataStore, dossierMedicalService, databaseRepository);
        medecinService = new MedecinService(dataStore, databaseRepository);
        rendezVousService = new RendezVousService(dataStore, databaseRepository);
        consultationService = new ConsultationService(dataStore, databaseRepository);
        documentMedicalService = new DocumentMedicalService(dataStore, databaseRepository);
        authService = new AuthService(dataStore, validationService, databaseRepository);

        // Etape 4 bis: Charger la base vers la memoire
        databaseRepository.loadAll(dataStore);

        // Etape 5: Charger les donnees de demo si la base est vide
        if (!databaseRepository.hasAnyUsers()) {
            AppBootstrap bootstrap = new AppBootstrap(dataStore, validationService, authService, medecinService,
                patientService, rendezVousService, consultationService, documentMedicalService);
            bootstrap.load();
        }
    }

    /**
     * @Objectif Afficher l'ecran de connexion.
     */
    private void showLogin() {
        // Etape 1: Creation de l'ecran de connexion
        LoginView view = new LoginView(authService, this::showDashboard, this::showRegister, this::showResetPassword);
        // Etape 2: Creation de la scene JavaFX
        // Scene = ce qui est affiche dans la fenetre
        Scene scene = new Scene(view.createView(), 1100, 720);
        // Etape 3: Chargement des styles CSS
        applyStyles(scene);
        // Etape 4: Titre de la fenetre
        stage.setTitle("MyHopital - Connexion");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @Objectif Afficher l'ecran d'inscription.
     */
    private void showRegister() {
        // Etape 1: Creation de l'ecran d'inscription
        RegisterView view = new RegisterView(authService, validationService, this::showLogin);
        // Etape 2: Creation de la scene
        Scene scene = new Scene(view.createView(), 1100, 720);
        // Etape 3: Styles CSS
        applyStyles(scene);
        // Etape 4: Titre
        stage.setTitle("MyHopital - Inscription");
        stage.setScene(scene);
    }

    /**
     * @Objectif Afficher l'ecran de reinitialisation.
     */
    private void showResetPassword() {
        // Etape 1: Creation de l'ecran de reinitialisation
        com.rivaldo.hopital.ui.ResetPasswordView view =
                new com.rivaldo.hopital.ui.ResetPasswordView(authService, this::showLogin);
        // Etape 2: Creation de la scene
        Scene scene = new Scene(view.createView(), 900, 600);
        // Etape 3: Styles CSS
        applyStyles(scene);
        // Etape 4: Titre
        stage.setTitle("MyHopital - Reinitialisation");
        stage.setScene(scene);
    }

    /**
     * @Objectif Afficher le tableau de bord.
     * @param utilisateur utilisateur connecte
     */
    private void showDashboard(Utilisateur utilisateur) {
        // Etape 1: Creation de l'ecran principal apres connexion
        DashboardView view = new DashboardView(
                utilisateur,
                dataStore,
                patientService,
                medecinService,
                rendezVousService,
                consultationService,
                documentMedicalService,
                dossierMedicalService,
            authService,
                this::showLogin
        );
        // Etape 2: Creation de la scene
        Scene scene = new Scene(view.createView(), 1280, 760);
        // Etape 3: Styles CSS
        applyStyles(scene);
        // Etape 4: Titre
        stage.setTitle("MyHopital - Tableau de bord");
        stage.setScene(scene);
    }

    /**
     * @Objectif Charger les styles CSS.
     * @param scene scene JavaFX
     */
    private void applyStyles(Scene scene) {
        // Etape 1: Ajout du fichier CSS principal
        scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
    }

    @Override
    /**
     * @Objectif Nettoyer les ressources a la fermeture.
     */
    public void stop() {
        // Etape 1 : fermer la connexion base si elle existe
        if (databaseManager != null) {
            databaseManager.close();
        }
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: L'application n'exposait pas la configuration globale.
Cause: Absence de chargement centralise de la config.
Consequence: Difficile de preparer l'usage MySQL.
Solution: Charger AppConfig au demarrage.
Pourquoi: Centraliser les parametres et preparer l'evolution.
Comment: AppConfig.loadDefault() au lancement.
*/
