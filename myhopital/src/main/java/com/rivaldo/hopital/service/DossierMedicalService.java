package com.rivaldo.hopital.service;

import com.rivaldo.hopital.domain.DossierMedical;
import com.rivaldo.hopital.domain.EtatDossier;
import com.rivaldo.hopital.repository.DataStore;
import com.rivaldo.hopital.repository.DatabaseRepository;
import com.rivaldo.hopital.util.IdGenerator;

import java.time.LocalDate;
import java.util.Optional;

/**
 * @Fichier DossierMedicalService.java
 * @Objectif Gerer les dossiers medicaux.
 * @Couche Service
 */
public class DossierMedicalService {
    private final DataStore dataStore;
    private final DatabaseRepository databaseRepository;

    /**
     * @Objectif Construire le service dossier.
     * @param dataStore stockage en memoire
     * @param databaseRepository acces base de donnees
     */
    public DossierMedicalService(DataStore dataStore, DatabaseRepository databaseRepository) {
        this.dataStore = dataStore;
        this.databaseRepository = databaseRepository;
    }

    /**
     * @Objectif Creer un dossier pour un patient.
     * @param patientId identifiant patient
     * @param dateOuverture date d'ouverture
     * @param etat etat
     * @return dossier
     */
    public DossierMedical createForPatient(String patientId, LocalDate dateOuverture, EtatDossier etat) {
        // Etape 1: Creer un dossier avec un id unique
        DossierMedical dossier = new DossierMedical(IdGenerator.newId(), patientId, dateOuverture, etat);
        // Etape 2: Ajouter le dossier au stockage en memoire
        dataStore.getDossiers().add(dossier);
        // Etape 2 bis: Persister le dossier
        databaseRepository.insertDossier(dossier);
        // Etape 3: Retourner le dossier cree
        return dossier;
    }

    /**
     * @Objectif Mettre a jour un dossier.
     * @param dossier dossier
     */
    public void update(DossierMedical dossier) {
        // Etape 1: Persister la mise a jour
        databaseRepository.updateDossier(dossier);
    }

    /**
     * @Objectif Supprimer un dossier.
     * @param dossier dossier
     */
    public void delete(DossierMedical dossier) {
        // Etape 1: Supprimer en base
        databaseRepository.deleteDossier(dossier.getId());
        // Etape 2: Supprimer de la liste memoire
        dataStore.getDossiers().remove(dossier);
    }

    /**
     * @Objectif Trouver un dossier par id.
     * @param id identifiant dossier
     * @return dossier optionnel
     */
    public Optional<DossierMedical> findById(String id) {
        // Etape 1: Parcourir les dossiers
        // Etape 2: Garder celui qui match l'id
        return dataStore.getDossiers().stream()
                .filter(dossier -> dossier.getId().equals(id))
                .findFirst();
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Le dossier n'etait pas cree a temps.
Cause: Creation separee du patient.
Consequence: Dossiers manquants lors des consultations.
Solution: Un service dedie pour creer le dossier.
Pourquoi: Assurer le lien patient-dossier.
Comment: Methode createForPatient.
*/
