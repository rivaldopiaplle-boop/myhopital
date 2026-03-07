package com.rivaldo.hopital.ui;

import com.rivaldo.hopital.domain.Utilisateur;
import com.rivaldo.hopital.service.AuthService;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * @Fichier ChangePasswordView.java
 * @Objectif Ecran pour changer le mot de passe.
 * @Couche UI
 */
public class ChangePasswordView implements UiCrudSupport {
    private final AuthService authService;
    private final Utilisateur utilisateur;

    /**
     * @Objectif Construire l'ecran de changement mot de passe.
     * @param authService service auth
     * @param utilisateur utilisateur connecte
     */
    public ChangePasswordView(AuthService authService, Utilisateur utilisateur) {
        this.authService = authService;
        this.utilisateur = utilisateur;
    }

    /**
     * @Objectif Construire le layout JavaFX.
     * @return racine graphique
     */
    public Parent createView() {
        // Etape 1: Email visible mais non modifiable
        TextField emailField = new TextField(utilisateur.getEmail());
        // Etape 2: Bloquer la modification de l'email
        emailField.setDisable(true);
        // Etape 3: Creer les champs mot de passe
        PasswordField currentField = new PasswordField();
        PasswordField newField = new PasswordField();
        PasswordField confirmField = new PasswordField();

        // Etape 4: Creer le bouton d'action
        Button saveButton = new Button("Changer");
        saveButton.getStyleClass().add("primary");

        saveButton.setOnAction(event -> {
            // Etape 5: Verifier la confirmation
            if (!newField.getText().equals(confirmField.getText())) {
                showAlert("Mot de passe", "La confirmation ne correspond pas.");
                return;
            }
            // Etape 6: Appeler le service pour changer le mot de passe
            boolean ok = authService.changePassword(utilisateur.getEmail(), currentField.getText(), newField.getText());
            if (ok) {
                // Etape 7: Afficher un message de succes
                showAlert("OK", "Mot de passe mis a jour.");
                // Etape 8: Nettoyer les champs
                currentField.clear();
                newField.clear();
                confirmField.clear();
            } else {
                // Etape 9: Afficher l'erreur
                showAlert("Erreur", "Mot de passe actuel incorrect.");
            }
        });

        VBox form = new VBox(10,
                UiFactory.labeledLine("Email", emailField),
                UiFactory.labeledLine("Mot de passe actuel", currentField),
                UiFactory.labeledLine("Nouveau mot de passe", newField),
                UiFactory.labeledLine("Confirmation", confirmField),
                buildActionBar(saveButton)
        );
        form.setPadding(new Insets(20));
        form.getStyleClass().add("card");

        return form;
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
Probleme: Aucun changement de mot de passe apres inscription.
Cause: Fonction manquante.
Consequence: Mot de passe non evolutif.
Solution: Ecran de changement mot de passe.
Pourquoi: Offrir une gestion securisee.
Comment: Verifier l'ancien mot de passe.
*/
