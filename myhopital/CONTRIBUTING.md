# GUIDE DE CONTRIBUTION

Ce guide explique comment contribuer au projet MyHopital : organisation, conventions, tests, et bonnes pratiques.

---

## 1) Philosophie du projet

- Lisible avant tout : code clair, noms explicites
- Modification minimale : un changement a la fois
- Documentation a jour : tout changement important doit etre documente

---

## 2) Organisation des fichiers

- Domain : objets metier (pas de logique)
- Service : regles metier et validations
- Repository : acces aux donnees (SQLite + DataStore)
- UI : ecrans JavaFX
- Config : demarrage et initialisation
- Util : outils partages (CSV, dates, ids)

---

## 3) Conventions de code

### Nommage
- Classes : PascalCase (PatientService)
- Methodes : camelCase (createPatient)
- Variables : camelCase (patientList)
- Constantes : UPPER_SNAKE_CASE

### Style
- Indentation : 4 espaces
- Longueur : privilegier les lignes courtes
- Commentaires : uniquement si une logique est complexe

---

## 4) Commits et changements

- Un commit = une idee
- Message clair : "Add patient validation" ou "Fix rendezvous conflict"
- Ne pas melanger refactoring et nouvelles fonctions

---

## 5) Ajouter une fonctionnalite (processus conseille)

1. Identifier le besoin (ex: champ "assurance" patient)
2. Mettre a jour domain/ (Patient.java)
3. Mettre a jour repository/ (tables + requetes SQL)
4. Mettre a jour service/ (validation)
5. Mettre a jour ui/ (formulaire + table)
6. Tester manuellement dans l'application
7. Mettre a jour la documentation

---

## 6) Tests manuels recommandés

- Connexion avec les 3 roles
- Creation d'un patient
- Creation d'un rendez-vous
- Creation d'une consultation
- Ajout d'un document medical
- Verification du dossier medical

---

## 7) Erreurs frequentes a eviter

- Modifier une table SQLite sans mettre a jour DatabaseRepository
- Oublier de rafraichir les listes du DataStore
- Modifier un fichier UI sans tester l'ecran

---

## 8) Documentation a maintenir

- README.md : vue generale
- INDEX.md : table des matieres
- DEMARRAGE_RAPIDE.md : parcours pas a pas
- EXPLICATION_COMPLETE_PROJET.md : architecture detaillee
- GUIDE_PROJET_COMPLET.md : arborescence detaillee
- SCHEMAS_VISUELS.md : schemas
- CONTRIBUTING.md : ce guide

---

## 9) Questions / aide

Si un point est flou, ajoutez une note dans la documentation avant de pousser des changements. La coherence du projet est prioritaire.
