package com.rivaldo.hopital.ui;

import com.rivaldo.hopital.domain.AdminNotification;
import com.rivaldo.hopital.repository.DataStore;
import com.rivaldo.hopital.service.AuthService;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.geometry.Orientation;

/**
 * @Fichier AdminApprovalView.java
 * @Objectif Ecran d'approbation des directeurs.
 * @Couche UI
 */
public class AdminApprovalView implements UiCrudSupport {
    private final DataStore dataStore;
    private final AuthService authService;

    /**
     * @Objectif Construire l'ecran d'approbation.
     * @param dataStore stockage en memoire
     * @param authService service auth
     */
    public AdminApprovalView(DataStore dataStore, AuthService authService) {
        // Etape 1 : memoriser le stockage
        this.dataStore = dataStore;
        // Etape 2 : memoriser le service d'authentification
        this.authService = authService;
    }

    /**
     * @Objectif Construire le layout JavaFX.
     * @return racine graphique
     */
    public Parent createView() {
        // Etape 1 : creer la liste des notifications
        ListView<AdminNotification> notifications = new ListView<>(dataStore.getNotifications());
        // Etape 2 : definir le rendu des lignes
        notifications.setCellFactory(list -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(AdminNotification item, boolean empty) {
                super.updateItem(item, empty);
                // Etape 3 : afficher un message simple
                setText(empty || item == null ? null : item.getMessage());
            }
        });

        // Etape 4 : creer le champ de code
        TextField codeField = new TextField();
        // Etape 5 : creer le bouton d'approbation
        Button approveButton = new Button("Approuver");
        // Etape 6 : appliquer le style primaire
        approveButton.getStyleClass().add("primary");

        // Etape 7 : definir l'action d'approbation
        approveButton.setOnAction(event -> {
            // Etape 8 : verifier le code d'approbation
            boolean ok = authService.approveDirector(codeField.getText());
            // Etape 9 : afficher le resultat
            if (ok) {
                showAlert("OK", "Directeur approuve.");
                codeField.clear();
            } else {
                showAlert("Erreur", "Code invalide.");
            }
        });

        // Etape 10 : construire le formulaire d'action
        VBox formBox = new VBox(10,
            UiFactory.labeledLine("Code", codeField),
            buildActionBar(approveButton)
        );
        // Etape 11 : ajouter du padding au formulaire
        formBox.setPadding(new Insets(10));
        // Etape 12 : appliquer le style carte
        formBox.getStyleClass().add("card");

        // Etape 12 bis : creer un SplitPane vertical pour le redimensionnement
        SplitPane split = new SplitPane();
        // Etape 12 ter : utiliser une orientation verticale
        split.setOrientation(Orientation.VERTICAL);
        // Etape 12 quater : definir la position initiale du separateur
        split.setDividerPositions(0.7);
        // Etape 12 quinquies : ajouter la liste et le formulaire
        split.getItems().addAll(notifications, formBox);

        // Etape 13 : creer un scroll pour voir tout le contenu
        ScrollPane contentScroll = new ScrollPane(split);
        // Etape 14 : activer l'ajustement en largeur
        contentScroll.setFitToWidth(true);

        // Etape 15 : retourner le contenu
        return contentScroll;
    }

    /**
     * @Objectif Afficher une alerte simple.
     * @param title titre de la fenetre
     * @param message message a afficher
     */
    private void showAlert(String title, String message) {
        // Etape 1 : creer une alerte simple
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        // Etape 2 : definir le titre
        alert.setTitle(title);
        // Etape 3 : retirer l'en-tete
        alert.setHeaderText(null);
        // Etape 4 : definir le message
        alert.setContentText(message);
        // Etape 5 : afficher l'alerte et attendre
        alert.showAndWait();
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Les codes d'approbation n'etaient pas visibles.
Cause: Aucun ecran d'inbox.
Consequence: Attente pour activer un directeur.
Solution: Liste de notifications + champ code.
Pourquoi: Donner le controle au directeur principal.
Comment: ListView + validation du code.
*/
