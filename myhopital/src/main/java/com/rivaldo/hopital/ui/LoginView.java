package com.rivaldo.hopital.ui;

import com.rivaldo.hopital.domain.Utilisateur;
import com.rivaldo.hopital.service.AuthService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * @Fichier LoginView.java
 * @Objectif Ecran de connexion de l'application desktop.
 * @Couche UI
 */
public class LoginView {
    private final AuthService authService;
    private final Consumer<Utilisateur> onLogin;
    private final Runnable onRegister;
    private final Runnable onReset;

    /**
     * @Objectif Construire l'ecran de connexion.
     * @param authService service d'authentification
     * @param onLogin action apres connexion
     * @param onRegister navigation vers inscription
     * @param onReset navigation vers reset mot de passe
     */
    public LoginView(AuthService authService, Consumer<Utilisateur> onLogin, Runnable onRegister, Runnable onReset) {
        // Etape 1 : memoriser le service d'authentification
        this.authService = authService;
        // Etape 2 : memoriser l'action apres connexion
        this.onLogin = onLogin;
        // Etape 3 : memoriser l'action vers l'inscription
        this.onRegister = onRegister;
        // Etape 4 : memoriser l'action vers le reset mot de passe
        this.onReset = onReset;
    }

    /**
     * @Objectif Construire le layout JavaFX.
     * @return racine graphique
     */
    public Parent createView() {
        // Etape 1 : creer le conteneur principal de l'ecran
        BorderPane root = new BorderPane();
        // Etape 2 : ajouter la classe CSS principale
        root.getStyleClass().add("login-root");

        // Etape 3 : creer le titre principal
        Label title = new Label("MyHopital - Gestion intelligente des soins");
        // Etape 4 : appliquer le style du titre
        title.getStyleClass().add("title");

        // Etape 5 : creer le texte secondaire
        Label subtitle = new Label("Coordination medicale en temps reel, dossiers partages, rendez-vous fiables.");
        // Etape 6 : appliquer le style du sous-titre
        subtitle.getStyleClass().add("subtitle");

        // Etape 7 : creer la description courte
        Label description = new Label("Unifier les patients, les medecins et les consultations dans un flux securise.");
        // Etape 8 : appliquer le style de la description
        description.getStyleClass().add("subtitle");

        // Etape 9 : construire l'en-tete avec les textes
        VBox header = new VBox(6, title, subtitle, description);
        // Etape 10 : ajouter du padding a l'en-tete
        header.setPadding(new Insets(30, 30, 10, 30));
        // Etape 11 : placer l'en-tete en haut
        root.setTop(header);

        // Etape 12 : creer le champ email
        TextField emailField = new TextField();
        // Etape 13 : definir le placeholder email
        emailField.setPromptText("Email");

        // Etape 14 : creer le champ mot de passe
        PasswordField passwordField = new PasswordField();
        // Etape 15 : definir le placeholder mot de passe
        passwordField.setPromptText("Mot de passe");

        // Etape 16 : creer le bouton de connexion
        Button loginButton = new Button("Se connecter");
        // Etape 17 : appliquer le style primaire
        loginButton.getStyleClass().add("primary");

        // Etape 18 : creer le bouton d'inscription
        Button registerButton = new Button("S'inscrire");
        // Etape 19 : appliquer le style secondaire
        registerButton.getStyleClass().add("secondary");

        // Etape 20 : creer le bouton mot de passe oublie
        Button forgotButton = new Button("Mot de passe oublie");
        // Etape 21 : appliquer le style lien
        forgotButton.getStyleClass().add("link");

        // Etape 22 : definir l'action du bouton de connexion
        loginButton.setOnAction(event -> {
            // Etape 23 : tenter la connexion avec les saisies
            Optional<Utilisateur> user = authService.login(emailField.getText(), passwordField.getText());
            // Etape 24 : verifier si la connexion reussit
            if (user.isPresent()) {
                // Etape 25 : executer l'action apres connexion
                onLogin.accept(user.get());
            } else {
                // Etape 26 : chercher si l'email existe pour message plus clair
                authService.findByEmail(emailField.getText()).ifPresentOrElse(found -> {
                    // Etape 27 : verifier si le directeur est en attente
                    if (found.getRole() == com.rivaldo.hopital.domain.Role.DIRECTEUR && !found.isApproved()) {
                        // Etape 28 : afficher le message d'attente
                        showAlert("Attente", "Votre compte directeur attend l'approbation.");
                    } else {
                        // Etape 29 : afficher l'erreur generique
                        showAlert("Connexion echouee", "Email ou mot de passe invalide.");
                    }
                }, () -> {
                    // Etape 30 : afficher l'erreur generique si email inconnu
                    showAlert("Connexion echouee", "Email ou mot de passe invalide.");
                });
            }
        });

        // Etape 31 : definir l'action vers l'inscription
        registerButton.setOnAction(event -> onRegister.run());
        // Etape 32 : definir l'action vers le reset mot de passe
        forgotButton.setOnAction(event -> onReset.run());

        // Etape 33 : construire le formulaire principal
        VBox form = new VBox(12,
            UiFactory.labeledLine("Email *", emailField),
            UiFactory.labeledLine("Mot de passe *", passwordField),
            new HBox(12, loginButton, registerButton, forgotButton)
        );
        // Etape 34 : ajouter du padding au formulaire
        form.setPadding(new Insets(20));
        // Etape 35 : appliquer le style carte
        form.getStyleClass().add("card");

        // Etape 36 : aligner le formulaire en haut au centre
        BorderPane.setAlignment(form, Pos.TOP_CENTER);
        // Etape 37 : placer le formulaire au centre
        root.setCenter(form);

        // Etape 38 : retourner la racine graphique
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
Probleme: L'ecran d'accueil etait trop neutre et peu rassurant.
Cause: Texte court sans message de confiance.
Consequence: Mauvaise perception du produit.
Solution: Ajouter un message de valeur et un bouton mot de passe oublie.
Pourquoi: Ameliorer l'experience et guider l'utilisateur.
Comment: Ajouter des labels et une action simple d'assistance.
*/
