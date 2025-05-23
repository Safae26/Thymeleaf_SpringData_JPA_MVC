# Rapport README - Application de Gestion des Patients

## Description du Projet
Application Web JEE basée sur Spring MVC (L'architecture web Modèle-Vue-Contrôleur), Thymeleaf et Spring Data JPA pour la gestion des patients dans un hôpital.

Elle doit permettre de :
- Gérer les patients (CRUD complet).
- Implémenter une pagination des résultats.
- Ajouter des fonctionnalités de recherche.
- Sécuriser l'accès aux différentes fonctionnalités.
- Utiliser un système de templates pour une interface cohérente.
- Valider les données des formulaires.

## Technologies
- **Backend**:
  - Spring Boot (Framework principal)
  - Spring Data JPA pour la persistance des données
  - Spring Security pour la gestion de l'authentification et des autorisations
- **Bases de données**:
  - H2 (dev)
  - MySQL (prod)
- **Frontend**:
  - Le moteur de templates **Thymeleaf**
  - Bootstrap

## 🧱 Structure des Packages
net.safae.hospital
├── entities
│ ├── Patient
│ ├── AppUser
│ └── AppRole
├── repository
│ ├── PatientRepository
│ ├── AppUserRepository
│ └── AppRoleRepository
├── web
│ ├── PatientController
│ └── SecurityController
├── service
│ └── AccountService
└── security
└── SecurityConfig

Le projet suit une architecture MVC (Modèle-Vue-Contrôleur) typique d'une application Spring Boot, il contient les packages suivants :

entities : contient les classes de domaine représentant les entités métier : Classe Patient.
repositories : contient les interfaces JPA permettant l’accès aux données :
Interface PatientRepository: Fournit des méthodes CRUD automatiques et la recherche paginée.
security :Gère l'authentification et l'autorisation via Spring Security, incluant la modélisation des utilisateurs/rôles, la configuration de sécurité et les contrôleurs dédiés. Il contient les packages:
Entités qui contient les classes AppRole pour définir les rôles d'accès et AppUser pour modéliser un utilisateur avec ses credentials et rôles associés.
Répo qui contient les interfaces AppRoleRepository / AppUserRepository pour persister et rechercher rôles/utilisateurs en base.
Service qui contient l'interface AccountService qui définit les contrats pour la gestion des utilisateurs et rôles, l'implémentation AccountServiceImpl qui implémente les règles métier (validation des mots de passe, gestion des transactions avec @Transactional), ainsi l'implémentation UserDetailServiceImpl pour adapter le modèle AppUser à Spring Security en implémentant UserDetailsService pour l'authentification.
La classe SecurityConfig pour configurer les règles d'accès et l'authentification (ex: routes protégées).
web : Contient les contrôleurs MVC :
Classe PatientController: Gère l'affichage et la recherche des patients.
Classe SecurityController: Gère les vues liées à l'authentification.
HospitalApplication : Point d'entrée de l'application avec configuration automatique.
templates: Contient les vues Thymeleaf pour l'interface utilisateur, structurées avec des fragments réutilisables et des formulaires liés aux entités.Il contient les fichiers suivants:
template1.html : Template de base avec navbar et layout commun à toutes les pages.
patients.html : Affiche la liste paginée des patients avec recherche et actions (éditer/supprimer).
formPatients.html : Formulaire de création d'un patient avec validation.
editPatients.html : Vue spécifique pour modifier un patient existant.
login.html : Page d'authentification avec formulaire de connexion.
notAuthorized.html : Message d'erreur pour les accès non autorisés.
application.properties : Paramètres de l'application (BDD, sécurité, etc.).
schema.sql : Script SQL pour initialiser la structure de la base de données.

## Fonctionnalités
### Gestion Patients
- ✅ CRUD complet
- 🔍 Recherche & pagination
- 📝 Validation de formulaires

### Sécurité
- 🔒 2 modes d'authentification:
  - InMemory (test)
  - JDBC (prod)
- 👥 Gestion des rôles

## Templates
| Fichier              | Description                  |
|----------------------|------------------------------|
| `patients.html`      | Liste des patients           |
| `formPatients.html`  | Formulaire d'ajout           |
| `editPatients.html`  | Formulaire d'édition         |
| `login.html`         | Page de connexion            |
| `notAuthorized.html` | Page d'erreur 403            |
| `template1.html`     | Template de base             |

## Dépendances Maven
```xml
<dependencies>
    <!-- Spring -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- Sécurité -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <!-- Templates -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
</dependencies>
```

## Configuration
```
# H2 Configuration
spring.datasource.url=jdbc:h2:mem:hospital
spring.h2.console.enabled=true

# MySQL Configuration (prod)
# spring.datasource.url=jdbc:mysql://localhost:3306/hospital
# spring.datasource.username=root
# spring.datasource.password=secret
```

## application.properties

# Auteur : Safae ERAJI
