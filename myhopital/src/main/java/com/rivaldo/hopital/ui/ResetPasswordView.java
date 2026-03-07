package com.rivaldo.hopital.ui;

import com.rivaldo.hopital.exception.ValidationException;
import com.rivaldo.hopital.service.AuthService;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @Fichier ResetPasswordView.java
 * @Objectif Ecran de reinitialisation du mot de passe.
 * @Couche UI
 */
public class ResetPasswordView implements UiCrudSupport {
    private final AuthService authService;
    private final Runnable onBack;

    /**
     * @Objectif Construire l'ecran reset mot de passe.
     * @param authService service auth
     * @param onBack action retour
     */
    public ResetPasswordView(AuthService authService, Runnable onBack) {
        this.authService = authService;
        this.onBack = onBack;
    }

    /**
     * @Objectif Construire le layout JavaFX.
     * @return racine graphique
     */
    public Parent createView() {
        // Etape 1: Conteneur principal
        BorderPane root = new BorderPane();

        // Etape 2: Champs de saisie
        TextField emailField = new TextField();
        TextField codeField = new TextField();
        PasswordField newPasswordField = new PasswordField();
        PasswordField confirmField = new PasswordField();

        // Etape 3: Boutons d'action
        Button sendCodeButton = new Button("Envoyer code");
        sendCodeButton.getStyleClass().add("primary");
        Button resetButton = new Button("Valider");
        Button backButton = new Button("Retour");

        sendCodeButton.setOnAction(event -> {
            // Etape 4: Demander un code de reinitialisation
            try {
                String code = authService.requestPasswordReset(emailField.getText());
                showAlert("Code envoye", "Code de reinitialisation: " + code);
            } catch (ValidationException ex) {
                showAlert("Erreur", ex.getMessage());
            }
        });

        resetButton.setOnAction(event -> {
            // Etape 5: Verifier la confirmation
            if (!newPasswordField.getText().equals(confirmField.getText())) {
                showAlert("Mot de passe", "La confirmation ne correspond pas.");
                return;
            }
            // Etape 6: Changer le mot de passe si le code est valide
            boolean ok = authService.resetPassword(emailField.getText(), codeField.getText(), newPasswordField.getText());
            if (ok) {
                // Etape 7: Message de succes
                showAlert("OK", "Mot de passe change.");
                onBack.run();
            } else {
                // Etape 8: Message d'erreur
                showAlert("Erreur", "Code invalide.");
            }
        });

        // Etape 9: Retour a l'ecran precedent
        backButton.setOnAction(event -> onBack.run());

        VBox form = new VBox(10,
                UiFactory.labeledLine("Email", emailField),
                buildActionBar(sendCodeButton, backButton),
                UiFactory.labeledLine("Code", codeField),
                UiFactory.labeledLine("Nouveau mot de passe", newPasswordField),
                UiFactory.labeledLine("Confirmation", confirmField),
                buildActionBar(resetButton)
        );
        form.setPadding(new Insets(20));
        form.getStyleClass().add("card");

        root.setCenter(form);
        return root;
    }

    private void showAlert(String title, String message) {
        // Etape 1: Fenetre simple d'information
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Le mot de passe oublie n'avait pas de procedure.
Cause: Aucun ecran de reinitialisation.
Consequence: Utilisateur bloque.
Solution: Ecran avec code aleatoire.
Pourquoi: Permettre la recuperation simple.
Comment: Generer un code puis valider.
*/
