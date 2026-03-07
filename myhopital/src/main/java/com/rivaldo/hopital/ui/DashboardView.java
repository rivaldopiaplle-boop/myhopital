package com.rivaldo.hopital.ui;

import com.rivaldo.hopital.domain.Role;
import com.rivaldo.hopital.domain.Utilisateur;
import com.rivaldo.hopital.repository.DataStore;
import com.rivaldo.hopital.service.ConsultationService;
import com.rivaldo.hopital.service.DossierMedicalService;
import com.rivaldo.hopital.service.DocumentMedicalService;
import com.rivaldo.hopital.service.MedecinService;
import com.rivaldo.hopital.service.PatientService;
import com.rivaldo.hopital.service.RendezVousService;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * @Fichier DashboardView.java
 * @Objectif Tableau de bord avec onglets selon le role.
 * @Couche UI
 */
public class DashboardView {
    private final Utilisateur utilisateur;
    private final DataStore dataStore;
    private final PatientService patientService;
    private final MedecinService medecinService;
    private final RendezVousService rendezVousService;
    private final ConsultationService consultationService;
    private final DocumentMedicalService documentMedicalService;
    private final DossierMedicalService dossierMedicalService;
    private final com.rivaldo.hopital.service.AuthService authService;
    private final Runnable onLogout;

    /**
     * @Objectif Construire le tableau de bord.
     * @param utilisateur utilisateur connecte
     * @param dataStore depot central des donnees
     * @param patientService service patient
     * @param medecinService service medecin
     * @param rendezVousService service rendez-vous
     * @param consultationService service consultation
     * @param documentMedicalService service document medical
     * @param dossierMedicalService service dossier medical
     * @param authService service d'authentification
     * @param onLogout action de deconnexion
     */
    public DashboardView(Utilisateur utilisateur, DataStore dataStore, PatientService patientService,
                         MedecinService medecinService, RendezVousService rendezVousService,
                         ConsultationService consultationService, DocumentMedicalService documentMedicalService,
                         DossierMedicalService dossierMedicalService,
                         com.rivaldo.hopital.service.AuthService authService,
                         Runnable onLogout) {
        // Etape 1 : memoriser l'utilisateur connecte
        this.utilisateur = utilisateur;
        // Etape 2 : memoriser l'acces aux donnees
        this.dataStore = dataStore;
        // Etape 3 : memoriser le service patient
        this.patientService = patientService;
        // Etape 4 : memoriser le service medecin
        this.medecinService = medecinService;
        // Etape 5 : memoriser le service rendez-vous
        this.rendezVousService = rendezVousService;
        // Etape 6 : memoriser le service consultation
        this.consultationService = consultationService;
        // Etape 7 : memoriser le service document medical
        this.documentMedicalService = documentMedicalService;
        // Etape 8 : memoriser le service dossier medical
        this.dossierMedicalService = dossierMedicalService;
        // Etape 9 : memoriser le service d'authentification
        this.authService = authService;
        // Etape 10 : memoriser l'action de deconnexion
        this.onLogout = onLogout;
    }

    /**
     * @Objectif Construire le layout JavaFX.
     * @return racine graphique
     */
    public Parent createView() {
        // Etape 1 : creer le conteneur principal
        BorderPane root = new BorderPane();

        // Etape 2 : creer le label utilisateur connecte
        Label userLabel = new Label("Connecte: " + utilisateur.getPrenom() + " " + utilisateur.getNom()
                + " - " + utilisateur.getRole().getLabel());
        // Etape 3 : creer le bouton de deconnexion
        Button logoutButton = new Button("Deconnexion");
        // Etape 4 : relier le bouton a l'action de sortie
        logoutButton.setOnAction(event -> onLogout.run());

        // Etape 5 : composer l'en-tete avec le label et le bouton
        HBox header = new HBox(12, userLabel, logoutButton);
        // Etape 6 : ajouter du padding sur l'en-tete
        header.setPadding(new Insets(10));
        // Etape 7 : ajouter une classe CSS pour styliser l'en-tete
        header.getStyleClass().add("header");
        // Etape 8 : placer l'en-tete en haut de l'ecran
        root.setTop(header);

        // Etape 9 : creer la zone d'onglets principale
        TabPane tabs = new TabPane();
        // Etape 10 : creer la vue des dossiers (toujours presente)
        DossierMedicalView dossierView = new DossierMedicalView(dataStore, dossierMedicalService);
        // Etape 11 : construire l'onglet dossiers
        Tab dossierTab = new Tab("Dossiers", dossierView.createView());

        // Etape 11 bis : creer la vue des consultations
        ConsultationView consultationView = new ConsultationView(dataStore, consultationService);
        // Etape 11 ter : construire l'onglet consultations
        Tab consultationTab = new Tab("Consultations", consultationView.createView());

        // Etape 11 quater : creer la vue des documents medicaux
        DocumentMedicalView documentMedicalView = new DocumentMedicalView(dataStore, documentMedicalService);
        // Etape 11 quinquies : construire l'onglet documents
        Tab documentTab = new Tab("Documents", documentMedicalView.createView());

        // Etape 12 : verifier si le role autorise l'onglet patients
        if (utilisateur.getRole() == Role.DIRECTEUR || utilisateur.getRole() == Role.GESTIONNAIRE) {
            // Etape 13 : creer la vue des patients
            PatientView patientView = new PatientView(dataStore, patientService, rendezVousService,
                    patient -> {
                        // Etape 14 : filtrer les dossiers selon le patient selectionne
                        dossierView.setPatientFilter(patient.getId());
                        // Etape 15 : selectionner l'onglet dossiers pour afficher le resultat
                        tabs.getSelectionModel().select(dossierTab);
                    },
                    patient -> {
                        // Etape 16 : filtrer les consultations selon le patient selectionne
                        consultationView.setPatientFilter(patient.getId());
                        // Etape 17 : selectionner l'onglet consultations
                        tabs.getSelectionModel().select(consultationTab);
                    },
                    patient -> {
                        // Etape 18 : filtrer les documents selon le patient selectionne
                        documentMedicalView.setPatientFilter(patient.getId());
                        // Etape 19 : selectionner l'onglet documents
                        tabs.getSelectionModel().select(documentTab);
                    });
            // Etape 16 : ajouter l'onglet patients
            tabs.getTabs().add(new Tab("Patients", patientView.createView()));
        }

        // Etape 17 : verifier si le role autorise l'onglet medecins
        if (utilisateur.getRole() == Role.DIRECTEUR) {
            // Etape 18 : creer la vue des medecins
            MedecinView medecinView = new MedecinView(dataStore, medecinService, rendezVousService,
                medecin -> {
                    // Etape 19 : filtrer les consultations selon le medecin selectionne
                    consultationView.setMedecinFilter(medecin.getId());
                    // Etape 20 : selectionner l'onglet consultations
                    tabs.getSelectionModel().select(consultationTab);
                },
                medecin -> {
                    // Etape 21 : filtrer les documents selon le medecin selectionne
                    documentMedicalView.setMedecinFilter(medecin.getId());
                    // Etape 22 : selectionner l'onglet documents
                    tabs.getSelectionModel().select(documentTab);
                });
            // Etape 23 : ajouter l'onglet medecins
            tabs.getTabs().add(new Tab("Medecins", medecinView.createView()));
        }

        // Etape 20 : creer la vue des rendez-vous
        RendezVousView rendezVousView = new RendezVousView(dataStore, rendezVousService);
        // Etape 21 : ajouter l'onglet rendez-vous
        tabs.getTabs().add(new Tab("Rendez-vous", rendezVousView.createView()));

        // Etape 22 : ajouter l'onglet consultations
        tabs.getTabs().add(consultationTab);

        // Etape 23 : ajouter l'onglet documents
        tabs.getTabs().add(documentTab);

        // Etape 26 : ajouter l'onglet dossiers a la fin
        tabs.getTabs().add(dossierTab);

        // Etape 27 : creer la vue de changement de mot de passe
        ChangePasswordView changePasswordView = new ChangePasswordView(authService, utilisateur);
        // Etape 28 : ajouter l'onglet mot de passe
        tabs.getTabs().add(new Tab("Mot de passe", changePasswordView.createView()));

        // Etape 29 : verifier si le directeur principal voit les approvals
        if (utilisateur.getRole() == Role.DIRECTEUR && utilisateur.isPrimaryDirector()) {
            // Etape 30 : creer la vue des validations d'admin
            AdminApprovalView approvalView = new AdminApprovalView(dataStore, authService);
            // Etape 31 : ajouter l'onglet approvals
            tabs.getTabs().add(new Tab("Approvals", approvalView.createView()));
        }

        // Etape 32 : placer les onglets au centre
        root.setCenter(tabs);
        // Etape 33 : retourner la racine graphique
        return root;
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Tous les utilisateurs voyaient les memes fonctions.
Cause: Absence de filtrage par role.
Consequence: Risque d'acces non autorise.
Solution: Afficher les onglets selon le role connecte.
Pourquoi: Respecter la separation des responsabilites.
Comment: Conditions sur Role pour construire les tabs.
*/
