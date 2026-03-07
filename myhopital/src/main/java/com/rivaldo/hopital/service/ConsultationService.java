package com.rivaldo.hopital.service;

import com.rivaldo.hopital.domain.Consultation;
import com.rivaldo.hopital.repository.DataStore;
import com.rivaldo.hopital.repository.DatabaseRepository;
import com.rivaldo.hopital.util.IdGenerator;

/**
 * @Fichier ConsultationService.java
 * @Objectif Gerer les consultations.
 * @Couche Service
 */
public class ConsultationService {
    private final DataStore dataStore;
    private final DatabaseRepository databaseRepository;

    /**
     * @Objectif Construire le service consultation.
     * @param dataStore stockage en memoire
     * @param databaseRepository acces base de donnees
     */
    public ConsultationService(DataStore dataStore, DatabaseRepository databaseRepository) {
        this.dataStore = dataStore;
        this.databaseRepository = databaseRepository;
    }

    /**
     * @Objectif Creer une consultation.
     * @param consultation consultation
     * @return consultation creee
     */
    public Consultation create(Consultation consultation) {
        // Etape 1: Generer un identifiant unique
        consultation.setId(IdGenerator.newId());
        // Etape 2: Ajouter la consultation au stockage en memoire
        dataStore.getConsultations().add(consultation);
        // Etape 2 bis: Persister la consultation
        databaseRepository.insertConsultation(consultation);
        // Etape 3: Retourner la consultation creee
        return consultation;
    }

    /**
     * @Objectif Mettre a jour une consultation.
     * @param consultation consultation
     */
    public void update(Consultation consultation) {
        // Etape 1: Aucune action supplementaire
        // L'UI modifie la meme instance en memoire
        databaseRepository.updateConsultation(consultation);
    }

    /**
     * @Objectif Supprimer une consultation.
     * @param consultation consultation
     */
    public void delete(Consultation consultation) {
        // Etape 1: Supprimer la consultation en base
        databaseRepository.deleteConsultation(consultation.getId());
        // Etape 2: Supprimer la consultation de la liste
        dataStore.getConsultations().remove(consultation);
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Les consultations n'avaient pas d'identifiant unique.
Cause: Creation sans id.
Consequence: Difficile de referencer une consultation.
Solution: Generer un id a la creation.
Pourquoi: Garder une trace claire.
Comment: IdGenerator dans le service.
*/
