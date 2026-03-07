package com.rivaldo.hopital.service;

import com.rivaldo.hopital.domain.Medecin;
import com.rivaldo.hopital.repository.DataStore;
import com.rivaldo.hopital.repository.DatabaseRepository;
import com.rivaldo.hopital.util.IdGenerator;

import java.util.Optional;

/**
 * @Fichier MedecinService.java
 * @Objectif Gerer les medecins.
 * @Couche Service
 */
public class MedecinService {
    private final DataStore dataStore;
    private final DatabaseRepository databaseRepository;

    /**
     * @Objectif Construire le service medecin.
     * @param dataStore stockage en memoire
     * @param databaseRepository acces base de donnees
     */
    public MedecinService(DataStore dataStore, DatabaseRepository databaseRepository) {
        this.dataStore = dataStore;
        this.databaseRepository = databaseRepository;
    }

    /**
     * @Objectif Creer un medecin.
     * @param medecin medecin
     * @return medecin cree
     */
    public Medecin create(Medecin medecin) {
        // Etape 1: Generer un identifiant unique
        medecin.setId(IdGenerator.newId());
        // Etape 2: Creer un matricule court et lisible
        medecin.setMatricule("MED-" + medecin.getId().substring(0, 6));
        // Etape 3: Ajouter le medecin au stockage en memoire
        dataStore.getMedecins().add(medecin);
        // Etape 3 bis: Persister le medecin
        databaseRepository.insertMedecin(medecin);
        // Etape 4: Retourner le medecin cree
        return medecin;
    }

    /**
     * @Objectif Mettre a jour un medecin.
     * @param medecin medecin
     */
    public void update(Medecin medecin) {
        // Etape 1: Aucune action supplementaire
        // L'UI modifie la meme instance en memoire
        databaseRepository.updateMedecin(medecin);
    }

    /**
     * @Objectif Supprimer un medecin.
     * @param medecin medecin
     */
    public void delete(Medecin medecin) {
        // Etape 1: Supprimer le medecin en base
        databaseRepository.deleteMedecin(medecin.getId());
        // Etape 2: Supprimer le medecin de la liste
        dataStore.getMedecins().remove(medecin);
    }

    /**
     * @Objectif Trouver un medecin par id.
     * @param id identifiant
     * @return medecin optionnel
     */
    public Optional<Medecin> findById(String id) {
        // Etape 1: Parcourir les medecins
        // Etape 2: Garder celui qui match l'id
        return dataStore.getMedecins().stream()
                .filter(medecin -> medecin.getId().equals(id))
                .findFirst();
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Les medecins n'avaient pas d'identifiant metier.
Cause: Pas de matricule genere.
Consequence: Difficultes de reference.
Solution: Generer un matricule a la creation.
Pourquoi: Identifier clairement chaque medecin.
Comment: Prefixe MED- sur un id unique.
*/
