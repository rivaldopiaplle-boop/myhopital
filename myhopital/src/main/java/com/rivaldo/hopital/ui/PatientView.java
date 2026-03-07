package com.rivaldo.hopital.ui;

import com.rivaldo.hopital.domain.Adresse;
import com.rivaldo.hopital.domain.EtatDossier;
import com.rivaldo.hopital.domain.EtatPatient;
import com.rivaldo.hopital.domain.MoyenContact;
import com.rivaldo.hopital.domain.Patient;
import com.rivaldo.hopital.domain.Sexe;
import com.rivaldo.hopital.repository.DataStore;
import com.rivaldo.hopital.service.PatientService;
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
 * @Fichier PatientView.java
 * @Objectif Ecran de gestion des patients.
 * @Couche UI
 */
public class PatientView implements UiCrudSupport {
    private final DataStore dataStore;
    private final PatientService patientService;
    private final RendezVousService rendezVousService;
    private final Consumer<Patient> onOpenDossier;
    private final Consumer<Patient> onOpenConsultations;
    private final Consumer<Patient> onOpenDocuments;

    /**
     * @Objectif Construire l'ecran patient.
     * @param dataStore stockage en memoire
     * @param patientService service patient
     * @param rendezVousService service rendez-vous
     * @param onOpenDossier action ouvrir dossier
     * @param onOpenConsultations action ouvrir consultations
     * @param onOpenDocuments action ouvrir documents
     */
    public PatientView(DataStore dataStore, PatientService patientService, RendezVousService rendezVousService,
                       Consumer<Patient> onOpenDossier, Consumer<Patient> onOpenConsultations,
                       Consumer<Patient> onOpenDocuments) {
        // Etape 1 : memoriser le stockage
        this.dataStore = dataStore;
        // Etape 2 : memoriser le service patient
        this.patientService = patientService;
        // Etape 3 : memoriser le service rendez-vous
        this.rendezVousService = rendezVousService;
        // Etape 4 : memoriser l'action d'ouverture du dossier
        this.onOpenDossier = onOpenDossier;
        // Etape 5 : memoriser l'action d'ouverture des consultations
        this.onOpenConsultations = onOpenConsultations;
        // Etape 6 : memoriser l'action d'ouverture des documents
        this.onOpenDocuments = onOpenDocuments;
    }

    /**
     * @Objectif Construire le layout JavaFX.
     * @return racine graphique
     */
    public Parent createView() {
        // Etape 1 : creer le conteneur principal
        BorderPane root = new BorderPane();

        // Etape 2 : creer le tableau des patients
        TableView<Patient> table = new TableView<>();
        // Etape 2 bis : forcer l'ajustement des colonnes
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Etape 3 : creer la colonne ID
        TableColumn<Patient, String> idCol = new TableColumn<>("ID");
        // Etape 4 : lier la colonne ID au champ id
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Etape 5 : creer la colonne dossier
        TableColumn<Patient, String> dossierCol = new TableColumn<>("Dossier");
        // Etape 6 : lier la colonne dossier au champ dossierNumber
        dossierCol.setCellValueFactory(new PropertyValueFactory<>("dossierNumber"));

        // Etape 7 : creer la colonne nom
        TableColumn<Patient, String> nomCol = new TableColumn<>("Nom");
        // Etape 8 : lier la colonne nom au champ nom
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));

        // Etape 9 : creer la colonne prenom
        TableColumn<Patient, String> prenomCol = new TableColumn<>("Prenom");
        // Etape 10 : lier la colonne prenom au champ prenom
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));

        // Etape 11 : creer la colonne email
        TableColumn<Patient, String> emailCol = new TableColumn<>("Email");
        // Etape 12 : lier la colonne email au champ email
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Etape 13 : creer la colonne telephone
        TableColumn<Patient, String> telCol = new TableColumn<>("Telephone");
        // Etape 14 : lier la colonne telephone au champ telephone
        telCol.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        // Etape 15 : creer la colonne sexe
        TableColumn<Patient, String> sexeCol = new TableColumn<>("Sexe");
        // Etape 16 : formater le sexe en texte lisible
        sexeCol.setCellValueFactory(cell -> new SimpleStringProperty(
            cell.getValue().getSexe() != null ? cell.getValue().getSexe().name() : ""));

        // Etape 17 : creer la colonne adresse
        TableColumn<Patient, String> adresseCol = new TableColumn<>("Adresse");
        // Etape 18 : formater l'adresse en texte court
        adresseCol.setCellValueFactory(cell -> new SimpleStringProperty(formatAdresse(cell.getValue().getAdresse())));

        // Etape 19 : creer la colonne contact
        TableColumn<Patient, String> contactCol = new TableColumn<>("Contact");
        // Etape 20 : formater le contact en texte lisible
        contactCol.setCellValueFactory(cell -> new SimpleStringProperty(
            cell.getValue().getMoyenContact() != null ? cell.getValue().getMoyenContact().name() : ""));

        // Etape 21 : creer la colonne naissance
        TableColumn<Patient, String> naissanceCol = new TableColumn<>("Naissance");
        // Etape 22 : lier la colonne naissance au champ dateNaissance
        naissanceCol.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));

        // Etape 23 : creer la colonne date dossier
        TableColumn<Patient, String> dossierDateCol = new TableColumn<>("Date dossier");
        // Etape 24 : formater la date d'ouverture du dossier
        dossierDateCol.setCellValueFactory(cell -> new SimpleStringProperty(formatDossierDate(cell.getValue())));

        // Etape 25 : creer la colonne etat dossier
        TableColumn<Patient, String> dossierEtatCol = new TableColumn<>("Etat dossier");
        // Etape 26 : formater l'etat du dossier
        dossierEtatCol.setCellValueFactory(cell -> new SimpleStringProperty(formatDossierEtat(cell.getValue())));

        // Etape 27 : creer la colonne etat patient
        TableColumn<Patient, String> etatCol = new TableColumn<>("Etat");
        // Etape 28 : lier la colonne etat au champ etat
        etatCol.setCellValueFactory(new PropertyValueFactory<>("etat"));

        // Etape 29 : ajouter toutes les colonnes au tableau
        table.getColumns().addAll(idCol, dossierCol, nomCol, prenomCol, emailCol, telCol, sexeCol,
            adresseCol, contactCol, naissanceCol, dossierDateCol, dossierEtatCol, etatCol);

        // Etape 30 : creer le champ de recherche
        TextField searchField = new TextField();
        // Etape 31 : definir le placeholder de recherche
        searchField.setPromptText("Rechercher patient...");

        // Etape 32 : creer la liste de criteres de recherche
        ComboBox<String> sortBox = new ComboBox<>();
        // Etape 33 : definir les choix de recherche
        sortBox.getItems().setAll("Tous", "ID", "Dossier", "Nom", "Prenom", "Email", "Telephone", "Sexe",
            "Adresse", "Contact", "Naissance", "Date dossier", "Etat dossier", "Etat");
        // Etape 34 : definir le critere par defaut
        sortBox.setValue("Tous");

        // Etape 35 : creer la liste filtree liee au DataStore
        FilteredList<Patient> filtered = new FilteredList<>(dataStore.getPatients(), p -> true);
        // Etape 36 : connecter la liste filtree au tableau
        table.setItems(filtered);

        // Etape 37 : definir l'action de recherche
        Runnable applySearch = () -> {
            // Etape 38 : normaliser le mot cle
            String keyword = searchField.getText() != null ? searchField.getText().trim().toLowerCase() : "";
            // Etape 39 : lire le critere de recherche
            String criterion = sortBox.getValue();
            // Etape 40 : appliquer le predicat de recherche
            filtered.setPredicate(patient -> {
                if (keyword.isEmpty()) {
                    return true;
                }
                if (criterion == null || "Tous".equals(criterion)) {
                    return patient.getNom().toLowerCase().contains(keyword)
                        || patient.getPrenom().toLowerCase().contains(keyword)
                        || patient.getEmail().toLowerCase().contains(keyword)
                        || (patient.getTelephone() != null && patient.getTelephone().toLowerCase().contains(keyword))
                        || (patient.getDossierNumber() != null && patient.getDossierNumber().toLowerCase().contains(keyword));
                }
                return switch (criterion) {
                    case "ID" -> patient.getId().toLowerCase().contains(keyword);
                    case "Dossier" -> patient.getDossierNumber() != null
                        && patient.getDossierNumber().toLowerCase().contains(keyword);
                    case "Nom" -> patient.getNom().toLowerCase().contains(keyword);
                    case "Prenom" -> patient.getPrenom().toLowerCase().contains(keyword);
                    case "Email" -> patient.getEmail().toLowerCase().contains(keyword);
                    case "Telephone" -> patient.getTelephone() != null
                        && patient.getTelephone().toLowerCase().contains(keyword);
                    case "Sexe" -> patient.getSexe() != null
                        && patient.getSexe().name().toLowerCase().contains(keyword);
                    case "Adresse" -> formatAdresse(patient.getAdresse()).toLowerCase().contains(keyword);
                    case "Contact" -> patient.getMoyenContact() != null
                        && patient.getMoyenContact().name().toLowerCase().contains(keyword);
                    case "Naissance" -> patient.getDateNaissance() != null
                        && patient.getDateNaissance().toString().toLowerCase().contains(keyword);
                    case "Date dossier" -> formatDossierDate(patient).toLowerCase().contains(keyword);
                    case "Etat dossier" -> formatDossierEtat(patient).toLowerCase().contains(keyword);
                    case "Etat" -> patient.getEtat() != null
                        && patient.getEtat().name().toLowerCase().contains(keyword);
                    default -> false;
                };
            });
        };

        // Etape 41 : ecouter les changements de recherche
        searchField.textProperty().addListener((obs, oldValue, newValue) -> applySearch.run());
        // Etape 42 : ecouter les changements de critere
        sortBox.valueProperty().addListener((obs, oldValue, newValue) -> applySearch.run());

        // Etape 45 : creer la barre d'outils
        HBox tools = new HBox(10, searchField, sortBox);
        // Etape 46 : ajouter du padding a la barre d'outils
        tools.setPadding(new Insets(10));

        // Etape 47 : creer les champs du formulaire
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
        ComboBox<EtatPatient> etatBox = new ComboBox<>(FXCollections.observableArrayList(EtatPatient.values()));

        // Etape 48 : creer la liste ouvrir dossier
        ComboBox<String> openDossierBox = new ComboBox<>(FXCollections.observableArrayList("Oui", "Non"));
        // Etape 49 : definir la valeur par defaut
        openDossierBox.setValue("Oui");
        // Etape 50 : creer la liste d'etats de dossier
        ComboBox<EtatDossier> etatDossierBox = new ComboBox<>(FXCollections.observableArrayList(EtatDossier.values()));
        // Etape 51 : definir l'etat par defaut
        etatDossierBox.setValue(EtatDossier.OUVERT);

        // Etape 52 : creer les boutons d'action
        Button addButton = new Button("Enregistrer");
        // Etape 53 : appliquer le style primaire
        addButton.getStyleClass().add("primary");
        Button updateButton = new Button("Modifier");
        Button deleteButton = new Button("Supprimer");
        Button clearButton = new Button("Actualiser");
        Button dossiersButton = new Button("Dossiers");
        Button consultationsButton = new Button("Consultations");
        Button documentsButton = new Button("Documents");

        // Etape 54 : creer la liste des disponibilites
        ListView<String> disponibilites = new ListView<>();
        // Etape 55 : fixer la hauteur de la liste
        disponibilites.setPrefHeight(120);

        // Etape 56 : creer la source des disponibilites
        ObservableList<String> disponibiliteItems = FXCollections.observableArrayList();
        // Etape 57 : connecter la source a la liste
        disponibilites.setItems(disponibiliteItems);

        // Etape 58 : definir l'action d'ajout
        addButton.setOnAction(event -> {
            // Etape 59 : creer un patient depuis le formulaire
            Patient patient = new Patient(null, null, nomField.getText(), prenomField.getText(),
                    naissancePicker.getValue(), sexeBox.getValue(), telephoneField.getText(), emailField.getText(),
                    new Adresse(rueField.getText(), codePostalField.getText(), villeField.getText(), paysField.getText()),
                    contactBox.getValue(), etatBox.getValue());
            // Etape 60 : determiner si on ouvre un dossier
            boolean openDossier = "Oui".equals(openDossierBox.getValue());
            // Etape 61 : enregistrer le patient
            patientService.create(patient, openDossier, etatDossierBox.getValue());
            // Etape 62 : nettoyer le formulaire
            clearForm(nomField, prenomField, naissancePicker, emailField, telephoneField, sexeBox,
                    rueField, codePostalField, villeField, paysField, contactBox, etatBox);
        });

        // Etape 63 : definir l'action de modification
        updateButton.setOnAction(event -> {
            // Etape 64 : recuperer le patient selectionne
            Patient selected = table.getSelectionModel().getSelectedItem();
            // Etape 65 : sortir si rien n'est selectionne
            if (selected == null) {
                return;
            }
            // Etape 66 : copier les champs vers l'objet
            selected.setNom(nomField.getText());
            selected.setPrenom(prenomField.getText());
            selected.setDateNaissance(naissancePicker.getValue());
            selected.setEmail(emailField.getText());
            selected.setTelephone(telephoneField.getText());
            selected.setSexe(sexeBox.getValue());
            selected.setAdresse(new Adresse(rueField.getText(), codePostalField.getText(), villeField.getText(), paysField.getText()));
            selected.setMoyenContact(contactBox.getValue());
            selected.setEtat(etatBox.getValue());
            // Etape 66 bis : persister la modification
            patientService.update(selected);
            // Etape 67 : rafraichir le tableau
            table.refresh();
        });

        // Etape 68 : definir l'action de suppression
        deleteButton.setOnAction(event -> {
            // Etape 69 : recuperer le patient selectionne
            Patient selected = table.getSelectionModel().getSelectedItem();
            // Etape 70 : supprimer si selection existe
            if (selected != null) {
                patientService.delete(selected);
            }
        });

        // Etape 71 : definir l'action de nettoyage
        clearButton.setOnAction(event -> clearForm(nomField, prenomField, naissancePicker, emailField, telephoneField,
                sexeBox, rueField, codePostalField, villeField, paysField, contactBox, etatBox));

        // Etape 72 : definir l'action d'ouverture des dossiers
        dossiersButton.setOnAction(event -> {
            // Etape 73 : recuperer le patient selectionne
            Patient selected = table.getSelectionModel().getSelectedItem();
            // Etape 74 : ouvrir le dossier si selection valide
            if (selected != null) {
                onOpenDossier.accept(selected);
            }
        });

        // Etape 75 : definir l'action d'ouverture des consultations
        consultationsButton.setOnAction(event -> {
            // Etape 76 : recuperer le patient selectionne
            Patient selected = table.getSelectionModel().getSelectedItem();
            // Etape 77 : ouvrir les consultations si selection valide
            if (selected != null) {
                onOpenConsultations.accept(selected);
            }
        });

        // Etape 78 : definir l'action d'ouverture des documents
        documentsButton.setOnAction(event -> {
            // Etape 79 : recuperer le patient selectionne
            Patient selected = table.getSelectionModel().getSelectedItem();
            // Etape 80 : ouvrir les documents si selection valide
            if (selected != null) {
                onOpenDocuments.accept(selected);
            }
        });

        // Etape 81 : ecouter la selection dans le tableau
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {
            // Etape 82 : sortir si pas de selection
            if (selected == null) {
                return;
            }
            // Etape 83 : remplir le formulaire avec la selection
            nomField.setText(selected.getNom());
            prenomField.setText(selected.getPrenom());
            naissancePicker.setValue(selected.getDateNaissance());
            emailField.setText(selected.getEmail());
            telephoneField.setText(selected.getTelephone());
            sexeBox.setValue(selected.getSexe());
            if (selected.getAdresse() != null) {
                // Etape 84 : copier l'adresse dans le formulaire
                rueField.setText(selected.getAdresse().getRue());
                codePostalField.setText(selected.getAdresse().getCodePostal());
                villeField.setText(selected.getAdresse().getVille());
                paysField.setText(selected.getAdresse().getPays());
            }
            contactBox.setValue(selected.getMoyenContact());
            etatBox.setValue(selected.getEtat());
            // Etape 85 : charger les disponibilites
            refreshDisponibilites(selected.getId(), disponibiliteItems);
        });

        // Etape 86 : ecouter les changements de rendez-vous
        dataStore.getRendezVous().addListener((javafx.collections.ListChangeListener.Change<?> change) -> {
            // Etape 87 : recuperer le patient selectionne
            Patient selected = table.getSelectionModel().getSelectedItem();
            // Etape 88 : rafraichir les disponibilites si selection valide
            if (selected != null) {
                refreshDisponibilites(selected.getId(), disponibiliteItems);
            }
        });

        // Etape 89 : construire le formulaire
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
                UiFactory.labeledLine("Etat patient", etatBox),
                UiFactory.labeledLine("Ouvrir dossier", openDossierBox),
                UiFactory.labeledLine("Etat dossier", etatDossierBox),
                buildActionBar(addButton, updateButton, deleteButton, clearButton,
                    dossiersButton, consultationsButton, documentsButton),
                new Label("Disponibilites patient"),
                disponibilites
        );
            // Etape 90 : ajouter du padding au formulaire
        form.setPadding(new Insets(10));
            // Etape 91 : appliquer le style carte
        form.getStyleClass().add("card");

        // Etape 91 bis : rendre le formulaire redimensionnable
        form.setMaxWidth(Double.MAX_VALUE);

        // Etape 91 ter : creer un scroll pour voir tout le formulaire
        ScrollPane formScroll = new ScrollPane(form);
        // Etape 91 quater : activer l'ajustement en largeur
        formScroll.setFitToWidth(true);

        // Etape 92 : construire la colonne de gauche
        VBox left = new VBox(10, tools, table);
        // Etape 93 : ajouter du padding a gauche
        left.setPadding(new Insets(10));
        // Etape 94 : laisser le tableau grandir en hauteur
        VBox.setVgrow(table, Priority.ALWAYS);
        // Etape 95 : creer un SplitPane pour le redimensionnement
        SplitPane split = new SplitPane();
        // Etape 96 : definir la position initiale du separateur
        split.setDividerPositions(0.55);
        // Etape 97 : ajouter la colonne gauche et le formulaire
        split.getItems().addAll(left, formScroll);
        // Etape 98 : placer le SplitPane au centre
        root.setCenter(split);

        // Etape 99 : retourner la racine graphique
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
     * @Objectif Formater la date d'ouverture du dossier.
     * @param patient patient cible
     * @return texte de date
     */
    private String formatDossierDate(Patient patient) {
        // Etape 1 : verifier si le patient a des dossiers
        if (patient.getDossiers().isEmpty()) {
            // Etape 2 : retourner une chaine vide
            return "";
        }
        // Etape 3 : retourner la date du premier dossier
        return String.valueOf(patient.getDossiers().get(0).getDateOuverture());
    }

    /**
     * @Objectif Formater l'etat du dossier.
     * @param patient patient cible
     * @return texte d'etat
     */
    private String formatDossierEtat(Patient patient) {
        // Etape 1 : verifier si le patient a des dossiers
        if (patient.getDossiers().isEmpty()) {
            // Etape 2 : retourner une chaine vide
            return "";
        }
        // Etape 3 : retourner l'etat du premier dossier
        return String.valueOf(patient.getDossiers().get(0).getEtat());
    }

    /**
     * @Objectif Recalculer et afficher les disponibilites.
     * @param patientId identifiant patient
     * @param target liste cible
     */
    private void refreshDisponibilites(String patientId, ObservableList<String> target) {
        // Etape 1 : recuperer les creneaux disponibles
        List<LocalDateTime> slots = rendezVousService.availableSlotsForPatient(patientId);
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
     * @param etatBox champ etat
     */
    private void clearForm(TextField nomField, TextField prenomField, DatePicker naissancePicker, TextField emailField,
                           TextField telephoneField, ComboBox<Sexe> sexeBox, TextField rueField,
                           TextField codePostalField, TextField villeField, TextField paysField,
                           ComboBox<MoyenContact> contactBox, ComboBox<EtatPatient> etatBox) {
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
        // Etape 12 : effacer l'etat
        etatBox.setValue(null);
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: La gestion patient manquait d'un ecran unique.
Cause: Les actions etaient dispersees.
Consequence: Saisie lente et erreurs.
Solution: Un formulaire + tableau synchronise.
Pourquoi: Accelere les operations et les recherches.
Comment: TableView + selection qui remplit le formulaire.
*/
