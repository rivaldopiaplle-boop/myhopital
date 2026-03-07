package com.rivaldo.hopital.ui;

import com.rivaldo.hopital.domain.Adresse;
import com.rivaldo.hopital.domain.Medecin;
import com.rivaldo.hopital.domain.MoyenContact;
import com.rivaldo.hopital.domain.Sexe;
import com.rivaldo.hopital.domain.Specialite;
import com.rivaldo.hopital.repository.DataStore;
import com.rivaldo.hopital.service.MedecinService;
import com.rivaldo.hopital.service.RendezVousService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

/**
 * @Fichier MedecinView.java
 * @Objectif Ecran de gestion des medecins.
 * @Couche UI
 */
public class MedecinView implements UiCrudSupport {
    private final DataStore dataStore;
    private final MedecinService medecinService;
    private final RendezVousService rendezVousService;
    private final Consumer<Medecin> onOpenConsultations;
    private final Consumer<Medecin> onOpenDocuments;

    /**
     * @Objectif Construire l'ecran medecin.
     * @param dataStore stockage en memoire
     * @param medecinService service medecin
     * @param rendezVousService service rendez-vous
     * @param onOpenConsultations action ouvrir consultations
     * @param onOpenDocuments action ouvrir documents
     */
    public MedecinView(DataStore dataStore, MedecinService medecinService, RendezVousService rendezVousService,
                       Consumer<Medecin> onOpenConsultations, Consumer<Medecin> onOpenDocuments) {
        // Etape 1 : memoriser le stockage
        this.dataStore = dataStore;
        // Etape 2 : memoriser le service medecin
        this.medecinService = medecinService;
        // Etape 3 : memoriser le service rendez-vous
        this.rendezVousService = rendezVousService;
        // Etape 4 : memoriser l'action d'ouverture des consultations
        this.onOpenConsultations = onOpenConsultations;
        // Etape 5 : memoriser l'action d'ouverture des documents
        this.onOpenDocuments = onOpenDocuments;
    }

    /**
     * @Objectif Construire le layout JavaFX.
     * @return racine graphique
     */
    public Parent createView() {
        // Etape 1 : creer le conteneur principal
        BorderPane root = new BorderPane();

        // Etape 2 : creer le tableau des medecins
        TableView<Medecin> table = new TableView<>();
        // Etape 2 bis : forcer l'ajustement des colonnes
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Etape 3 : creer la colonne ID
        TableColumn<Medecin, String> idCol = new TableColumn<>("ID");
        // Etape 4 : lier la colonne ID au champ id
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Etape 5 : creer la colonne matricule
        TableColumn<Medecin, String> matriculeCol = new TableColumn<>("Matricule");
        // Etape 6 : lier la colonne matricule au champ matricule
        matriculeCol.setCellValueFactory(new PropertyValueFactory<>("matricule"));

        // Etape 7 : creer la colonne nom
        TableColumn<Medecin, String> nomCol = new TableColumn<>("Nom");
        // Etape 8 : lier la colonne nom au champ nom
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));

        // Etape 9 : creer la colonne prenom
        TableColumn<Medecin, String> prenomCol = new TableColumn<>("Prenom");
        // Etape 10 : lier la colonne prenom au champ prenom
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));

        // Etape 11 : creer la colonne specialite
        TableColumn<Medecin, String> specCol = new TableColumn<>("Specialite");
        // Etape 12 : lier la colonne specialite au champ specialite
        specCol.setCellValueFactory(new PropertyValueFactory<>("specialite"));

        // Etape 13 : creer la colonne naissance
        TableColumn<Medecin, String> naissanceCol = new TableColumn<>("Naissance");
        // Etape 14 : lier la colonne naissance au champ dateNaissance
        naissanceCol.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));

        // Etape 15 : creer la colonne email
        TableColumn<Medecin, String> emailCol = new TableColumn<>("Email");
        // Etape 16 : lier la colonne email au champ email
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Etape 17 : creer la colonne telephone
        TableColumn<Medecin, String> telCol = new TableColumn<>("Telephone");
        // Etape 18 : lier la colonne telephone au champ telephone
        telCol.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        // Etape 19 : creer la colonne sexe
        TableColumn<Medecin, String> sexeCol = new TableColumn<>("Sexe");
        // Etape 20 : formater le sexe en texte lisible
        sexeCol.setCellValueFactory(cell -> new SimpleStringProperty(
            cell.getValue().getSexe() != null ? cell.getValue().getSexe().name() : ""));

        // Etape 21 : creer la colonne adresse
        TableColumn<Medecin, String> adresseCol = new TableColumn<>("Adresse");
        // Etape 22 : formater l'adresse en texte court
        adresseCol.setCellValueFactory(cell -> new SimpleStringProperty(formatAdresse(cell.getValue().getAdresse())));

        // Etape 23 : creer la colonne contact
        TableColumn<Medecin, String> contactCol = new TableColumn<>("Contact");
        // Etape 24 : formater le contact en texte lisible
        contactCol.setCellValueFactory(cell -> new SimpleStringProperty(
            cell.getValue().getMoyenContact() != null ? cell.getValue().getMoyenContact().name() : ""));

        // Etape 25 : ajouter toutes les colonnes au tableau
        table.getColumns().addAll(idCol, matriculeCol, nomCol, prenomCol, emailCol, telCol,
            naissanceCol, sexeCol, adresseCol, contactCol, specCol);

        // Etape 26 : creer le champ de recherche
        TextField searchField = new TextField();
        // Etape 27 : definir le placeholder de recherche
        searchField.setPromptText("Rechercher medecin...");

        // Etape 27 bis : creer la liste de criteres de recherche
        ComboBox<String> sortBox = new ComboBox<>();
        // Etape 27 ter : definir les choix de recherche
        sortBox.getItems().setAll("Tous", "ID", "Matricule", "Nom", "Prenom", "Specialite", "Naissance",
            "Email", "Telephone", "Sexe", "Adresse", "Contact");
        // Etape 27 quater : definir le critere par defaut
        sortBox.setValue("Tous");

        // Etape 28 : creer la liste filtree liee au DataStore
        FilteredList<Medecin> filtered = new FilteredList<>(dataStore.getMedecins(), m -> true);
        // Etape 29 : connecter la liste filtree au tableau
        table.setItems(filtered);

        // Etape 30 : definir l'action de recherche
        Runnable applySearch = () -> {
            // Etape 31 : normaliser le mot cle
            String keyword = searchField.getText() != null ? searchField.getText().trim().toLowerCase() : "";
            // Etape 32 : lire le critere de recherche
            String criterion = sortBox.getValue();
            // Etape 33 : appliquer le predicat de recherche
            filtered.setPredicate(medecin -> {
                if (keyword.isEmpty()) {
                    return true;
                }
                if (criterion == null || "Tous".equals(criterion)) {
                    return medecin.getNom().toLowerCase().contains(keyword)
                            || medecin.getPrenom().toLowerCase().contains(keyword);
                }
                return switch (criterion) {
                    case "ID" -> medecin.getId().toLowerCase().contains(keyword);
                    case "Matricule" -> medecin.getMatricule() != null
                        && medecin.getMatricule().toLowerCase().contains(keyword);
                    case "Nom" -> medecin.getNom().toLowerCase().contains(keyword);
                    case "Prenom" -> medecin.getPrenom().toLowerCase().contains(keyword);
                    case "Specialite" -> medecin.getSpecialite() != null
                        && medecin.getSpecialite().name().toLowerCase().contains(keyword);
                    case "Naissance" -> medecin.getDateNaissance() != null
                        && medecin.getDateNaissance().toString().toLowerCase().contains(keyword);
                    case "Email" -> medecin.getEmail().toLowerCase().contains(keyword);
                    case "Telephone" -> medecin.getTelephone() != null
                        && medecin.getTelephone().toLowerCase().contains(keyword);
                    case "Sexe" -> medecin.getSexe() != null
                        && medecin.getSexe().name().toLowerCase().contains(keyword);
                    case "Adresse" -> formatAdresse(medecin.getAdresse()).toLowerCase().contains(keyword);
                    case "Contact" -> medecin.getMoyenContact() != null
                        && medecin.getMoyenContact().name().toLowerCase().contains(keyword);
                    default -> false;
                };
            });
        };

        // Etape 34 : ecouter les changements de recherche
        searchField.textProperty().addListener((obs, oldValue, newValue) -> applySearch.run());
        // Etape 35 : ecouter les changements de critere
        sortBox.valueProperty().addListener((obs, oldValue, newValue) -> applySearch.run());

        // Etape 33 : creer les champs du formulaire
        TextField nomField = new TextField();
        TextField prenomField = new TextField();
        DatePicker naissancePicker = new DatePicker();
        TextField emailField = new TextField();
        TextField telephoneField = new TextField();
        ComboBox<Sexe> sexeBox = new ComboBox<>(FXCollections.observableArrayList(Sexe.values()));
        TextField rueField = new TextField();
        TextField codePostalField = new TextField();
        TextField villeField = new TextField();
        TextField paysField = new TextField();
        ComboBox<MoyenContact> contactBox = new ComboBox<>(FXCollections.observableArrayList(MoyenContact.values()));
        ComboBox<Specialite> specialiteBox = new ComboBox<>(FXCollections.observableArrayList(Specialite.values()));

        // Etape 34 : creer les boutons d'action
        Button addButton = new Button("Enregistrer");
        // Etape 35 : appliquer le style primaire
        addButton.getStyleClass().add("primary");
        Button updateButton = new Button("Modifier");
        Button deleteButton = new Button("Supprimer");
        Button clearButton = new Button("Actualiser");
        Button consultationsButton = new Button("Consultations");
        Button documentsButton = new Button("Documents");

        // Etape 36 : creer la liste des disponibilites
        ListView<String> disponibilites = new ListView<>();
        // Etape 37 : fixer la hauteur de la liste
        disponibilites.setPrefHeight(120);
        // Etape 38 : creer la source des disponibilites
        ObservableList<String> disponibiliteItems = FXCollections.observableArrayList();
        // Etape 39 : connecter la source a la liste
        disponibilites.setItems(disponibiliteItems);

        // Etape 40 : definir l'action d'ajout
        addButton.setOnAction(event -> {
            // Etape 41 : creer un medecin depuis le formulaire
            Medecin medecin = new Medecin(null, null, nomField.getText(), prenomField.getText(),
                    naissancePicker.getValue(), sexeBox.getValue(), telephoneField.getText(), emailField.getText(),
                    new Adresse(rueField.getText(), codePostalField.getText(), villeField.getText(), paysField.getText()),
                    contactBox.getValue(), specialiteBox.getValue());
            // Etape 42 : enregistrer le medecin
            medecinService.create(medecin);
            // Etape 43 : nettoyer le formulaire
            clearForm(nomField, prenomField, naissancePicker, emailField, telephoneField, sexeBox, rueField,
                    codePostalField, villeField, paysField, contactBox, specialiteBox);
        });

        // Etape 44 : definir l'action de modification
        updateButton.setOnAction(event -> {
            // Etape 45 : recuperer le medecin selectionne
            Medecin selected = table.getSelectionModel().getSelectedItem();
            // Etape 46 : sortir si rien n'est selectionne
            if (selected == null) {
                return;
            }
            // Etape 47 : copier les champs vers l'objet
            selected.setNom(nomField.getText());
            selected.setPrenom(prenomField.getText());
            selected.setDateNaissance(naissancePicker.getValue());
            selected.setEmail(emailField.getText());
            selected.setTelephone(telephoneField.getText());
            selected.setSexe(sexeBox.getValue());
            selected.setAdresse(new Adresse(rueField.getText(), codePostalField.getText(), villeField.getText(), paysField.getText()));
            selected.setMoyenContact(contactBox.getValue());
            selected.setSpecialite(specialiteBox.getValue());
            // Etape 47 bis : persister la modification
            medecinService.update(selected);
            // Etape 48 : rafraichir le tableau
            table.refresh();
        });

        // Etape 49 : definir l'action de suppression
        deleteButton.setOnAction(event -> {
            // Etape 50 : recuperer le medecin selectionne
            Medecin selected = table.getSelectionModel().getSelectedItem();
            // Etape 51 : supprimer si selection existe
            if (selected != null) {
                medecinService.delete(selected);
            }
        });

        // Etape 52 : definir l'action de nettoyage
        clearButton.setOnAction(event -> clearForm(nomField, prenomField, naissancePicker, emailField, telephoneField,
                sexeBox, rueField, codePostalField, villeField, paysField, contactBox, specialiteBox));

        // Etape 52 bis : definir l'action d'ouverture des consultations
        consultationsButton.setOnAction(event -> {
            // Etape 52 ter : recuperer le medecin selectionne
            Medecin selected = table.getSelectionModel().getSelectedItem();
            // Etape 52 quater : ouvrir les consultations si selection valide
            if (selected != null) {
                onOpenConsultations.accept(selected);
            }
        });

        // Etape 52 quinquies : definir l'action d'ouverture des documents
        documentsButton.setOnAction(event -> {
            // Etape 52 sexies : recuperer le medecin selectionne
            Medecin selected = table.getSelectionModel().getSelectedItem();
            // Etape 52 septies : ouvrir les documents si selection valide
            if (selected != null) {
                onOpenDocuments.accept(selected);
            }
        });

        // Etape 53 : ecouter la selection dans le tableau
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {
            // Etape 54 : sortir si pas de selection
            if (selected == null) {
                return;
            }
            // Etape 55 : remplir le formulaire avec la selection
            nomField.setText(selected.getNom());
            prenomField.setText(selected.getPrenom());
            naissancePicker.setValue(selected.getDateNaissance());
            emailField.setText(selected.getEmail());
            telephoneField.setText(selected.getTelephone());
            sexeBox.setValue(selected.getSexe());
            if (selected.getAdresse() != null) {
                // Etape 56 : copier l'adresse dans le formulaire
                rueField.setText(selected.getAdresse().getRue());
                codePostalField.setText(selected.getAdresse().getCodePostal());
                villeField.setText(selected.getAdresse().getVille());
                paysField.setText(selected.getAdresse().getPays());
            }
            contactBox.setValue(selected.getMoyenContact());
            specialiteBox.setValue(selected.getSpecialite());
            // Etape 57 : charger les disponibilites
            refreshDisponibilites(selected.getId(), disponibiliteItems);
        });

        // Etape 58 : ecouter les changements de rendez-vous
        dataStore.getRendezVous().addListener((javafx.collections.ListChangeListener.Change<?> change) -> {
            // Etape 59 : recuperer le medecin selectionne
            Medecin selected = table.getSelectionModel().getSelectedItem();
            // Etape 60 : rafraichir les disponibilites si selection valide
            if (selected != null) {
                refreshDisponibilites(selected.getId(), disponibiliteItems);
            }
        });

        // Etape 61 : construire le formulaire
        VBox form = new VBox(8,
                UiFactory.labeledLine("Nom", nomField),
                UiFactory.labeledLine("Prenom", prenomField),
                UiFactory.labeledLine("Date naissance", naissancePicker),
                UiFactory.labeledLine("Email", emailField),
                UiFactory.labeledLine("Telephone", telephoneField),
                UiFactory.labeledLine("Sexe", sexeBox),
                UiFactory.labeledLine("Rue", rueField),
                UiFactory.labeledLine("Code postal", codePostalField),
                UiFactory.labeledLine("Ville", villeField),
                UiFactory.labeledLine("Pays", paysField),
                UiFactory.labeledLine("Contact", contactBox),
                UiFactory.labeledLine("Specialite", specialiteBox),
            buildActionBar(addButton, updateButton, deleteButton, clearButton, consultationsButton, documentsButton),
                new Label("Disponibilites medecin"),
                disponibilites
        );
        // Etape 62 : ajouter du padding au formulaire
        form.setPadding(new Insets(10));
        // Etape 63 : appliquer le style carte
        form.getStyleClass().add("card");

            // Etape 63 bis : rendre le formulaire redimensionnable
            form.setMaxWidth(Double.MAX_VALUE);

            // Etape 63 ter : creer un scroll pour voir tout le formulaire
            ScrollPane formScroll = new ScrollPane(form);
            // Etape 63 quater : activer l'ajustement en largeur
            formScroll.setFitToWidth(true);

        // Etape 64 : construire la barre d'outils
        HBox tools = new HBox(10, searchField, sortBox);
        // Etape 64 bis : ajouter du padding aux outils
        tools.setPadding(new Insets(10));

        // Etape 65 : construire la colonne de gauche
        VBox left = new VBox(10, tools, table);
        // Etape 65 : ajouter du padding a gauche
        left.setPadding(new Insets(10));
            // Etape 66 : laisser le tableau grandir en hauteur
            VBox.setVgrow(table, Priority.ALWAYS);
        // Etape 67 : creer un SplitPane pour le redimensionnement
        SplitPane split = new SplitPane();
        // Etape 68 : definir la position initiale du separateur
        split.setDividerPositions(0.55);
        // Etape 69 : ajouter la colonne gauche et le formulaire
        split.getItems().addAll(left, formScroll);
        // Etape 70 : placer le SplitPane au centre
        root.setCenter(split);

        // Etape 71 : retourner la racine graphique
        return root;
    }

    /**
     * @Objectif Formater une adresse pour le tableau.
     * @param adresse adresse a formater
     * @return texte court
     */
    private String formatAdresse(Adresse adresse) {
        // Etape 1 : verifier si l'adresse est absente
        if (adresse == null) {
            // Etape 2 : retourner une chaine vide
            return "";
        }
        // Etape 3 : construire une version courte
        return adresse.getRue() + ", " + adresse.getVille() + ", " + adresse.getPays();
    }

    /**
     * @Objectif Recalculer et afficher les disponibilites.
     * @param medecinId identifiant medecin
     * @param target liste cible
     */
    private void refreshDisponibilites(String medecinId, ObservableList<String> target) {
        // Etape 1 : recuperer les creneaux disponibles
        List<LocalDateTime> slots = rendezVousService.availableSlotsForMedecin(medecinId);
        // Etape 2 : convertir en texte et pousser dans la liste
        target.setAll(slots.stream().map(LocalDateTime::toString).toList());
    }

    /**
     * @Objectif Vider le formulaire.
     * @param nomField champ nom
     * @param prenomField champ prenom
     * @param naissancePicker champ date
     * @param emailField champ email
     * @param telephoneField champ telephone
     * @param sexeBox champ sexe
     * @param rueField champ rue
     * @param codePostalField champ code postal
     * @param villeField champ ville
     * @param paysField champ pays
     * @param contactBox champ contact
     * @param specialiteBox champ specialite
     */
    private void clearForm(TextField nomField, TextField prenomField, DatePicker naissancePicker, TextField emailField,
                           TextField telephoneField, ComboBox<Sexe> sexeBox, TextField rueField,
                           TextField codePostalField, TextField villeField, TextField paysField,
                           ComboBox<MoyenContact> contactBox, ComboBox<Specialite> specialiteBox) {
        // Etape 1 : effacer le nom
        nomField.clear();
        // Etape 2 : effacer le prenom
        prenomField.clear();
        // Etape 3 : effacer la date
        naissancePicker.setValue(null);
        // Etape 4 : effacer l'email
        emailField.clear();
        // Etape 5 : effacer le telephone
        telephoneField.clear();
        // Etape 6 : effacer le sexe
        sexeBox.setValue(null);
        // Etape 7 : effacer la rue
        rueField.clear();
        // Etape 8 : effacer le code postal
        codePostalField.clear();
        // Etape 9 : effacer la ville
        villeField.clear();
        // Etape 10 : effacer le pays
        paysField.clear();
        // Etape 11 : effacer le contact
        contactBox.setValue(null);
        // Etape 12 : effacer la specialite
        specialiteBox.setValue(null);
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Pas de suivi des medecins dans l'outil.
Cause: Absence d'interface dediee.
Consequence: Difficile de lier rendez-vous et consultations.
Solution: Ecran de gestion medecin avec table.
Pourquoi: Assurer la coherence des donnees.
Comment: TableView et formulaire synchronises.
*/
