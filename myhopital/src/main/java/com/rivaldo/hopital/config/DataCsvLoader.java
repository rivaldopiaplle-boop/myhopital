package com.rivaldo.hopital.config;

import com.rivaldo.hopital.domain.Adresse;
import com.rivaldo.hopital.domain.Consultation;
import com.rivaldo.hopital.domain.DocumentMedical;
import com.rivaldo.hopital.domain.DocumentType;
import com.rivaldo.hopital.domain.EtatDossier;
import com.rivaldo.hopital.domain.EtatPatient;
import com.rivaldo.hopital.domain.Medecin;
import com.rivaldo.hopital.domain.MoyenContact;
import com.rivaldo.hopital.domain.Patient;
import com.rivaldo.hopital.domain.RendezVous;
import com.rivaldo.hopital.domain.Sexe;
import com.rivaldo.hopital.domain.Specialite;
import com.rivaldo.hopital.repository.DataStore;
import com.rivaldo.hopital.service.ConsultationService;
import com.rivaldo.hopital.service.DocumentMedicalService;
import com.rivaldo.hopital.service.MedecinService;
import com.rivaldo.hopital.service.PatientService;
import com.rivaldo.hopital.service.RendezVousService;
import com.rivaldo.hopital.util.CsvUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Fichier DataCsvLoader.java
 * @Objectif Charger des donnees de demo depuis des CSV.
 * @Couche Config
 */
public class DataCsvLoader {
    private static final String PATIENTS_FILE = "/data/patients.csv";
    private static final String MEDECINS_FILE = "/data/medecins.csv";
    private static final String RENDEZVOUS_FILE = "/data/rendezvous.csv";
    private static final String CONSULTATIONS_FILE = "/data/consultations.csv";
    private static final String DOCUMENTS_FILE = "/data/documents.csv";

    private final DataStore dataStore;
    private final PatientService patientService;
    private final MedecinService medecinService;
    private final RendezVousService rendezVousService;
    private final ConsultationService consultationService;
    private final DocumentMedicalService documentMedicalService;

    /**
     * @Objectif Construire le chargeur CSV.
     * @param dataStore stockage en memoire
     * @param patientService service patients
     * @param medecinService service medecins
     * @param rendezVousService service rendez-vous
     * @param consultationService service consultations
     * @param documentMedicalService service documents
     */
    public DataCsvLoader(DataStore dataStore, PatientService patientService, MedecinService medecinService,
                         RendezVousService rendezVousService, ConsultationService consultationService,
                         DocumentMedicalService documentMedicalService) {
        this.dataStore = dataStore;
        this.patientService = patientService;
        this.medecinService = medecinService;
        this.rendezVousService = rendezVousService;
        this.consultationService = consultationService;
        this.documentMedicalService = documentMedicalService;
    }

    /**
     * @Objectif Charger toutes les donnees depuis CSV.
     */
    public void loadAll() {
        // Etape 1: Charger les patients
        // On garde un index par email pour relier les autres donnees
        Map<String, Patient> patientsByEmail = loadPatients();
        // Etape 2: Charger les medecins
        // On garde un index par email pour relier les rendez-vous
        Map<String, Medecin> medecinsByEmail = loadMedecins();
        // Etape 3: Charger les rendez-vous
        loadRendezVous(patientsByEmail, medecinsByEmail);
        // Etape 4: Charger les consultations
        loadConsultations(patientsByEmail, medecinsByEmail);
        // Etape 5: Charger les documents
        loadDocuments(patientsByEmail, medecinsByEmail);
    }

    private Map<String, Patient> loadPatients() {
        // Etape 1: Creer un index vide
        Map<String, Patient> map = new HashMap<>();
        // Etape 2: Lire toutes les lignes du fichier CSV patients
        List<String> lines = CsvUtils.readResourceLines(PATIENTS_FILE);
        for (String line : lines) {
            // Etape 3: Decouper la ligne sur le separateur ;
            String[] parts = line.split(";", -1);
            if (parts.length < 13) {
                // Etape 4: Ligne incomplete, on ignore
                continue;
            }
            // Etape 5: Construire l'objet Patient
            Patient patient = new Patient(null, null, parts[0], parts[1], LocalDate.parse(parts[2]),
                    Sexe.valueOf(parts[3]), parts[4], parts[5],
                    new Adresse(parts[6], parts[7], parts[8], parts[9]),
                    MoyenContact.valueOf(parts[10]), EtatPatient.valueOf(parts[11]));
            // Etape 6: Lire l'option ouvrir dossier
            boolean openDossier = "OUI".equalsIgnoreCase(parts[12]);
            // Etape 7: Etat du dossier si fourni, sinon OUVERT
            EtatDossier etatDossier = parts.length > 13 && !parts[13].isBlank()
                    ? EtatDossier.valueOf(parts[13])
                    : EtatDossier.OUVERT;
            // Etape 8: Sauvegarder le patient via le service
            patientService.create(patient, openDossier, etatDossier);
            // Etape 9: Indexer par email pour les liens suivants
            map.put(patient.getEmail(), patient);
        }
        return map;
    }

    private Map<String, Medecin> loadMedecins() {
        // Etape 1: Creer un index vide
        Map<String, Medecin> map = new HashMap<>();
        // Etape 2: Lire toutes les lignes du fichier CSV medecins
        List<String> lines = CsvUtils.readResourceLines(MEDECINS_FILE);
        for (String line : lines) {
            // Etape 3: Decouper la ligne sur le separateur ;
            String[] parts = line.split(";", -1);
            if (parts.length < 12) {
                // Etape 4: Ligne incomplete, on ignore
                continue;
            }
            // Etape 5: Construire l'objet Medecin
            Medecin medecin = new Medecin(null, null, parts[0], parts[1], LocalDate.parse(parts[2]),
                    Sexe.valueOf(parts[3]), parts[4], parts[5],
                    new Adresse(parts[6], parts[7], parts[8], parts[9]),
                    MoyenContact.valueOf(parts[10]), Specialite.valueOf(parts[11]));
            // Etape 6: Sauvegarder via le service
            medecinService.create(medecin);
            // Etape 7: Indexer par email pour les liens suivants
            map.put(medecin.getEmail(), medecin);
        }
        return map;
    }

    private void loadRendezVous(Map<String, Patient> patientsByEmail, Map<String, Medecin> medecinsByEmail) {
        // Etape 1: Lire toutes les lignes du fichier CSV rendez-vous
        List<String> lines = CsvUtils.readResourceLines(RENDEZVOUS_FILE);
        for (String line : lines) {
            // Etape 2: Decouper la ligne sur le separateur ;
            String[] parts = line.split(";", -1);
            if (parts.length < 3) {
                // Etape 3: Ligne incomplete, on ignore
                continue;
            }
            // Etape 4: Retrouver le patient et le medecin via email
            Patient patient = patientsByEmail.get(parts[0]);
            Medecin medecin = medecinsByEmail.get(parts[1]);
            if (patient == null || medecin == null) {
                // Etape 5: Si on ne trouve pas le lien, on ignore
                continue;
            }
            // Etape 6: Construire le rendez-vous
            RendezVous rdv = new RendezVous(null, patient.getId(), medecin.getId(), LocalDateTime.parse(parts[2]));
            // Etape 7: Sauvegarder via le service
            rendezVousService.create(rdv);
        }
    }

    private void loadConsultations(Map<String, Patient> patientsByEmail, Map<String, Medecin> medecinsByEmail) {
        // Etape 1: Lire toutes les lignes du fichier CSV consultations
        List<String> lines = CsvUtils.readResourceLines(CONSULTATIONS_FILE);
        for (String line : lines) {
            // Etape 2: Decouper la ligne sur le separateur ;
            String[] parts = line.split(";", -1);
            if (parts.length < 5) {
                // Etape 3: Ligne incomplete, on ignore
                continue;
            }
            // Etape 4: Retrouver le patient et le medecin via email
            Patient patient = patientsByEmail.get(parts[0]);
            Medecin medecin = medecinsByEmail.get(parts[1]);
            if (patient == null || medecin == null || patient.getDossiers().isEmpty()) {
                // Etape 5: Si on ne trouve pas le lien ou pas de dossier, on ignore
                continue;
            }
            // Etape 6: Recuperer le dossier principal du patient
            String dossierId = patient.getDossiers().get(0).getId();
            // Etape 7: Construire la consultation
            Consultation consultation = new Consultation(null, patient.getId(), medecin.getId(), dossierId,
                    LocalDateTime.parse(parts[2]), parts[3], parts[4]);
            // Etape 8: Sauvegarder via le service
            consultationService.create(consultation);
        }
    }

    private void loadDocuments(Map<String, Patient> patientsByEmail, Map<String, Medecin> medecinsByEmail) {
        // Etape 1: Lire toutes les lignes du fichier CSV documents
        List<String> lines = CsvUtils.readResourceLines(DOCUMENTS_FILE);
        for (String line : lines) {
            // Etape 2: Decouper la ligne sur le separateur ;
            String[] parts = line.split(";", -1);
            if (parts.length < 5) {
                // Etape 3: Ligne incomplete, on ignore
                continue;
            }
            // Etape 4: Retrouver le patient et le medecin via email
            Patient patient = patientsByEmail.get(parts[0]);
            Medecin medecin = medecinsByEmail.get(parts[1]);
            if (patient == null || medecin == null || patient.getDossiers().isEmpty()) {
                // Etape 5: Si on ne trouve pas le lien ou pas de dossier, on ignore
                continue;
            }
            // Etape 6: Recuperer le dossier principal du patient
            String dossierId = patient.getDossiers().get(0).getId();
            // Etape 7: Construire le document medical
            DocumentMedical document = new DocumentMedical(null, patient.getId(), medecin.getId(), dossierId,
                    parts[2], DocumentType.valueOf(parts[3]), LocalDate.parse(parts[4]));
            // Etape 8: Sauvegarder via le service
            documentMedicalService.create(document);
        }
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Les donnees de demo etaient limitees.
Cause: Pas de fichier externe.
Consequence: Peu de varietes dans les tableaux.
Solution: Charger depuis des CSV.
Pourquoi: Modifier les donnees sans changer le code.
Comment: Parsing simple en memoire.
*/
