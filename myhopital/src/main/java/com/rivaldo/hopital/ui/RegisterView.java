package com.rivaldo.hopital.ui;

import com.rivaldo.hopital.domain.Adresse;
import com.rivaldo.hopital.domain.MoyenContact;
import com.rivaldo.hopital.domain.Role;
import com.rivaldo.hopital.domain.Sexe;
import com.rivaldo.hopital.exception.ValidationException;
import com.rivaldo.hopital.service.AuthService;
import com.rivaldo.hopital.service.ValidationService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @Fichier RegisterView.java
 * @Objectif Ecran d'inscription des utilisateurs.
 * @Couche UI
 */
public class RegisterView {
    private final AuthService authService;
    private final ValidationService validationService;
    private final Runnable onBack;

    /**
     * @Objectif Construire l'ecran d'inscription.
     * @param authService service d'authentification
     * @param validationService outil de validation
     * @param onBack action retour
     */
    public RegisterView(AuthService authService, ValidationService validationService, Runnable onBack) {
        // Etape 1 : memoriser le service d'authentification
        this.authService = authService;
        // Etape 2 : memoriser l'outil de validation
        this.validationService = validationService;
        // Etape 3 : memoriser l'action de retour
        this.onBack = onBack;
    }

    /**
     * @Objectif Construire le layout JavaFX.
     * @return racine graphique
     */
    public Parent createView() {
        // Etape 1 : creer le conteneur principal de l'ecran
        BorderPane root = new BorderPane();
        // Etape 2 : ajouter la classe CSS principale
        root.getStyleClass().add("register-root");

        // Etape 3 : creer le titre principal
        Label title = new Label("Inscription Utilisateur");
        // Etape 4 : appliquer le style du titre
        title.getStyleClass().add("title");

        // Etape 5 : creer l'en-tete avec un sous-titre
        VBox header = new VBox(4, title, new Label("Creez un compte pour acceder au systeme"));
        // Etape 6 : ajouter du padding a l'en-tete
        header.setPadding(new Insets(30, 30, 10, 30));
        // Etape 7 : placer l'en-tete en haut
        root.setTop(header);

        // Etape 8 : creer les champs de saisie texte
        TextField nomField = new TextField();
        TextField prenomField = new TextField();
        DatePicker naissancePicker = new DatePicker();
        TextField emailField = new TextField();
        TextField telephoneField = new TextField();
        // Etape 9 : creer la liste de sexes
        ComboBox<Sexe> sexeBox = new ComboBox<>();
        // Etape 10 : charger les valeurs de sexe
        sexeBox.getItems().setAll(Sexe.values());

        // Etape 11 : creer les champs d'adresse
        TextField rueField = new TextField();
        TextField codePostalField = new TextField();
        TextField villeField = new TextField();
        TextField paysField = new TextField();
        // Etape 12 : creer la liste de contact prefere
        ComboBox<MoyenContact> contactBox = new ComboBox<>();
        // Etape 13 : charger les valeurs de contact
        contactBox.getItems().setAll(MoyenContact.values());

        // Etape 14 : creer la liste des roles
        ComboBox<Role> roleBox = new ComboBox<>();
        // Etape 15 : charger les valeurs des roles
        roleBox.getItems().setAll(Role.values());

        // Etape 16 : creer les champs de mot de passe
        PasswordField passwordField = new PasswordField();
        PasswordField confirmField = new PasswordField();

        // Etape 17 : creer le bouton d'inscription
        Button registerButton = new Button("Valider l'inscription");
        // Etape 18 : appliquer le style primaire
        registerButton.getStyleClass().add("primary");
        // Etape 19 : creer le bouton retour
        Button backButton = new Button("Retour");

        // Etape 20 : definir l'action du bouton d'inscription
        registerButton.setOnAction(event -> {
            // Etape 21 : verifier les champs obligatoires
            if (!validationService.isRequiredValid(nomField.getText())
                    || !validationService.isRequiredValid(prenomField.getText())
                    || !validationService.isEmailValid(emailField.getText())
                    || roleBox.getValue() == null) {
                // Etape 22 : afficher un message d'erreur
                showAlert("Champs incomplets", "Veuillez remplir les champs obligatoires.");
                // Etape 23 : arreter l'action si invalide
                return;
            }
            // Etape 24 : verifier la confirmation du mot de passe
            if (!passwordField.getText().equals(confirmField.getText())) {
                // Etape 25 : afficher un message d'erreur
                showAlert("Mot de passe", "La confirmation ne correspond pas.");
                // Etape 26 : arreter l'action si invalide
                return;
            }
            try {
                // Etape 27 : creer le compte utilisateur
                com.rivaldo.hopital.domain.Utilisateur created = authService.register(nomField.getText(), prenomField.getText(),
                        emailField.getText(), passwordField.getText(), roleBox.getValue(),
                        naissancePicker.getValue(), telephoneField.getText(), sexeBox.getValue(),
                        new Adresse(rueField.getText(), codePostalField.getText(), villeField.getText(), paysField.getText()),
                        contactBox.getValue());
                // Etape 28 : verifier si directeur en attente
                if (created.getRole() == Role.DIRECTEUR && !created.isApproved()) {
                    // Etape 29 : afficher le message d'attente
                    showAlert("Inscription OK", "Compte directeur en attente d'approbation.");
                } else {
                    // Etape 30 : afficher un message de bienvenue
                    showAlert("Inscription OK", validationService.randomWelcomeMessage());
                }
                // Etape 31 : revenir a l'ecran de connexion
                onBack.run();
            } catch (ValidationException ex) {
                // Etape 32 : afficher l'erreur metier
                showAlert("Inscription refusee", ex.getMessage());
            }
        });

        // Etape 33 : definir l'action du bouton retour
        backButton.setOnAction(event -> onBack.run());

        // Etape 34 : construire le formulaire principal
        VBox form = new VBox(10,
                UiFactory.labeledLine("Nom *", nomField),
                UiFactory.labeledLine("Prenom *", prenomField),
                UiFactory.labeledLine("Date naissance *", naissancePicker),
                UiFactory.labeledLine("Email *", emailField),
                UiFactory.labeledLine("Telephone *", telephoneField),
                UiFactory.labeledLine("Sexe *", sexeBox),
                UiFactory.labeledLine("Rue *", rueField),
                UiFactory.labeledLine("Code postal *", codePostalField),
                UiFactory.labeledLine("Ville *", villeField),
                UiFactory.labeledLine("Pays *", paysField),
                UiFactory.labeledLine("Contact prefere *", contactBox),
                UiFactory.labeledLine("Role *", roleBox),
                UiFactory.labeledLine("Mot de passe *", passwordField),
                UiFactory.labeledLine("Confirmation *", confirmField),
                new HBox(12, registerButton, backButton)
        );
        // Etape 35 : aligner le formulaire en haut a gauche
        form.setAlignment(Pos.TOP_LEFT);
        // Etape 36 : ajouter du padding au formulaire
        form.setPadding(new Insets(20));
        // Etape 37 : appliquer le style carte
        form.getStyleClass().add("card");

        // Etape 38 : placer le formulaire au centre
        root.setCenter(form);
        // Etape 39 : retourner la racine graphique
        return root;
    }

    /**
     * @Objectif Afficher une alerte simple.
     * @param title titre de la fenetre
     * @param message message a afficher
     */
    private void showAlert(String title, String message) {
        // Etape 1 : creer une alerte simple d'information
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
Probleme: Les champs obligatoires n'etaient pas visibles pour l'utilisateur.
Cause: Absence d'indicateur simple sur le formulaire.
Consequence: Erreurs de saisie et frustration.
Solution: Ajouter une etoile pour les champs obligatoires.
Pourquoi: Clarifier l'attendu et accelerer la saisie.
Comment: Modifier les labels et garder une validation cote service.
*/
