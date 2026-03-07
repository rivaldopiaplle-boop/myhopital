package com.rivaldo.hopital.ui;

import com.rivaldo.hopital.domain.Consultation;
import com.rivaldo.hopital.domain.DossierMedical;
import com.rivaldo.hopital.domain.Medecin;
import com.rivaldo.hopital.domain.Patient;
import com.rivaldo.hopital.repository.DataStore;
import com.rivaldo.hopital.service.ConsultationService;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Orientation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @Fichier ConsultationView.java
 * @Objectif Ecran de gestion des consultations.
 * @Couche UI
 */
public class ConsultationView implements UiCrudSupport {
    private final DataStore dataStore;
    private final ConsultationService consultationService;
    private FilteredList<Consultation> filtered;
    private ComboBox<String> searchCriteriaBox;
    private TextField searchField;
    private ComboBox<Patient> patientBox;
    private ComboBox<Medecin> medecinBox;
    private ComboBox<DossierMedical> dossierBox;
    private TableView<Consultation> table;

    /**
     * @Objectif Construire l'ecran consultation.
     * @param dataStore stockage en memoire
     * @param consultationService service consultation
     */
    public ConsultationView(DataStore dataStore, ConsultationService consultationService) {
        // Etape 1 : memoriser le stockage
        this.dataStore = dataStore;
        // Etape 2 : memoriser le service consultation
        this.consultationService = consultationService;
    }

    /**
     * @Objectif Construire le layout JavaFX.
     * @return racine graphique
     */
    public Parent createView() {
        // Etape 1 : creer le conteneur principal
        BorderPane root = new BorderPane();

        // Etape 2 : creer le tableau des consultations
        table = new TableView<>();
        // Etape 2 bis : forcer l'ajustement des colonnes
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Etape 3 : creer la colonne ID
        TableColumn<Consultation, String> idCol = new TableColumn<>("ID");
        // Etape 4 : lier la colonne ID au champ id
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        // Etape 5 : creer la colonne patient
        TableColumn<Consultation, String> patientCol = new TableColumn<>("Patient");
        // Etape 6 : lier la colonne patient au champ patientId
        patientCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        // Etape 7 : creer la colonne medecin
        TableColumn<Consultation, String> medecinCol = new TableColumn<>("Medecin");
        // Etape 8 : lier la colonne medecin au champ medecinId
        medecinCol.setCellValueFactory(new PropertyValueFactory<>("medecinId"));
        // Etape 9 : creer la colonne dossier
        TableColumn<Consultation, String> dossierCol = new TableColumn<>("Dossier");
        // Etape 10 : lier la colonne dossier au champ dossierId
        dossierCol.setCellValueFactory(new PropertyValueFactory<>("dossierId"));
        // Etape 11 : creer la colonne date
        TableColumn<Consultation, String> dateCol = new TableColumn<>("Date");
        // Etape 12 : lier la colonne date au champ date
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        // Etape 13 : creer la colonne diagnostic
        TableColumn<Consultation, String> diagnosticCol = new TableColumn<>("Diagnostic");
        // Etape 14 : lier la colonne diagnostic au champ diagnostic
        diagnosticCol.setCellValueFactory(new PropertyValueFactory<>("diagnostic"));
        // Etape 15 : creer la colonne prescription
        TableColumn<Consultation, String> prescriptionCol = new TableColumn<>("Prescription");
        // Etape 16 : lier la colonne prescription au champ prescription
        prescriptionCol.setCellValueFactory(new PropertyValueFactory<>("prescription"));
        // Etape 17 : ajouter toutes les colonnes au tableau
        table.getColumns().addAll(idCol, patientCol, medecinCol, dossierCol, dateCol, diagnosticCol, prescriptionCol);
        // Etape 18 : creer la liste filtree liee au DataStore
        filtered = new FilteredList<>(dataStore.getConsultations(), c -> true);
        // Etape 19 : connecter la liste filtree au tableau
        table.setItems(filtered);

        // Etape 20 : creer le champ de recherche
        searchField = new TextField();
        // Etape 21 : definir le placeholder de recherche
        searchField.setPromptText("Rechercher consultation...");

        // Etape 22 : creer la liste de criteres de recherche
        searchCriteriaBox = new ComboBox<>();
        // Etape 23 : definir les choix de recherche
        searchCriteriaBox.getItems().setAll("Tous", "ID", "Patient", "Medecin", "Dossier", "Date",
            "Diagnostic", "Prescription");
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

        // Etape 39 : creer le picker de date
        DatePicker datePicker = new DatePicker();
        // Etape 40 : creer la liste d'heures
        ComboBox<LocalTime> timeBox = new ComboBox<>(FXCollections.observableArrayList(
                LocalTime.of(9, 0), LocalTime.of(10, 0), LocalTime.of(11, 0),
                LocalTime.of(14, 0), LocalTime.of(15, 0))
        );

        // Etape 41 : creer le champ diagnostic
        TextArea diagnosticArea = new TextArea();
        // Etape 42 : definir le placeholder diagnostic
        diagnosticArea.setPromptText("Diagnostic");
        // Etape 43 : creer le champ prescription
        TextArea prescriptionArea = new TextArea();
        // Etape 44 : definir le placeholder prescription
        prescriptionArea.setPromptText("Prescription");

        // Etape 45 : creer les boutons d'action
        Button addButton = new Button("Enregistrer");
        // Etape 46 : appliquer le style primaire
        addButton.getStyleClass().add("primary");
        Button updateButton = new Button("Modifier");
        Button deleteButton = new Button("Supprimer");
        Button clearButton = new Button("Actualiser");

        // Etape 47 : ecouter le patient pour mettre a jour les dossiers
        patientBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            // Etape 48 : creer une liste locale de dossiers
            ObservableList<DossierMedical> dossiers = FXCollections.observableArrayList();
            // Etape 49 : remplir la liste si un patient est choisi
            if (newValue != null) {
                for (DossierMedical dossier : dataStore.getDossiers()) {
                    if (dossier.getPatientId().equals(newValue.getId())) {
                        dossiers.add(dossier);
                    }
                }
            }
            // Etape 50 : connecter la liste au ComboBox
            dossierBox.setItems(dossiers);
        });

        // Etape 51 : bloquer les dates passees
        datePicker.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date != null && date.isBefore(LocalDate.now())) {
                    setDisable(true);
                }
            }
        });

        // Etape 52 : definir l'action d'ajout
        addButton.setOnAction(event -> {
            // Etape 53 : lire les valeurs du formulaire
            Patient patient = patientBox.getValue();
            Medecin medecin = medecinBox.getValue();
            DossierMedical dossier = dossierBox.getValue();
            LocalDate date = datePicker.getValue();
            LocalTime time = timeBox.getValue();
            // Etape 54 : verifier les champs obligatoires
            if (patient == null || medecin == null || dossier == null || date == null || time == null) {
                return;
            }
            // Etape 55 : creer la consultation
            Consultation consultation = new Consultation(null, patient.getId(), medecin.getId(), dossier.getId(),
                    LocalDateTime.of(date, time), diagnosticArea.getText(), prescriptionArea.getText());
            // Etape 56 : enregistrer la consultation
            consultationService.create(consultation);
        });

        // Etape 57 : definir l'action de modification
        updateButton.setOnAction(event -> {
            // Etape 58 : recuperer la consultation selectionnee
            Consultation selected = table.getSelectionModel().getSelectedItem();
            Patient patient = patientBox.getValue();
            Medecin medecin = medecinBox.getValue();
            DossierMedical dossier = dossierBox.getValue();
            LocalDate date = datePicker.getValue();
            LocalTime time = timeBox.getValue();
            // Etape 59 : verifier les champs
            if (selected == null || patient == null || medecin == null || dossier == null || date == null || time == null) {
                return;
            }
            // Etape 60 : copier les champs vers l'objet
            selected.setPatientId(patient.getId());
            selected.setMedecinId(medecin.getId());
            selected.setDossierId(dossier.getId());
            selected.setDate(LocalDateTime.of(date, time));
            selected.setDiagnostic(diagnosticArea.getText());
            selected.setPrescription(prescriptionArea.getText());
            // Etape 61 : enregistrer la mise a jour
            consultationService.update(selected);
            // Etape 62 : rafraichir le tableau
            table.refresh();
        });

        // Etape 63 : definir l'action de suppression
        deleteButton.setOnAction(event -> {
            // Etape 64 : recuperer la consultation selectionnee
            Consultation selected = table.getSelectionModel().getSelectedItem();
            // Etape 65 : supprimer si selection existe
            if (selected != null) {
                consultationService.delete(selected);
            }
        });

        // Etape 66 : definir l'action de nettoyage
        clearButton.setOnAction(event -> {
            // Etape 67 : remettre le formulaire a zero
            patientBox.setValue(null);
            medecinBox.setValue(null);
            dossierBox.setItems(FXCollections.observableArrayList());
            dossierBox.setValue(null);
            datePicker.setValue(null);
            timeBox.setValue(null);
            diagnosticArea.clear();
            prescriptionArea.clear();
        });

        // Etape 68 : ecouter la selection dans le tableau
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {
            // Etape 69 : sortir si pas de selection
            if (selected == null) {
                return;
            }
            // Etape 70 : remplir le formulaire a partir de la selection
            patientBox.setValue(resolvePatient(selected.getPatientId()));
            medecinBox.setValue(resolveMedecin(selected.getMedecinId()));
            dossierBox.setItems(FXCollections.observableArrayList(resolveDossiers(selected.getPatientId())));
            dossierBox.setValue(resolveDossier(selected.getDossierId()));
            datePicker.setValue(selected.getDate() != null ? selected.getDate().toLocalDate() : null);
            timeBox.setValue(selected.getDate() != null ? selected.getDate().toLocalTime() : null);
            diagnosticArea.setText(selected.getDiagnostic());
            prescriptionArea.setText(selected.getPrescription());
        });

        // Etape 71 : construire la barre d'outils
        VBox tools = new VBox(8,
                UiFactory.labeledLine("Recherche", searchField),
            UiFactory.labeledLine("Trier par", searchCriteriaBox)
        );
        // Etape 72 : ajouter du padding a la barre d'outils
        tools.setPadding(new Insets(10));

        // Etape 73 : construire le formulaire
        VBox form = new VBox(8,
                UiFactory.labeledLine("Patient", patientBox),
                UiFactory.labeledLine("Medecin", medecinBox),
                UiFactory.labeledLine("Dossier", dossierBox),
                UiFactory.labeledLine("Date", datePicker),
                UiFactory.labeledLine("Heure", timeBox),
                UiFactory.labeledLine("Diagnostic", diagnosticArea),
                UiFactory.labeledLine("Prescription", prescriptionArea),
                buildActionBar(addButton, updateButton, deleteButton, clearButton)
        );
        // Etape 74 : ajouter du padding au formulaire
        form.setPadding(new Insets(10));
        // Etape 75 : appliquer le style carte
        form.getStyleClass().add("card");

        // Etape 75 bis : rendre le formulaire redimensionnable
        form.setMaxWidth(Double.MAX_VALUE);

        // Etape 75 ter : creer un scroll pour voir tout le formulaire
        ScrollPane formScroll = new ScrollPane(form);
        // Etape 75 quater : activer l'ajustement en largeur
        formScroll.setFitToWidth(true);

        // Etape 76 : creer un SplitPane vertical pour le redimensionnement
        SplitPane split = new SplitPane();
        // Etape 77 : utiliser une orientation verticale
        split.setOrientation(Orientation.VERTICAL);
        // Etape 78 : definir la position initiale du separateur
        split.setDividerPositions(0.4);
        // Etape 79 : ajouter les outils, le formulaire et le tableau
        split.getItems().addAll(tools, formScroll, table);
        // Etape 80 : placer le SplitPane au centre
        root.setCenter(split);

        // Etape 81 : retourner la racine graphique
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
            searchCriteriaBox.setValue("Patient");
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
            searchCriteriaBox.setValue("Medecin");
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
        filtered.setPredicate(consultation -> {
            boolean matchKeyword;
            if (keyword.isEmpty()) {
                matchKeyword = true;
            } else if (criterion == null || "Tous".equals(criterion)) {
                matchKeyword = consultation.getPatientId().toLowerCase().contains(keyword)
                        || consultation.getMedecinId().toLowerCase().contains(keyword)
                        || consultation.getDossierId().toLowerCase().contains(keyword)
                        || (consultation.getDiagnostic() != null
                            && consultation.getDiagnostic().toLowerCase().contains(keyword));
            } else {
                matchKeyword = switch (criterion) {
                    case "ID" -> consultation.getId().toLowerCase().contains(keyword);
                    case "Patient" -> consultation.getPatientId().toLowerCase().contains(keyword);
                    case "Medecin" -> consultation.getMedecinId().toLowerCase().contains(keyword);
                    case "Dossier" -> consultation.getDossierId().toLowerCase().contains(keyword);
                    case "Date" -> consultation.getDate() != null
                        && consultation.getDate().toString().toLowerCase().contains(keyword);
                    case "Diagnostic" -> consultation.getDiagnostic() != null
                        && consultation.getDiagnostic().toLowerCase().contains(keyword);
                    case "Prescription" -> consultation.getPrescription() != null
                        && consultation.getPrescription().toLowerCase().contains(keyword);
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
Probleme: Consultations et documents etaient confondus.
Cause: Un seul ecran pour les deux types.
Consequence: Perte de precision clinique.
Solution: Separer les consultations des documents medicaux.
Pourquoi: Clarifier les actes et le suivi.
Comment: Deux ecrans distincts relies au dossier.
*/
