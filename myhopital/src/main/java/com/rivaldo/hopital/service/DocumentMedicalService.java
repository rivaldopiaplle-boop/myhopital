package com.rivaldo.hopital.service;

import com.rivaldo.hopital.domain.DocumentMedical;
import com.rivaldo.hopital.repository.DataStore;
import com.rivaldo.hopital.repository.DatabaseRepository;
import com.rivaldo.hopital.util.IdGenerator;

/**
 * @Fichier DocumentMedicalService.java
 * @Objectif Gerer les documents medicaux.
 * @Couche Service
 */
public class DocumentMedicalService {
    private final DataStore dataStore;
    private final DatabaseRepository databaseRepository;

    /**
     * @Objectif Construire le service document medical.
     * @param dataStore stockage en memoire
     * @param databaseRepository acces base de donnees
     */
    public DocumentMedicalService(DataStore dataStore, DatabaseRepository databaseRepository) {
        this.dataStore = dataStore;
        this.databaseRepository = databaseRepository;
    }

    /**
     * @Objectif Creer un document medical.
     * @param document document
     * @return document cree
     */
    public DocumentMedical create(DocumentMedical document) {
        // Etape 1: Generer un identifiant unique
        document.setId(IdGenerator.newId());
        // Etape 2: Ajouter le document au stockage en memoire
        dataStore.getDocuments().add(document);
        // Etape 2 bis: Persister le document
        databaseRepository.insertDocument(document);
        // Etape 3: Retourner le document cree
        return document;
    }

    /**
     * @Objectif Mettre a jour un document.
     * @param document document
     */
    public void update(DocumentMedical document) {
        // Etape 1: Aucune action supplementaire
        // L'UI modifie la meme instance en memoire
        databaseRepository.updateDocument(document);
    }

    /**
     * @Objectif Supprimer un document.
     * @param document document
     */
    public void delete(DocumentMedical document) {
        // Etape 1: Supprimer le document en base
        databaseRepository.deleteDocument(document.getId());
        // Etape 2: Supprimer le document de la liste
        dataStore.getDocuments().remove(document);
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Les documents n'avaient pas d'identifiant.
Cause: Enregistrement brut.
Consequence: Difficile de tracer un document.
Solution: Generer un id a la creation.
Pourquoi: Faciliter la recherche et la suppression.
Comment: IdGenerator dans le service.
*/
