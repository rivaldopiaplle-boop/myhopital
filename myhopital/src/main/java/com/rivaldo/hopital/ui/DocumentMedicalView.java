package com.rivaldo.hopital.ui;

import com.rivaldo.hopital.domain.DocumentMedical;
import com.rivaldo.hopital.domain.DocumentType;
import com.rivaldo.hopital.domain.DossierMedical;
import com.rivaldo.hopital.domain.Medecin;
import com.rivaldo.hopital.domain.Patient;
import com.rivaldo.hopital.repository.DataStore;
import com.rivaldo.hopital.service.DocumentMedicalService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Orientation;

import java.time.LocalDate;

/**
 * @Fichier DocumentMedicalView.java
 * @Objectif Ecran de gestion des documents medicaux.
 * @Couche UI
 */
public class DocumentMedicalView implements UiCrudSupport {
    private final DataStore dataStore;
    private final DocumentMedicalService documentMedicalService;
    private FilteredList<DocumentMedical> filtered;
    private ComboBox<String> searchCriteriaBox;
    private TextField searchField;
    private ComboBox<Patient> patientBox;
    private ComboBox<Medecin> medecinBox;
    private ComboBox<DossierMedical> dossierBox;
    private TableView<DocumentMedical> table;

    /**
     * @Objectif Construire l'ecran document medical.
     * @param dataStore stockage en memoire
     * @param documentMedicalService service document
     */
    public DocumentMedicalView(DataStore dataStore, DocumentMedicalService documentMedicalService) {
        // Etape 1 : memoriser le stockage
        this.dataStore = dataStore;
        // Etape 2 : memoriser le service document medical
        this.documentMedicalService = documentMedicalService;
    }

    /**
     * @Objectif Construire le layout JavaFX.
     * @return racine graphique
     */
    public Parent createView() {
        // Etape 1 : creer le conteneur principal
        BorderPane root = new BorderPane();

        // Etape 2 : creer le tableau des documents
        table = new TableView<>();
        // Etape 2 bis : forcer l'ajustement des colonnes
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Etape 3 : creer la colonne ID
        TableColumn<DocumentMedical, String> idCol = new TableColumn<>("ID");
        // Etape 4 : lier la colonne ID au champ id
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        // Etape 5 : creer la colonne patient ID
        TableColumn<DocumentMedical, String> patientCol = new TableColumn<>("Patient ID");
        // Etape 6 : lier la colonne patient ID au champ patientId
        patientCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        // Etape 7 : creer la colonne medecin ID
        TableColumn<DocumentMedical, String> medecinCol = new TableColumn<>("Medecin ID");
        // Etape 8 : lier la colonne medecin ID au champ medecinId
        medecinCol.setCellValueFactory(new PropertyValueFactory<>("medecinId"));
        // Etape 9 : creer la colonne dossier ID
        TableColumn<DocumentMedical, String> dossierCol = new TableColumn<>("Dossier ID");
        // Etape 10 : lier la colonne dossier ID au champ dossierId
        dossierCol.setCellValueFactory(new PropertyValueFactory<>("dossierId"));
        // Etape 11 : creer la colonne nom
        TableColumn<DocumentMedical, String> nomCol = new TableColumn<>("Nom");
        // Etape 12 : lier la colonne nom au champ nom
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        // Etape 13 : creer la colonne type
        TableColumn<DocumentMedical, String> typeCol = new TableColumn<>("Type");
        // Etape 14 : lier la colonne type au champ type
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        // Etape 15 : creer la colonne date ajout
        TableColumn<DocumentMedical, String> dateCol = new TableColumn<>("Date ajout");
        // Etape 16 : lier la colonne date ajout au champ dateAjout
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateAjout"));
        // Etape 17 : ajouter toutes les colonnes au tableau
        table.getColumns().addAll(idCol, patientCol, medecinCol, dossierCol, nomCol, typeCol, dateCol);
        // Etape 18 : creer la liste filtree liee au DataStore
        filtered = new FilteredList<>(dataStore.getDocuments(), d -> true);
        // Etape 19 : connecter la liste filtree au tableau
        table.setItems(filtered);

        // Etape 20 : creer le champ de recherche
        searchField = new TextField();
        // Etape 21 : definir le placeholder de recherche
        searchField.setPromptText("Rechercher document...");

        // Etape 22 : creer la liste de criteres de recherche
        searchCriteriaBox = new ComboBox<>();
        // Etape 23 : definir les choix de recherche
        searchCriteriaBox.getItems().setAll("Tous", "ID", "Patient ID", "Medecin ID", "Dossier ID", "Nom",
            "Type", "Date ajout");
        // Etape 24 : definir le critere par defaut
        searchCriteriaBox.setValue("Tous");

        // Etape 24 : ecouter les changements de recherche
        searchField.textProperty().addListener((obs, oldValue, newValue) ->
            applyFilters(searchField.getText(), searchCriteriaBox.getValue()));
        // Etape 25 : ecouter les changements de critere
        searchCriteriaBox.valueProperty().addListener((obs, oldValue, newValue) ->
            applyFilters(searchField.getText(), newValue));

        // Etape 32 : creer les champs de saisie
        patientBox = new ComboBox<>(dataStore.getPatients());
        // Etape 33 : definir le convertisseur patient
        patientBox.setConverter(UiTextConverters.patientConverter());
        medecinBox = new ComboBox<>(dataStore.getMedecins());
        // Etape 34 : definir le convertisseur medecin
        medecinBox.setConverter(UiTextConverters.medecinConverter());

        // Etape 35 : creer la liste de dossiers
        dossierBox = new ComboBox<>();
        // Etape 36 : definir le convertisseur dossier
        dossierBox.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(DossierMedical dossier) {
                // Etape 37 : afficher l'id du dossier
                return dossier != null ? dossier.getId() : "";
            }

            @Override
            public DossierMedical fromString(String string) {
                // Etape 38 : conversion inverse non utilisee
                return null;
            }
        });

        // Etape 39 : creer la liste des types de document
        ComboBox<DocumentType> typeBox = new ComboBox<>(FXCollections.observableArrayList(DocumentType.values()));
        // Etape 40 : creer le champ nom
        TextField nomField = new TextField();
        // Etape 41 : creer le picker de date
        DatePicker datePicker = new DatePicker(LocalDate.now());

        // Etape 42 : creer les boutons d'action
        Button addButton = new Button("Enregistrer");
        // Etape 43 : appliquer le style primaire
        addButton.getStyleClass().add("primary");
        Button updateButton = new Button("Modifier");
        Button deleteButton = new Button("Supprimer");
        Button clearButton = new Button("Actualiser");

        // Etape 44 : ecouter le patient pour mettre a jour les dossiers
        patientBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            // Etape 45 : creer une liste locale de dossiers
            ObservableList<DossierMedical> dossiers = FXCollections.observableArrayList();
            // Etape 46 : remplir la liste si un patient est choisi
            if (newValue != null) {
                for (DossierMedical dossier : dataStore.getDossiers()) {
                    if (dossier.getPatientId().equals(newValue.getId())) {
                        dossiers.add(dossier);
                    }
                }
            }
            // Etape 47 : connecter la liste au ComboBox
            dossierBox.setItems(dossiers);
        });

        // Etape 48 : bloquer les dates passees
        datePicker.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date != null && date.isBefore(LocalDate.now())) {
                    setDisable(true);
                }
            }
        });

        // Etape 49 : definir l'action d'ajout
        addButton.setOnAction(event -> {
            // Etape 50 : lire les valeurs du formulaire
            Patient patient = patientBox.getValue();
            Medecin medecin = medecinBox.getValue();
            DossierMedical dossier = dossierBox.getValue();
            // Etape 51 : verifier les champs obligatoires
            if (patient == null || medecin == null || dossier == null || typeBox.getValue() == null) {
                return;
            }
            // Etape 52 : creer le document medical
            DocumentMedical document = new DocumentMedical(null, patient.getId(), medecin.getId(), dossier.getId(),
                    nomField.getText(), typeBox.getValue(), datePicker.getValue());
            // Etape 53 : enregistrer le document
            documentMedicalService.create(document);
        });

        // Etape 54 : definir l'action de modification
        updateButton.setOnAction(event -> {
            // Etape 55 : recuperer le document selectionne
            DocumentMedical selected = table.getSelectionModel().getSelectedItem();
            Patient patient = patientBox.getValue();
            Medecin medecin = medecinBox.getValue();
            DossierMedical dossier = dossierBox.getValue();
            // Etape 56 : verifier les champs
            if (selected == null || patient == null || medecin == null || dossier == null || typeBox.getValue() == null) {
                return;
            }
            // Etape 57 : copier les champs vers l'objet
            selected.setPatientId(patient.getId());
            selected.setMedecinId(medecin.getId());
            selected.setDossierId(dossier.getId());
            selected.setNom(nomField.getText());
            selected.setType(typeBox.getValue());
            selected.setDateAjout(datePicker.getValue());
            // Etape 58 : enregistrer la mise a jour
            documentMedicalService.update(selected);
            // Etape 59 : rafraichir le tableau
            table.refresh();
        });

        // Etape 60 : definir l'action de suppression
        deleteButton.setOnAction(event -> {
            // Etape 61 : recuperer le document selectionne
            DocumentMedical selected = table.getSelectionModel().getSelectedItem();
            // Etape 62 : supprimer si selection existe
            if (selected != null) {
                documentMedicalService.delete(selected);
            }
        });

        // Etape 63 : definir l'action de nettoyage
        clearButton.setOnAction(event -> {
            // Etape 64 : remettre le formulaire a zero
            patientBox.setValue(null);
            medecinBox.setValue(null);
            dossierBox.setItems(FXCollections.observableArrayList());
            dossierBox.setValue(null);
            nomField.clear();
            typeBox.setValue(null);
            datePicker.setValue(LocalDate.now());
        });

        // Etape 65 : ecouter la selection dans le tableau
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {
            // Etape 66 : sortir si pas de selection
            if (selected == null) {
                return;
            }
            // Etape 67 : remplir le formulaire a partir de la selection
            patientBox.setValue(resolvePatient(selected.getPatientId()));
            medecinBox.setValue(resolveMedecin(selected.getMedecinId()));
            dossierBox.setItems(FXCollections.observableArrayList(resolveDossiers(selected.getPatientId())));
            dossierBox.setValue(resolveDossier(selected.getDossierId()));
            nomField.setText(selected.getNom());
            typeBox.setValue(selected.getType());
            datePicker.setValue(selected.getDateAjout());
        });

        // Etape 68 : construire la barre d'outils
        VBox tools = new VBox(8,
                UiFactory.labeledLine("Recherche", searchField),
            UiFactory.labeledLine("Trier par", searchCriteriaBox)
        );
        // Etape 69 : ajouter du padding a la barre d'outils
        tools.setPadding(new Insets(10));

        // Etape 70 : construire le formulaire
        VBox form = new VBox(8,
                UiFactory.labeledLine("Patient", patientBox),
                UiFactory.labeledLine("Medecin", medecinBox),
                UiFactory.labeledLine("Dossier", dossierBox),
                UiFactory.labeledLine("Nom", nomField),
                UiFactory.labeledLine("Type", typeBox),
                UiFactory.labeledLine("Date ajout", datePicker),
                buildActionBar(addButton, updateButton, deleteButton, clearButton)
        );
        // Etape 71 : ajouter du padding au formulaire
        form.setPadding(new Insets(10));
        // Etape 72 : appliquer le style carte
        form.getStyleClass().add("card");

        // Etape 72 bis : rendre le formulaire redimensionnable
        form.setMaxWidth(Double.MAX_VALUE);

        // Etape 72 ter : creer un scroll pour voir tout le formulaire
        ScrollPane formScroll = new ScrollPane(form);
        // Etape 72 quater : activer l'ajustement en largeur
        formScroll.setFitToWidth(true);

        // Etape 73 : creer un SplitPane vertical pour le redimensionnement
        SplitPane split = new SplitPane();
        // Etape 74 : utiliser une orientation verticale
        split.setOrientation(Orientation.VERTICAL);
        // Etape 75 : definir la position initiale du separateur
        split.setDividerPositions(0.4);
        // Etape 76 : ajouter les outils, le formulaire et le tableau
        split.getItems().addAll(tools, formScroll, table);
        // Etape 77 : placer le SplitPane au centre
        root.setCenter(split);

        // Etape 78 : retourner la racine graphique
        return root;
    }

    /**
     * @Objectif Prefiltrer par patient.
     * @param patientId identifiant patient
     */
    public void setPatientFilter(String patientId) {
        // Etape 1 : trouver le patient par id
        Patient patient = resolvePatient(patientId);
        // Etape 2 : preselectionner le patient dans le formulaire
        if (patientBox != null) {
            patientBox.setValue(patient);
        }
        // Etape 3 : actualiser la liste de dossiers du patient
        if (dossierBox != null) {
            dossierBox.setItems(FXCollections.observableArrayList(resolveDossiers(patientId)));
        }
        // Etape 4 : prefiltrer la liste si la recherche est disponible
        if (searchCriteriaBox != null && searchField != null) {
            searchCriteriaBox.setValue("Patient ID");
            searchField.setText(patientId != null ? patientId : "");
            applyFilters(searchField.getText(), searchCriteriaBox.getValue());
        }
    }

    /**
     * @Objectif Prefiltrer par medecin.
     * @param medecinId identifiant medecin
     */
    public void setMedecinFilter(String medecinId) {
        // Etape 1 : trouver le medecin par id
        Medecin medecin = resolveMedecin(medecinId);
        // Etape 2 : preselectionner le medecin dans le formulaire
        if (medecinBox != null) {
            medecinBox.setValue(medecin);
        }
        // Etape 3 : prefiltrer la liste via la recherche
        if (searchCriteriaBox != null && searchField != null) {
            searchCriteriaBox.setValue("Medecin ID");
            searchField.setText(medecinId != null ? medecinId : "");
            applyFilters(searchField.getText(), searchCriteriaBox.getValue());
        }
    }

    /**
     * @Objectif Appliquer les filtres de recherche.
     * @param keywordValue mot cle
     */
    private void applyFilters(String keywordValue, String criterion) {
        // Etape 1 : normaliser le mot cle
        String keyword = keywordValue != null ? keywordValue.trim().toLowerCase() : "";
        // Etape 2 : appliquer le predicat compose
        filtered.setPredicate(document -> {
            boolean matchKeyword;
            if (keyword.isEmpty()) {
                matchKeyword = true;
            } else if (criterion == null || "Tous".equals(criterion)) {
                matchKeyword = document.getPatientId().toLowerCase().contains(keyword)
                        || document.getMedecinId().toLowerCase().contains(keyword)
                        || document.getDossierId().toLowerCase().contains(keyword)
                        || (document.getNom() != null && document.getNom().toLowerCase().contains(keyword));
            } else {
                matchKeyword = switch (criterion) {
                    case "ID" -> document.getId().toLowerCase().contains(keyword);
                    case "Patient ID" -> document.getPatientId().toLowerCase().contains(keyword);
                    case "Medecin ID" -> document.getMedecinId().toLowerCase().contains(keyword);
                    case "Dossier ID" -> document.getDossierId().toLowerCase().contains(keyword);
                    case "Nom" -> document.getNom() != null && document.getNom().toLowerCase().contains(keyword);
                    case "Type" -> document.getType() != null
                        && document.getType().name().toLowerCase().contains(keyword);
                    case "Date ajout" -> document.getDateAjout() != null
                        && document.getDateAjout().toString().toLowerCase().contains(keyword);
                    default -> false;
                };
            }
            return matchKeyword;
        });
    }

    /**
     * @Objectif Resoudre un patient par identifiant.
     * @param patientId identifiant patient
     * @return patient trouve ou null
     */
    private Patient resolvePatient(String patientId) {
        // Etape 1 : rechercher le patient par id
        return dataStore.getPatients().stream()
                .filter(patient -> patient.getId().equals(patientId))
                .findFirst()
                .orElse(null);
    }

    /**
     * @Objectif Resoudre un medecin par identifiant.
     * @param medecinId identifiant medecin
     * @return medecin trouve ou null
     */
    private Medecin resolveMedecin(String medecinId) {
        // Etape 1 : rechercher le medecin par id
        return dataStore.getMedecins().stream()
                .filter(medecin -> medecin.getId().equals(medecinId))
                .findFirst()
                .orElse(null);
    }

    /**
     * @Objectif Recuperer les dossiers d'un patient.
     * @param patientId identifiant patient
     * @return liste de dossiers
     */
    private java.util.List<DossierMedical> resolveDossiers(String patientId) {
        // Etape 1 : creer la liste resultat
        java.util.List<DossierMedical> dossiers = new java.util.ArrayList<>();
        // Etape 2 : parcourir tous les dossiers
        for (DossierMedical dossier : dataStore.getDossiers()) {
            // Etape 3 : garder les dossiers du patient
            if (dossier.getPatientId().equals(patientId)) {
                dossiers.add(dossier);
            }
        }
        // Etape 4 : retourner la liste
        return dossiers;
    }

    /**
     * @Objectif Resoudre un dossier par identifiant.
     * @param dossierId identifiant dossier
     * @return dossier trouve ou null
     */
    private DossierMedical resolveDossier(String dossierId) {
        // Etape 1 : rechercher le dossier par id
        return dataStore.getDossiers().stream()
                .filter(dossier -> dossier.getId().equals(dossierId))
                .findFirst()
                .orElse(null);
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Les documents n'etaient pas classes.
Cause: Pas d'ecran dedie ni de type.
Consequence: Difficile de retrouver un document.
Solution: Un ecran document avec type et date.
Pourquoi: Structurer les preuves medicales.
Comment: Formulaire + table avec filtre patient/dossier.
*/
