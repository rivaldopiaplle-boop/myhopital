package com.rivaldo.hopital.service;

import com.rivaldo.hopital.domain.DossierMedical;
import com.rivaldo.hopital.domain.EtatDossier;
import com.rivaldo.hopital.domain.Patient;
import com.rivaldo.hopital.repository.DataStore;
import com.rivaldo.hopital.repository.DatabaseRepository;
import com.rivaldo.hopital.util.IdGenerator;

import java.time.LocalDate;
import java.util.Optional;

/**
 * @Fichier PatientService.java
 * @Objectif Gerer les patients.
 * @Couche Service
 */
public class PatientService {
    private final DataStore dataStore;
    private final DossierMedicalService dossierService;
    private final DatabaseRepository databaseRepository;

    /**
     * @Objectif Construire le service patient.
     * @param dataStore stockage en memoire
     * @param dossierService service dossier
     * @param databaseRepository acces base de donnees
     */
    public PatientService(DataStore dataStore, DossierMedicalService dossierService,
                          DatabaseRepository databaseRepository) {
        this.dataStore = dataStore;
        this.dossierService = dossierService;
        this.databaseRepository = databaseRepository;
    }

    /**
     * @Objectif Creer un patient et ouvrir un dossier optionnel.
     * @param patient patient
     * @param openDossier ouvrir un dossier
     * @param dossierEtat etat du dossier
     * @return patient cree
     */
    public Patient create(Patient patient, boolean openDossier, EtatDossier dossierEtat) {
        // Etape 1: Generer un identifiant unique
        patient.setId(IdGenerator.newId());
        // Etape 2: Creer un numero de dossier court et lisible
        patient.setDossierNumber("DOSS-" + patient.getId().substring(0, 8));
        // Etape 3: Ajouter le patient au stockage en memoire
        dataStore.getPatients().add(patient);
        // Etape 3 bis: Persister le patient
        databaseRepository.insertPatient(patient);
        // Etape 4: Si on demande un dossier, on le cree tout de suite
        if (openDossier) {
            // Etape 5: Si l'etat n'est pas donne, on met OUVERT par defaut
            EtatDossier resolvedEtat = dossierEtat != null ? dossierEtat : EtatDossier.OUVERT;
            // Etape 6: Creer le dossier et le relier au patient
            DossierMedical dossier = dossierService.createForPatient(patient.getId(), LocalDate.now(), resolvedEtat);
            patient.addDossier(dossier);
        }
        // Etape 7: Retourner le patient cree
        return patient;
    }

    /**
     * @Objectif Mettre a jour un patient.
     * @param patient patient
     */
    public void update(Patient patient) {
        // Etape 1: Aucune action supplementaire
        // L'UI modifie la meme instance en memoire
        databaseRepository.updatePatient(patient);
    }

    /**
     * @Objectif Supprimer un patient et ses dossiers.
     * @param patient patient
     */
    public void delete(Patient patient) {
        // Etape 1: Supprimer en base
        databaseRepository.deletePatient(patient.getId());
        // Etape 1: Supprimer le patient de la liste
        dataStore.getPatients().remove(patient);
        // Etape 2: Supprimer tous les dossiers lies a ce patient
        dataStore.getDossiers().removeIf(dossier -> dossier.getPatientId().equals(patient.getId()));
    }

    /**
     * @Objectif Trouver un patient par id.
     * @param id identifiant
     * @return patient optionnel
     */
    public Optional<Patient> findById(String id) {
        // Etape 1: Parcourir les patients
        // Etape 2: Garder celui qui match l'id
        return dataStore.getPatients().stream()
                .filter(patient -> patient.getId().equals(id))
                .findFirst();
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: La creation patient devait ouvrir un dossier.
Cause: Processus non centralise.
Consequence: Dossiers manquants.
Solution: Gerer creation + dossier dans le service.
Pourquoi: Garantir un flux coherent.
Comment: Creer le patient puis le dossier si demande.
*/
