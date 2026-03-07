package com.rivaldo.hopital.ui;

import com.rivaldo.hopital.domain.Medecin;
import com.rivaldo.hopital.domain.Patient;
import com.rivaldo.hopital.domain.RendezVous;
import com.rivaldo.hopital.exception.ValidationException;
import com.rivaldo.hopital.repository.DataStore;
import com.rivaldo.hopital.service.RendezVousService;
import com.rivaldo.hopital.util.DateTimeUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Orientation;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * @Fichier RendezVousView.java
 * @Objectif Ecran de gestion des rendez-vous.
 * @Couche UI
 */
public class RendezVousView implements UiCrudSupport {
    private final DataStore dataStore;
    private final RendezVousService rendezVousService;
    private FilteredList<RendezVous> filtered;
    private TableView<RendezVous> table;

    /**
     * @Objectif Construire l'ecran des rendez-vous.
     * @param dataStore stockage en memoire
     * @param rendezVousService service rendez-vous
     */
    public RendezVousView(DataStore dataStore, RendezVousService rendezVousService) {
        // Etape 1 : memoriser le stockage
        this.dataStore = dataStore;
        // Etape 2 : memoriser le service rendez-vous
        this.rendezVousService = rendezVousService;
    }

    /**
     * @Objectif Construire le layout JavaFX.
     * @return racine graphique
     */
    public Parent createView() {
        // Etape 1 : creer le conteneur principal
        BorderPane root = new BorderPane();

        // Etape 2 : creer le tableau des rendez-vous
        table = new TableView<>();
        // Etape 2 bis : forcer l'ajustement des colonnes
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Etape 3 : creer la colonne ID
        TableColumn<RendezVous, String> idCol = new TableColumn<>("ID");
        // Etape 4 : lier la colonne ID au champ id
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Etape 5 : creer la colonne medecin (nom)
        TableColumn<RendezVous, String> medecinCol = new TableColumn<>("Medecin");
        // Etape 6 : resoudre le nom du medecin
        medecinCol.setCellValueFactory(cell -> {
            // Etape 7 : construire un libelle medecin
            String label = resolveMedecinLabel(cell.getValue().getMedecinId());
            return new javafx.beans.property.SimpleStringProperty(label);
        });

        // Etape 8 : creer la colonne medecin ID
        TableColumn<RendezVous, String> medecinIdCol = new TableColumn<>("Medecin ID");
        // Etape 9 : lier la colonne medecin ID au champ medecinId
        medecinIdCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMedecinId()));

        // Etape 10 : creer la colonne patient (nom)
        TableColumn<RendezVous, String> patientCol = new TableColumn<>("Patient");
        // Etape 11 : resoudre le nom du patient
        patientCol.setCellValueFactory(cell -> {
            // Etape 12 : construire un libelle patient
            String label = resolvePatientLabel(cell.getValue().getPatientId());
            return new javafx.beans.property.SimpleStringProperty(label);
        });

        // Etape 13 : creer la colonne patient ID
        TableColumn<RendezVous, String> patientIdCol = new TableColumn<>("Patient ID");
        // Etape 14 : lier la colonne patient ID au champ patientId
        patientIdCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPatientId()));

        // Etape 15 : creer la colonne date/heure
        TableColumn<RendezVous, String> dateCol = new TableColumn<>("Date/Heure");
        // Etape 16 : formater la date/heure en texte
        dateCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getDateHeure().toString()));

        // Etape 17 : ajouter les colonnes au tableau
        table.getColumns().addAll(idCol, medecinCol, medecinIdCol, patientCol, patientIdCol, dateCol);
        // Etape 18 : creer la liste filtree liee au DataStore
        filtered = new FilteredList<>(dataStore.getRendezVous(), r -> true);
        // Etape 19 : connecter la liste filtree au tableau
        table.setItems(filtered);

        // Etape 20 : creer le champ de recherche
        TextField searchField = new TextField();
        // Etape 21 : definir le placeholder de recherche
        searchField.setPromptText("Rechercher rendez-vous...");

        // Etape 22 : creer la liste de criteres de recherche
        ComboBox<String> sortBox = new ComboBox<>();
        // Etape 23 : definir les choix de recherche
        sortBox.getItems().setAll("Tous", "ID", "Medecin", "Medecin ID", "Patient", "Patient ID", "Date/Heure");
        // Etape 24 : definir le critere par defaut
        sortBox.setValue("Tous");

        // Etape 25 : ecouter les changements de recherche
        searchField.textProperty().addListener((obs, oldValue, newValue) ->
            applyFilters(searchField.getText(), sortBox.getValue()));

        // Etape 26 : ecouter les changements de critere
        sortBox.valueProperty().addListener((obs, oldValue, newValue) ->
            applyFilters(searchField.getText(), newValue));

        // Etape 28 : creer les champs de saisie
        ComboBox<Medecin> medecinBox = new ComboBox<>(dataStore.getMedecins());
        // Etape 29 : definir le placeholder medecin
        medecinBox.setPromptText("Medecin");
        // Etape 30 : definir le convertisseur medecin
        medecinBox.setConverter(UiTextConverters.medecinConverter());

        // Etape 31 : creer la liste patients
        ComboBox<Patient> patientBox = new ComboBox<>(dataStore.getPatients());
        // Etape 32 : definir le placeholder patient
        patientBox.setPromptText("Patient");
        // Etape 33 : definir le convertisseur patient
        patientBox.setConverter(UiTextConverters.patientConverter());

        // Etape 34 : creer le picker de date
        DatePicker datePicker = new DatePicker();
        // Etape 35 : creer la liste d'heures
        ComboBox<LocalTime> timeBox = new ComboBox<>();

        // Etape 36 : creer un label d'etat de disponibilite
        Label availabilityLabel = new Label("Selectionnez medecin, patient, date et heure");

        // Etape 37 : creer les boutons d'action
        Button addButton = new Button("Enregistrer");
        // Etape 38 : appliquer le style primaire
        addButton.getStyleClass().add("primary");
        Button updateButton = new Button("Modifier");
        Button deleteButton = new Button("Supprimer");
        Button clearButton = new Button("Actualiser");

        // Etape 39 : creer l'action de rafraichissement des heures
        Runnable refreshTimes = () -> updateAvailableTimes(medecinBox, patientBox, datePicker, timeBox);
        // Etape 40 : creer l'action de verification de disponibilite
        Runnable refreshAvailability = () -> updateAvailabilityStatus(medecinBox, patientBox, datePicker, timeBox, availabilityLabel);
        // Etape 41 : ecouter les changements de medecin
        medecinBox.valueProperty().addListener((obs, oldValue, newValue) -> refreshTimes.run());
        // Etape 42 : ecouter les changements de patient
        patientBox.valueProperty().addListener((obs, oldValue, newValue) -> refreshTimes.run());
        // Etape 43 : ecouter les changements de date
        datePicker.valueProperty().addListener((obs, oldValue, newValue) -> refreshTimes.run());
        // Etape 44 : ecouter les changements d'heure
        timeBox.valueProperty().addListener((obs, oldValue, newValue) -> refreshAvailability.run());
        // Etape 45 : rafraichir le statut apres chaque mise a jour des heures
        medecinBox.valueProperty().addListener((obs, oldValue, newValue) -> refreshAvailability.run());
        patientBox.valueProperty().addListener((obs, oldValue, newValue) -> refreshAvailability.run());
        datePicker.valueProperty().addListener((obs, oldValue, newValue) -> refreshAvailability.run());

        // Etape 46 : bloquer les dates passees
        datePicker.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date != null && date.isBefore(LocalDate.now())) {
                    setDisable(true);
                }
            }
        });

        // Etape 47 : definir l'action d'ajout
        addButton.setOnAction(event -> {
            // Etape 48 : lire les valeurs du formulaire
            Medecin medecin = medecinBox.getValue();
            Patient patient = patientBox.getValue();
            LocalDate date = datePicker.getValue();
            LocalTime time = timeBox.getValue();
            // Etape 49 : verifier les champs obligatoires
            if (medecin == null || patient == null || date == null || time == null) {
                // Etape 50 : afficher un message d'erreur
                showAlert("Champs incomplets", "Selectionnez medecin, patient et horaire.");
                // Etape 51 : arreter l'action
                return;
            }
            // Etape 52 : creer le rendez-vous
            RendezVous rdv = new RendezVous(null, patient.getId(), medecin.getId(), LocalDateTime.of(date, time));
            try {
                // Etape 53 : enregistrer le rendez-vous
                rendezVousService.create(rdv);
                // Etape 54 : rafraichir les horaires
                refreshTimes.run();
            } catch (ValidationException ex) {
                // Etape 55 : afficher l'erreur metier
                showAlert("Rendez-vous refuse", ex.getMessage());
            }
        });

        // Etape 56 : definir l'action de modification
        updateButton.setOnAction(event -> {
            // Etape 57 : recuperer le rendez-vous selectionne
            RendezVous selected = table.getSelectionModel().getSelectedItem();
            Medecin medecin = medecinBox.getValue();
            Patient patient = patientBox.getValue();
            LocalDate date = datePicker.getValue();
            LocalTime time = timeBox.getValue();
            // Etape 58 : verifier les champs
            if (selected == null || medecin == null || patient == null || date == null || time == null) {
                return;
            }
            try {
                // Etape 59 : mettre a jour via le service
                rendezVousService.update(selected, medecin.getId(), patient.getId(), LocalDateTime.of(date, time));
                // Etape 60 : rafraichir le tableau
                table.refresh();
                // Etape 61 : rafraichir les horaires
                refreshTimes.run();
            } catch (ValidationException ex) {
                // Etape 62 : afficher l'erreur metier
                showAlert("Rendez-vous refuse", ex.getMessage());
            }
        });

        // Etape 63 : definir l'action de suppression
        deleteButton.setOnAction(event -> {
            // Etape 64 : recuperer le rendez-vous selectionne
            RendezVous selected = table.getSelectionModel().getSelectedItem();
            // Etape 65 : supprimer si selection existe
            if (selected != null) {
                rendezVousService.delete(selected);
                // Etape 66 : rafraichir les horaires
                refreshTimes.run();
            }
        });

        // Etape 67 : definir l'action de nettoyage
        clearButton.setOnAction(event -> {
            // Etape 68 : remettre le formulaire a zero
            medecinBox.setValue(null);
            patientBox.setValue(null);
            datePicker.setValue(null);
            timeBox.getItems().clear();
            // Etape 69 : remettre le message d'etat
            availabilityLabel.setText("Selectionnez medecin, patient, date et heure");
            availabilityLabel.getStyleClass().removeAll("status-ok", "status-bad");
        });

        // Etape 70 : ecouter la selection dans le tableau
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {
            // Etape 71 : sortir si pas de selection
            if (selected == null) {
                return;
            }
            // Etape 72 : remplir le formulaire a partir de la selection
            medecinBox.setValue(resolveMedecin(selected.getMedecinId()));
            patientBox.setValue(resolvePatient(selected.getPatientId()));
            datePicker.setValue(selected.getDateHeure().toLocalDate());
            timeBox.setItems(FXCollections.observableArrayList(selected.getDateHeure().toLocalTime()));
            timeBox.setValue(selected.getDateHeure().toLocalTime());
            // Etape 73 : afficher l'etat de disponibilite
            refreshAvailability.run();
        });

        // Etape 74 : construire la barre d'outils
        VBox tools = new VBox(8,
            UiFactory.labeledLine("Recherche", searchField),
            UiFactory.labeledLine("Trier par", sortBox)
        );
        // Etape 75 : ajouter du padding a la barre d'outils
        tools.setPadding(new Insets(10));

        // Etape 76 : construire le formulaire
        VBox form = new VBox(8,
                UiFactory.labeledLine("Medecin", medecinBox),
                UiFactory.labeledLine("Patient", patientBox),
                UiFactory.labeledLine("Date", datePicker),
                UiFactory.labeledLine("Heure", timeBox),
                availabilityLabel,
                buildActionBar(addButton, updateButton, deleteButton, clearButton)
        );
        // Etape 77 : ajouter du padding au formulaire
        form.setPadding(new Insets(10));
        // Etape 78 : appliquer le style carte
        form.getStyleClass().add("card");

        // Etape 78 bis : rendre le formulaire redimensionnable
        form.setMaxWidth(Double.MAX_VALUE);

        // Etape 78 ter : creer un scroll pour voir tout le formulaire
        ScrollPane formScroll = new ScrollPane(form);
        // Etape 78 quater : activer l'ajustement en largeur
        formScroll.setFitToWidth(true);

        // Etape 79 : creer un SplitPane vertical pour le redimensionnement
        SplitPane split = new SplitPane();
        // Etape 80 : utiliser une orientation verticale
        split.setOrientation(Orientation.VERTICAL);
        // Etape 81 : definir la position initiale du separateur
        split.setDividerPositions(0.4);
        // Etape 82 : ajouter les outils, le formulaire et le tableau
        split.getItems().addAll(tools, formScroll, table);
        // Etape 83 : placer le SplitPane au centre
        root.setCenter(split);

        // Etape 84 : retourner la racine graphique
        return root;
    }

    private void applyFilters(String keywordValue, String criterion) {
        // Etape 1 : normaliser le mot cle
        String keyword = keywordValue != null ? keywordValue.trim().toLowerCase() : "";
        // Etape 2 : appliquer le predicat compose
        filtered.setPredicate(rdv -> {
            if (keyword.isEmpty()) {
                return true;
            }
            String medecinLabel = resolveMedecinLabel(rdv.getMedecinId()).toLowerCase();
            String patientLabel = resolvePatientLabel(rdv.getPatientId()).toLowerCase();
            if (criterion == null || "Tous".equals(criterion)) {
                return rdv.getMedecinId().toLowerCase().contains(keyword)
                        || rdv.getPatientId().toLowerCase().contains(keyword)
                        || medecinLabel.contains(keyword)
                        || patientLabel.contains(keyword)
                        || rdv.getDateHeure().toString().toLowerCase().contains(keyword);
            }
            return switch (criterion) {
                case "ID" -> rdv.getId().toLowerCase().contains(keyword);
                case "Medecin" -> medecinLabel.contains(keyword);
                case "Medecin ID" -> rdv.getMedecinId().toLowerCase().contains(keyword);
                case "Patient" -> patientLabel.contains(keyword);
                case "Patient ID" -> rdv.getPatientId().toLowerCase().contains(keyword);
                case "Date/Heure" -> rdv.getDateHeure().toString().toLowerCase().contains(keyword);
                default -> false;
            };
        });
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
     * @Objectif Mettre a jour les horaires disponibles.
     * @param medecinBox choix medecin
     * @param patientBox choix patient
     * @param datePicker choix date
     * @param timeBox choix heure
     */
    private void updateAvailableTimes(ComboBox<Medecin> medecinBox, ComboBox<Patient> patientBox,
                                      DatePicker datePicker, ComboBox<LocalTime> timeBox) {
        // Etape 1 : lire les selections actuelles
        Medecin medecin = medecinBox.getValue();
        Patient patient = patientBox.getValue();
        LocalDate date = datePicker.getValue();
        // Etape 2 : verifier que la date est selectionnee
        if (date == null) {
            // Etape 3 : vider les heures
            timeBox.getItems().clear();
            return;
        }

        // Etape 4 : construire tous les creneaux du jour
        List<LocalDateTime> allSlots = DateTimeUtils.buildSlots(date, 1);
        // Etape 5 : extraire les heures
        List<LocalTime> times = allSlots.stream()
                .map(LocalDateTime::toLocalTime)
                .toList();

        // Etape 6 : injecter les heures dans la liste
        timeBox.setItems(FXCollections.observableArrayList(times));
        // Etape 7 : si medecin ou patient manquant, on garde la liste sans statut
        if (medecin == null || patient == null) {
            return;
        }
    }

    /**
     * @Objectif Mettre a jour le message de disponibilite.
     * @param medecinBox choix medecin
     * @param patientBox choix patient
     * @param datePicker choix date
     * @param timeBox choix heure
     * @param availabilityLabel label d'etat
     */
    private void updateAvailabilityStatus(ComboBox<Medecin> medecinBox, ComboBox<Patient> patientBox,
                                          DatePicker datePicker, ComboBox<LocalTime> timeBox,
                                          Label availabilityLabel) {
        // Etape 1 : lire les selections actuelles
        Medecin medecin = medecinBox.getValue();
        Patient patient = patientBox.getValue();
        LocalDate date = datePicker.getValue();
        LocalTime time = timeBox.getValue();

        // Etape 2 : verifier que tout est selectionne
        if (medecin == null || patient == null || date == null || time == null) {
            // Etape 3 : afficher un message neutre
            availabilityLabel.setText("Selectionnez medecin, patient, date et heure");
            availabilityLabel.getStyleClass().removeAll("status-ok", "status-bad");
            return;
        }

        // Etape 4 : construire le creneau choisi
        LocalDateTime slot = LocalDateTime.of(date, time);
        // Etape 5 : recuperer les creneaux disponibles pour le medecin
        List<LocalDateTime> medecinSlots = rendezVousService.availableSlotsForMedecin(medecin.getId());
        // Etape 6 : recuperer les creneaux disponibles pour le patient
        List<LocalDateTime> patientSlots = rendezVousService.availableSlotsForPatient(patient.getId());

        // Etape 7 : verifier la disponibilite
        boolean ok = medecinSlots.contains(slot) && patientSlots.contains(slot);
        // Etape 8 : afficher le statut
        if (ok) {
            availabilityLabel.setText("Creneau disponible");
            availabilityLabel.getStyleClass().removeAll("status-bad");
            availabilityLabel.getStyleClass().add("status-ok");
        } else {
            availabilityLabel.setText("Creneau deja occupe");
            availabilityLabel.getStyleClass().removeAll("status-ok");
            availabilityLabel.getStyleClass().add("status-bad");
        }
    }

    /**
     * @Objectif Resoudre le libelle d'un medecin.
     * @param medecinId identifiant medecin
     * @return libelle affiche
     */
    private String resolveMedecinLabel(String medecinId) {
        // Etape 1 : rechercher un libelle lisible
        return dataStore.getMedecins().stream()
                .filter(medecin -> medecin.getId().equals(medecinId))
                .map(medecin -> medecin.getNom() + " " + medecin.getPrenom())
                .findFirst()
                .orElse("-");
    }

    /**
     * @Objectif Resoudre le libelle d'un patient.
     * @param patientId identifiant patient
     * @return libelle affiche
     */
    private String resolvePatientLabel(String patientId) {
        // Etape 1 : rechercher un libelle lisible
        return dataStore.getPatients().stream()
                .filter(patient -> patient.getId().equals(patientId))
                .map(patient -> patient.getNom() + " " + patient.getPrenom())
                .findFirst()
                .orElse("-");
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
Probleme: L'UI ne differenciait pas les erreurs metier.
Cause: Exceptions generiques non captees.
Consequence: Messages peu clairs pour l'utilisateur.
Solution: Capturer ValidationException et afficher un message explicite.
Pourquoi: Guider la correction de saisie.
Comment: Remplacer IllegalArgumentException dans le catch.
*/
