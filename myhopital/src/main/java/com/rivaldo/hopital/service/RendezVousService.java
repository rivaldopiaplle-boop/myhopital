package com.rivaldo.hopital.service;

import com.rivaldo.hopital.domain.RendezVous;
import com.rivaldo.hopital.exception.ValidationException;
import com.rivaldo.hopital.repository.DataStore;
import com.rivaldo.hopital.repository.DatabaseRepository;
import com.rivaldo.hopital.util.DateTimeUtils;
import com.rivaldo.hopital.util.IdGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Fichier RendezVousService.java
 * @Objectif Gerer les rendez-vous avec controle de disponibilite.
 * @Couche Service
 */
public class RendezVousService {
    private final DataStore dataStore;
    private final DatabaseRepository databaseRepository;

    /**
     * @Objectif Construire le service des rendez-vous.
     * @param dataStore stockage en memoire
     * @param databaseRepository acces base de donnees
     */
    public RendezVousService(DataStore dataStore, DatabaseRepository databaseRepository) {
        this.dataStore = dataStore;
        this.databaseRepository = databaseRepository;
    }

    /**
     * @Objectif Creer un rendez-vous si le creneau est libre.
     * @param rendezVous rendez-vous
     * @return rendez-vous cree
     */
    public RendezVous create(RendezVous rendezVous) {
        // Etape 1: Verifier si le medecin est libre a ce creneau
        if (!isSlotAvailableForMedecin(rendezVous.getMedecinId(), rendezVous.getDateHeure())) {
            throw new ValidationException("Creneau medecin indisponible.");
        }
        // Etape 2: Verifier si le patient est libre a ce creneau
        if (!isSlotAvailableForPatient(rendezVous.getPatientId(), rendezVous.getDateHeure())) {
            throw new ValidationException("Creneau patient indisponible.");
        }
        // Etape 3: Generer un identifiant unique
        rendezVous.setId(IdGenerator.newId());
        // Etape 4: Ajouter le rendez-vous au stockage en memoire
        dataStore.getRendezVous().add(rendezVous);
        // Etape 4 bis: Persister le rendez-vous
        databaseRepository.insertRendezVous(rendezVous);
        // Etape 5: Retourner le rendez-vous cree
        return rendezVous;
    }

    /**
     * @Objectif Supprimer un rendez-vous.
     * @param rendezVous rendez-vous
     */
    public void delete(RendezVous rendezVous) {
        // Etape 1: Supprimer le rendez-vous en base
        databaseRepository.deleteRendezVous(rendezVous.getId());
        // Etape 2: Supprimer le rendez-vous de la liste
        dataStore.getRendezVous().remove(rendezVous);
    }

    /**
     * @Objectif Mettre a jour un rendez-vous.
     * @param rendezVous rendez-vous
     * @param medecinId medecin
     * @param patientId patient
     * @param dateHeure date/heure
     */
    public void update(RendezVous rendezVous, String medecinId, String patientId, LocalDateTime dateHeure) {
        // Etape 1: Verifier la disponibilite du medecin (en excluant le rendez-vous courant)
        if (!isSlotAvailableForMedecinExcluding(rendezVous.getId(), medecinId, dateHeure)) {
            throw new ValidationException("Creneau medecin indisponible.");
        }
        // Etape 2: Verifier la disponibilite du patient (en excluant le rendez-vous courant)
        if (!isSlotAvailableForPatientExcluding(rendezVous.getId(), patientId, dateHeure)) {
            throw new ValidationException("Creneau patient indisponible.");
        }
        // Etape 3: Appliquer les nouvelles valeurs
        rendezVous.setMedecinId(medecinId);
        rendezVous.setPatientId(patientId);
        rendezVous.setDateHeure(dateHeure);
        databaseRepository.updateRendezVous(rendezVous);
    }

        /**
         * @Objectif Retourner les creneaux disponibles d'un medecin.
         * @param medecinId identifiant medecin
         * @return liste des creneaux
         */
    public List<LocalDateTime> availableSlotsForMedecin(String medecinId) {
        // Etape 1: Construire tous les creneaux possibles sur 10 jours
        List<LocalDateTime> allSlots = DateTimeUtils.buildSlots(LocalDate.now(), 10);
        // Etape 2: Lister les creneaux deja pris par ce medecin
        List<LocalDateTime> taken = dataStore.getRendezVous().stream()
                .filter(rdv -> rdv.getMedecinId().equals(medecinId))
                .map(RendezVous::getDateHeure)
                .collect(Collectors.toList());
        // Etape 3: Garder seulement les creneaux libres
        return allSlots.stream()
                .filter(slot -> !taken.contains(slot))
                .collect(Collectors.toList());
    }

    /**
     * @Objectif Retourner les creneaux disponibles d'un patient.
     * @param patientId identifiant patient
     * @return liste des creneaux
     */
    public List<LocalDateTime> availableSlotsForPatient(String patientId) {
        // Etape 1: Construire tous les creneaux possibles sur 10 jours
        List<LocalDateTime> allSlots = DateTimeUtils.buildSlots(LocalDate.now(), 10);
        // Etape 2: Lister les creneaux deja pris par ce patient
        List<LocalDateTime> taken = dataStore.getRendezVous().stream()
                .filter(rdv -> rdv.getPatientId().equals(patientId))
                .map(RendezVous::getDateHeure)
                .collect(Collectors.toList());
        // Etape 3: Garder seulement les creneaux libres
        return allSlots.stream()
                .filter(slot -> !taken.contains(slot))
                .collect(Collectors.toList());
    }

    private boolean isSlotAvailableForMedecin(String medecinId, LocalDateTime slot) {
        // Etape 1: Aucun rendez-vous existant ne doit matcher medecin + creneau
        return dataStore.getRendezVous().stream()
                .noneMatch(rdv -> rdv.getMedecinId().equals(medecinId) && rdv.getDateHeure().equals(slot));
    }

    private boolean isSlotAvailableForPatient(String patientId, LocalDateTime slot) {
        // Etape 1: Aucun rendez-vous existant ne doit matcher patient + creneau
        return dataStore.getRendezVous().stream()
                .noneMatch(rdv -> rdv.getPatientId().equals(patientId) && rdv.getDateHeure().equals(slot));
    }

    private boolean isSlotAvailableForMedecinExcluding(String rdvId, String medecinId, LocalDateTime slot) {
        // Etape 1: Ignorer le rendez-vous courant, puis verifier les autres
        return dataStore.getRendezVous().stream()
                .filter(rdv -> !rdv.getId().equals(rdvId))
                .noneMatch(rdv -> rdv.getMedecinId().equals(medecinId) && rdv.getDateHeure().equals(slot));
    }

    private boolean isSlotAvailableForPatientExcluding(String rdvId, String patientId, LocalDateTime slot) {
        // Etape 1: Ignorer le rendez-vous courant, puis verifier les autres
        return dataStore.getRendezVous().stream()
                .filter(rdv -> !rdv.getId().equals(rdvId))
                .noneMatch(rdv -> rdv.getPatientId().equals(patientId) && rdv.getDateHeure().equals(slot));
    }
}

/*
HISTOIRE (Probleme, Cause, Consequence, Solution)
Probleme: Un patient ou un medecin pouvait avoir deux rendez-vous au meme moment.
Cause: Aucun controle de conflit avant creation.
Consequence: Conflits d'agenda et erreurs metier.
Solution: Verifier les disponibilites avant l'enregistrement.
Pourquoi: Garantir une planification fiable.
Comment: Comparer les creneaux existants avec la demande.
*/
