package com.rivaldo.hopital.ui;

import com.rivaldo.hopital.domain.DossierMedical;
import com.rivaldo.hopital.domain.EtatDossier;
import com.rivaldo.hopital.domain.Patient;
import com.rivaldo.hopital.repository.DataStore;
import com.rivaldo.hopital.service.DossierMedicalService;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Orientation;
import javafx.beans.property.SimpleStringProperty;

/**
 * @Fichier DossierMedicalView.java
 * @Objectif Ecran de gestion des dossiers avec filtres.
 * @Couche UI
 */
public class DossierMedicalView implements UiCrudSupport {
    private final DataStore dataStore;
    private final DossierMedicalService dossierMedicalService;
    private FilteredList<DossierMedical> filtered;
    private TableView<DossierMedical> table;
    private TextField searchField;
    private ComboBox<String> searchCriteriaBox;
    private ComboBox<Patient> patientBox;
    private DatePicker datePicker;
    private ComboBox<EtatDossier> etatBox;

    /**
     * @Objectif Construire l'ecran dossier.
     * @param dataStore stockage en memoire
     * @param dossierMedicalService service dossier
     */
    public DossierMedicalView(DataStore dataStore, DossierMedicalService dossierMedicalService) {
        // Etape 1 : memoriser le stockage
        this.dataStore = dataStore;
        // Etape 2 : memoriser le service dossier
        this.dossierMedicalService = dossierMedicalService;
    }

    /**
     * @Objectif Construire le layout JavaFX.
     * @return racine graphique
     */
    public Parent createView() {
        // Etape 1 : creer le conteneur principal
        BorderPane root = new BorderPane();

        // Etape 2 : creer le tableau des dossiers
        table = new TableView<>();
        // Etape 2 bis : forcer l'ajustement des colonnes
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Etape 3 : creer la colonne ID
        TableColumn<DossierMedical, String> idCol = new TableColumn<>("ID");
        // Etape 4 : lier la colonne ID au champ id
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Etape 5 : creer la colonne patient
        TableColumn<DossierMedical, String> patientCol = new TableColumn<>("Patient");
        // Etape 6 : lier la colonne patient au champ patientId
        patientCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));

        // Etape 7 : creer la colonne date ouverture
        TableColumn<DossierMedical, String> dateCol = new TableColumn<>("Date ouverture");
        // Etape 8 : lier la colonne date ouverture au champ dateOuverture
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateOuverture"));

        // Etape 9 : creer la colonne etat
        TableColumn<DossierMedical, String> etatCol = new TableColumn<>("Etat");
        // Etape 10 : lier la colonne etat au champ etat
        etatCol.setCellValueFactory(new PropertyValueFactory<>("etat"));

        // Etape 11 : creer la colonne consultations
        TableColumn<DossierMedical, String> consultationsCol = new TableColumn<>("Consultations");
        // Etape 12 : afficher le nombre de consultations
        consultationsCol.setCellValueFactory(cell -> new SimpleStringProperty(
            String.valueOf(countConsultationsForDossier(cell.getValue().getId()))));

        // Etape 13 : creer la colonne documents
        TableColumn<DossierMedical, String> documentsCol = new TableColumn<>("Documents");
        // Etape 14 : afficher le nombre de documents
        documentsCol.setCellValueFactory(cell -> new SimpleStringProperty(
            String.valueOf(countDocumentsForDossier(cell.getValue().getId()))));

        // Etape 15 : ajouter les colonnes au tableau
        table.getColumns().addAll(idCol, patientCol, dateCol, etatCol, consultationsCol, documentsCol);

        // Etape 16 : creer la liste filtree liee au DataStore
        filtered = new FilteredList<>(dataStore.getDossiers(), dossier -> true);
        // Etape 17 : connecter la liste filtree au tableau
        table.setItems(filtered);

        // Etape 17 bis : rafraichir les compteurs lors des changements
        dataStore.getConsultations().addListener((javafx.collections.ListChangeListener.Change<?> change) ->
            table.refresh());
        dataStore.getDocuments().addListener((javafx.collections.ListChangeListener.Change<?> change) ->
            table.refresh());

        // Etape 18 : creer le champ de recherche
        searchField = new TextField();
        // Etape 19 : definir le placeholder de recherche
        searchField.setPromptText("Rechercher dossier...");

        // Etape 20 : creer la liste de criteres de recherche
        searchCriteriaBox = new ComboBox<>();
        // Etape 21 : definir les choix de recherche
        searchCriteriaBox.getItems().setAll("Tous", "ID", "Patient ID", "Date ouverture", "Etat",
            "Consultations", "Documents");
        // Etape 22 : definir le critere par defaut
        searchCriteriaBox.setValue("Tous");

        // Etape 23 : ecouter les changements de recherche
        searchField.textProperty().addListener((obs, oldValue, newValue) ->
            applyFilters(searchField.getText(), searchCriteriaBox.getValue()));
        // Etape 24 : ecouter les changements de critere
        searchCriteriaBox.valueProperty().addListener((obs, oldValue, newValue) ->
            applyFilters(searchField.getText(), newValue));

        // Etape 25 : creer les champs du formulaire
        patientBox = new ComboBox<>(dataStore.getPatients());
        patientBox.setConverter(UiTextConverters.patientConverter());
        datePicker = new DatePicker();
        etatBox = new ComboBox<>(FXCollections.observableArrayList(EtatDossier.values()));

        // Etape 26 : creer les boutons d'action
        Button addButton = new Button("Enregistrer");
        addButton.getStyleClass().add("primary");
        Button updateButton = new Button("Modifier");
        Button deleteButton = new Button("Supprimer");
        Button clearButton = new Button("Actualiser");

        // Etape 27 : definir l'action d'ajout
        addButton.setOnAction(event -> {
            Patient patient = patientBox.getValue();
            if (patient == null || datePicker.getValue() == null || etatBox.getValue() == null) {
                return;
            }
            dossierMedicalService.createForPatient(patient.getId(), datePicker.getValue(), etatBox.getValue());
            clearForm();
        });

        // Etape 28 : definir l'action de modification
        updateButton.setOnAction(event -> {
            DossierMedical selected = table.getSelectionModel().getSelectedItem();
            Patient patient = patientBox.getValue();
            if (selected == null || patient == null || datePicker.getValue() == null || etatBox.getValue() == null) {
                return;
            }
            selected.setPatientId(patient.getId());
            selected.setDateOuverture(datePicker.getValue());
            selected.setEtat(etatBox.getValue());
            dossierMedicalService.update(selected);
            table.refresh();
        });

        // Etape 29 : definir l'action de suppression
        deleteButton.setOnAction(event -> {
            DossierMedical selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                dossierMedicalService.delete(selected);
            }
        });

        // Etape 30 : definir l'action de nettoyage
        clearButton.setOnAction(event -> clearForm());

        // Etape 31 : ecouter la selection dans le tableau
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {
            if (selected == null) {
                return;
            }
            patientBox.setValue(resolvePatient(selected.getPatientId()));
            datePicker.setValue(selected.getDateOuverture());
            etatBox.setValue(selected.getEtat());
        });

        // Etape 32 : construire la barre d'outils
        VBox tools = new VBox(8,
            UiFactory.labeledLine("Recherche", searchField),
            UiFactory.labeledLine("Trier par", searchCriteriaBox)
        );
        tools.setPadding(new Insets(10));

        // Etape 33 : construire le formulaire
        VBox form = new VBox(8,
            UiFactory.labeledLine("Patient", patientBox),
            UiFactory.labeledLine("Date ouverture", datePicker),
            UiFactory.labeledLine("Etat", etatBox),
            buildActionBar(addButton, updateButton, deleteButton, clearButton)
        );
        form.setPadding(new Insets(10));
        form.getStyleClass().add("card");
        form.setMaxWidth(Double.MAX_VALUE);

        // Etape 34 : creer un scroll pour voir tout le formulaire
        ScrollPane formScroll = new ScrollPane(form);
        formScroll.setFitToWidth(true);

        // Etape 35 : creer un SplitPane vertical pour le redimensionnement
        SplitPane split = new SplitPane();
        split.setOrientation(Orientation.VERTICAL);
        split.setDividerPositions(0.35);
        split.getItems().addAll(tools, formScroll, table);
        root.setCenter(split);

        // Etape 36 : retourner la racine graphique
        return root;
    }

    /**
     * @Objectif Prefiltrer par patient.
     * @param patientId identifiant patient
     */
    public void setPatientFilter(String patientId) {
        // Etape 1 : preselectionner le patient dans le formulaire
        if (patientBox != null) {
            patientBox.setValue(resolvePatient(patientId));
        }
        // Etape 2 : prefiltrer la liste via la recherche
        if (searchCriteriaBox != null && searchField != null) {
            searchCriteriaBox.setValue("Patient ID");
            searchField.setText(patientId != null ? patientId : "");
            applyFilters(searchField.getText(), searchCriteriaBox.getValue());
        }
    }

    /**
     * @Objectif Appliquer les filtres sur la liste.
     */
    private void applyFilters(String keywordValue, String criterion) {
        // Etape 1 : normaliser le mot cle
        String keyword = keywordValue != null ? keywordValue.trim().toLowerCase() : "";
        // Etape 2 : appliquer le predicat compose
        filtered.setPredicate(dossier -> {
            if (keyword.isEmpty()) {
                return true;
            }
            if (criterion == null || "Tous".equals(criterion)) {
                return dossier.getId().toLowerCase().contains(keyword)
                    || dossier.getPatientId().toLowerCase().contains(keyword)
                    || (dossier.getDateOuverture() != null
                        && dossier.getDateOuverture().toString().toLowerCase().contains(keyword))
                    || (dossier.getEtat() != null
                        && dossier.getEtat().name().toLowerCase().contains(keyword))
                    || String.valueOf(countConsultationsForDossier(dossier.getId())).contains(keyword)
                    || String.valueOf(countDocumentsForDossier(dossier.getId())).contains(keyword);
            }
            return switch (criterion) {
                case "ID" -> dossier.getId().toLowerCase().contains(keyword);
                case "Patient ID" -> dossier.getPatientId().toLowerCase().contains(keyword);
                case "Date ouverture" -> dossier.getDateOuverture() != null
                    && dossier.getDateOuverture().toString().toLowerCase().contains(keyword);
                case "Etat" -> dossier.getEtat() != null
                    && dossier.getEtat().name().toLowerCase().contains(keyword);
                case "Consultations" -> String.valueOf(countConsultationsForDossier(dossier.getId()))
                    .contains(keyword);
                case "Documents" -> String.valueOf(countDocumentsForDossier(dossier.getId())).contains(keyword);
                default -> false;
            };
        });
    }

    /**
     * @Objectif Compter les consultations d'un dossier.
     * @param dossierId identifiant dossier
     * @return nombre de consultations
     */
    private long countConsultationsForDossier(String dossierId) {
        // Etape 1 : sortir si l'id est absent
        if (dossierId == null) {
            return 0;
        }
        // Etape 2 : compter les consultations liees au dossier
        return dataStore.getConsultations().stream()
            .filter(consultation -> dossierId.equals(consultation.getDossierId()))
            .count();
    }

    /**
     * @Objectif Compter les documents d'un dossier.
     * @param dossierId identifiant dossier
     * @return nombre de documents
     */
    private long countDocumentsForDossier(String dossierId) {
        // Etape 1 : sortir si l'id est absent
        if (dossierId == null) {
            return 0;
        }
        // Etape 2 : compter les documents lies au dossier
        return dataStore.getDocuments().stream()
            .filter(document -> dossierId.equals(document.getDossierId()))
            .count();
    }

    /**
     * @Objectif Vider le formulaire.
     */
    private void clearForm() {
        // Etape 1 : vider la selection patient
        patientBox.setValue(null);
        // Etape 2 : vider la date d'ouverture
        datePicker.setValue(null);
        // Etape 3 : vider l'etat
        etatBox.setValue(null);
    }

    /**
     * @Objectif Resoudre un patient par identifiant.
     * @param patientId identifiant patient
     * @return patient trouve ou null
     */
    private Patient resolvePatient(String patientId) {
        // Etape 1 : sortir si l'id est absent
        if (patientId == null) {
            return null;
        }
        // Etape 2 : rechercher le patient par id
        return dataStore.getPatients().stream()
            .filter(patient -> patient.getId().equals(patientId))
            .findFirst()
            .orElse(null);
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Les dossiers n'etaient pas filtrables.
Cause: Absence de champs de filtre.
Consequence: Recherche lente.
Solution: Ajout de filtres par dossier, patient et etat.
Pourquoi: Retrouver un dossier rapidement.
Comment: FilteredList avec predicat compose.
*/
