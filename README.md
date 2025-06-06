# Application de Gestion Hospitalière - Rapport Technique

## Aperçu du Projet
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
  - J'ai commencé par H2, puis j'ai basculé vers MySQL
- **Frontend**:
  - Le moteur de templates **Thymeleaf**
  - Bootstrap

## 🛠️ Stack Technologique

### Backend
| Technologie | Usage |
|-------------|-------|
| ![Java](https://img.shields.io/badge/java-17-%23ED8B00) | Langage principal |
| ![Spring Boot](https://img.shields.io/badge/spring%20boot-3.1.5-%236DB33F) | Framework core |
| ![Spring Security](https://img.shields.io/badge/spring%20security-6.1-%236DB33F) | Authentification |

### Frontend
| Technologie | Usage |
|-------------|-------|
| ![Thymeleaf](https://img.shields.io/badge/thymeleaf-3.1-%23005C0F) | Templating |
| ![Bootstrap](https://img.shields.io/badge/bootstrap-5.3-%23563D7C) | UI Components |

### Base de Données
```mermaid
pie
    title Utilisation BD
    "H2 (Dev)" : 40
    "MySQL (Prod)" : 60
```

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

## 🧱 Structure des Packages

<img width="467" alt="str1" src="https://github.com/user-attachments/assets/19ff60e1-dbf9-4d80-9168-231b21b37601" />

<img width="467" alt="str2" src="https://github.com/user-attachments/assets/ab3deb44-ad0f-49fc-831f-49ffe1f5d460" />


### Architecture MVC

#### 🗂 Package entities
- **Patient.java**  
  Entité JPA représentant un patient avec :
  - `@Id` + `@GeneratedValue` pour l'identifiant
  - Validation des champs (`@NotEmpty`, `@Size`)
  - Annotations Lombok pour réduire le code boilerplate

#### 🗂 Package repository
- **PatientRepository.java**  
  Interface JpaRepository offrant :
  ```java
  Page<Patient> findByNomContains(String keyword, Pageable pageable);
  
  @Query("select p from Patient p where p.nom like :x")
  Page<Patient> chercher(@Param("x") String keyword, Pageable pageable);

#### 🔐 Package security
- SecurityConfig.java
Configuration Spring Security avec :
```
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    // Configuration des règles d'accès
    // Authentification InMemory/JDBC/Personnalisée
}
```

#### 🌐 Package web
- PatientController.java
Contrôleur MVC avec :
```
@GetMapping("/patients")
public String index(Model model, 
                   @RequestParam(defaultValue = "0") int page,
                   @RequestParam(defaultValue = "") String keyword) {
    // Pagination et recherche
}
```

## 🗂 Package Entities - Classe Patient

```java
@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Patient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotEmpty @Size(min = 4, max = 40)
    private String nom;
    
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateNaissance;
    
    private Boolean malade;
    
    @DecimalMin("100")
    private Double score;
}
```
``` mermaid
classDiagram
    class Patient {
        +Long id
        +String nom
        +Date dateNaissance
        +Boolean malade
        +Double score
        +builder() PatientBuilder
    }
```

- @Data : Génère getters/setters
- @Builder : Permet la construction fluide
- @NoArgsConstructor : Constructeur par défaut
- @AllArgsConstructor : Constructeur complet
  
Workflow de persistance :
```mermaid
sequenceDiagram
    participant App as Application
    participant JPA as JPA/Hibernate
    participant DB as Base de données
    
    App->>JPA: patientRepository.save(patient)
    JPA->>DB: INSERT INTO patient...
    DB-->>JPA: ID généré
    JPA-->>App: Patient persisté avec ID
```

## 🗂️ Package repositories - PatientRepository

```java
public interface PatientRepository extends JpaRepository<Patient, Long> {
    // Méthode dérivée automatique
    Page<Patient> findByNomContains(String keyword, Pageable pageable);
    
    // Requête JPQL personnalisée
    @Query("SELECT p FROM Patient p WHERE p.nom LIKE :x")
    Page<Patient> chercher(@Param("x") String keyword, Pageable pageable);
}
```

Fonctionnalités clés :
- Hérite des opérations CRUD de base via JpaRepository

Deux types de requêtes :
  - Méthode dérivée : Génération auto par Spring (findByNomContains)
  - Requête custom : Contrôle précis via @Query
Retourne des résultats paginés (Page<T> + Pageable)

``` mermaid
flowchart LR
    A[Controller] -->|Appelle| B[PatientRepository]
    B -->|Auto-implémente| C[Requêtes SQL]
    C -->|Retourne| D[Résultats paginés]
```

## 🔒 Package Security - Gestion d'Authentification

### 🏷️ Entités de Sécurité

```java
// AppRole.java
@Entity @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AppRole {
    @Id 
    private String role;  // "ADMIN", "USER", etc.
}

// AppUser.java
@Entity @Data @Builder
public class AppUser {
    @Id @GeneratedValue
    private Long id;
    
    @Column(unique=true)
    private String username;
    private String password;
    
    @ManyToMany(fetch=FetchType.EAGER)
    private Set<AppRole> roles = new HashSet<>();
}
```
Fonctionnalités :
- Gestion des rôles et utilisateurs
- Relation ManyToMany entre utilisateurs et rôles
- Chargement immédiat des rôles (EAGER)

## 📚 Repositories
```java
public interface AppRoleRepository extends JpaRepository<AppRole, String> {}

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);
}
```
Avantages :
- CRUD automatique via JpaRepository
- Recherche d'utilisateur par username

## 🛠️ Services
```java
public interface AccountService {
    AppUser addNewUser(String username, String password, String confirmPassword);
    void addRoleToUser(String username, String roleName);
    UserDetails loadUserByUsername(String username);
}

@Service @Transactional @RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AppUserRepository userRepo;
    private final AppRoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    // Implémentation des méthodes
}

@Service @AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final AccountService accountService;
    
    public UserDetails loadUserByUsername(String username) {
        // Conversion AppUser → UserDetails
    }
}
```
Fonctionnalités clés :
- Gestion transactionnelle
- Hachage des mots de passe
- Conversion pour Spring Security

## ⚙️ Configuration
``` java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(form -> form.loginPage("/login"));
        http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
        http.rememberMe(rm -> rm.key("secret").tokenValiditySeconds(1209600));
        return http.build();
    }
}
```
Options d'authentification :
1. InMemory (pour tests)
2. JDBC (base de données)
3. Personnalisée (via UserDetailServiceImpl)

``` mermaid
flowchart TB
    A[Login Page] -->|Submit| B[SecurityConfig]
    B --> C[UserDetailServiceImpl]
    C --> D[AccountService]
    D --> E[AppUserRepository]
    E -->|Verify| F[(Database)]
```
Fonctionnalités activées :
- Formulaire de login personnalisé
- Protection CSRF
- Remember-me (14 jours)
- Contrôle d'accès par rôles

Cela offre une sécurité complète tout en restant flexible pour différentes méthodes d'authentification.

## 🌐 Package Web - Contrôleurs Principaux

### 🏥 PatientController

```java
@Controller
@RequiredArgsConstructor
public class PatientController {
    private final PatientRepository patientRepository;

    @GetMapping("/patients")
    public String index(Model model, 
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword) {
        Page<Patient> pagePatients = patientRepository.findByNomContains(keyword, PageRequest.of(page, 5));
        model.addAttribute("patients", pagePatients.getContent());
        model.addAttribute("pages", new int[pagePatients.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "patients";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete")
    public String delete(@RequestParam Long id, 
                        @RequestParam int page,
                        @RequestParam String keyword) {
        patientRepository.deleteById(id);
        return "redirect:/user/index?page="+page+"&keyword="+keyword;
    }
}
```

### 🔐 SecurityController
``` java
@Controller
public class SecurityController {
    @GetMapping("/notAuthorized")
    public String notAuthorized() {
        return "notAuthorized";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}

### Sécurité
- 🔒 2 modes d'authentification:
  - InMemory (test)
  - JDBC (prod)
- 👥 Gestion des rôles

### 📁 Templates
```
| Fichier              | Description                  |
|----------------------|------------------------------|
| `patients.html`      | Liste des patients           |
| `formPatients.html`  | Formulaire d'ajout           |
| `editPatients.html`  | Formulaire d'édition         |
| `login.html`         | Page de connexion            |
| `notAuthorized.html` | Page d'erreur 403            |
| `template1.html`     | Template/Layout de base             |

### ⚙️ Fichiers de configuration
- application.properties :
  ```
  spring.datasource.url=jdbc:h2:mem:hospital
  spring.h2.console.enabled=true
  ```
- schema.sql : Script d'initialisation de la base

## 🔄 Workflow d'Exécution

### Diagramme de Séquence MVC

```mermaid
sequenceDiagram
    participant Vue as Vue (Thymeleaf)
    participant Controller as Controller
    participant Service as Service
    participant Repository as Repository
    
    Vue->>Controller: Requête HTTP (GET/POST)
    Controller->>Service: Appel métier
    Service->>Repository: Accès données JPA
    Repository-->>Service: Résultats DB
    Service-->>Controller: Données traitées
    Controller-->>Vue: Modèle + Vue HTML
```



## 🚀 Classe Principale - HopitalApplication
``` java
@SpringBootApplication
public class HopitalApplication {
    public static void main(String[] args) {
        SpringApplication.run(HopitalApplication.class, args);
    }

    @Bean
    CommandLineRunner start(PatientRepository pr) {
        return args -> {
            // Initialisation des patients
            Patient.builder().nom("Mohamed").score(100).build();
            // ...
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

## ⚙️ Configuration (application.properties)
Accès console H2 : http://localhost:8084/h2-console
```
# Application
spring.application.name=Hospital
server.port=8084

# Database
spring.datasource.url=jdbc:h2:mem:hospital
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```
```mermaid
flowchart TD
    A[PatientController] -->|Gère| B[Patients]
    A -->|Utilise| C[PatientRepository]
    D[SecurityController] -->|Fournit| E[Vues Sécurité]
    F[HopitalApplication] -->|Configure| G[Sécurité+DB]
    G -->|Initialise| H[Données de test]

```

- DB :
  
<img width="744" alt="db" src="https://github.com/user-attachments/assets/4ca21ca0-beb2-44c9-adb3-af735c60eb95" />

Tables :

- app_role :
  
<img width="200" alt="image" src="https://github.com/user-attachments/assets/db4b7d55-3b47-44fe-8c36-002ab5ab528a" />

- app_user :
  
<img width="804" alt="image" src="https://github.com/user-attachments/assets/f119bfcd-a547-4f4c-a0cc-75fc0c6f84d2" />

- app_user_role :

<img width="250" alt="image" src="https://github.com/user-attachments/assets/81f3d994-6881-4d6f-bc30-0e5ec36d3393" />

- Patients :
  
<img width="419" alt="image" src="https://github.com/user-attachments/assets/ee32a5aa-cd48-4c39-be30-34a03d227b6d" />

- Users :
  
<img width="598" alt="image" src="https://github.com/user-attachments/assets/2fc6d696-6b81-4841-9fc5-1eaa2f16b2cc" />

- Authorities :
  
<img width="295" alt="image" src="https://github.com/user-attachments/assets/f0413515-eab1-4c54-998f-41cf5efb3a26" />

## Résultats :
- Page Login :
  <img width="1279" alt="image" src="https://github.com/user-attachments/assets/114d9410-620c-479d-9e8d-369128153a52" />
- Page Accueil (pour Admin)
  <img width="1280" alt="image" src="https://github.com/user-attachments/assets/f45582ec-f6c6-4d99-85d4-620139a00d93" />
- Page Acceuil (pour User)
  <img width="1278" alt="image" src="https://github.com/user-attachments/assets/f7ad2e25-ede9-4e7a-b65d-c315e5ac56cd" />
- Page Modification
  <img width="1279" alt="image" src="https://github.com/user-attachments/assets/484b35cf-152c-40e0-8cc6-e619dc6f360f" />
- Page Modification (Après la modification)
  <img width="1280" alt="image" src="https://github.com/user-attachments/assets/3a0b756d-ac64-447e-a1fe-74bbf38b3c87" />
- Page Suppression: Je supprime Leila avec id=16
  <img width="1280" alt="image" src="https://github.com/user-attachments/assets/bbecaacf-b028-4092-95a0-0660430dbec3" />
- Page Suppréssion: Aprés la suppression
  <img width="1280" alt="image" src="https://github.com/user-attachments/assets/6b903568-aee5-4d96-a64b-5317db5a5eed" />
- Page Recherche 🔍:
  <img width="1280" alt="image" src="https://github.com/user-attachments/assets/34d5e869-432f-4e94-b5a1-5f60d21585fe" />
- Validation du formulaire
  <img width="1280" alt="image" src="https://github.com/user-attachments/assets/3730d51f-84ba-48f3-b2fe-33527dd2b0d5" />
- Page Ajouter:
  <img width="1279" alt="image" src="https://github.com/user-attachments/assets/ceb91d11-df8b-46ac-bcaa-bcce425a5909" />
- Page Ajouter: Aprés l'ajout
  <img width="1280" alt="image" src="https://github.com/user-attachments/assets/886ee31a-e1f0-451a-a412-eef17ce6d49a" />

## 🏁 Conclusion

Ce projet complet démontre la puissance de **Spring Boot** pour développer des applications web sécurisées et efficaces. À travers cette application hospitalière, j'ai pu implémenté :

- **Architecture MVC propre** avec séparation claire des couches (Controller/Service/Repository)
- **Sécurité robuste** combinant :
  - Authentification personnalisée (JDBC + InMemory)
  - Gestion fine des rôles (`@PreAuthorize`)
  - Protection contre les injections SQL
- **Expérience utilisateur optimale** :
  - Pagination intelligente
  - Recherche dynamique
  - Validation des formulaires
- **Productivité développeur** :
  - Réduction de code avec Lombok
  - Configuration simplifiée (Spring Boot Auto-configuration)
  - Console H2 pour le débogage

### 🌟 Bonnes Pratiques Appliquées
```mermaid
pie
    title Principes Respectés
    "Sécurité" : 30
    "Maintenabilité" : 25
    "Performance" : 20
    "UX" : 15
    "Tests" : 10
```

# Auteur : 
Safae ERAJI
