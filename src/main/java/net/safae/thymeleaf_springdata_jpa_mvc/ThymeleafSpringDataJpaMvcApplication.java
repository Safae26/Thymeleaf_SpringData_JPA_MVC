package net.safae.thymeleaf_springdata_jpa_mvc;

import net.safae.thymeleaf_springdata_jpa_mvc.entities.Patient;
import net.safae.thymeleaf_springdata_jpa_mvc.repository.PatientRepository;
import net.safae.thymeleaf_springdata_jpa_mvc.security.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import java.util.Date;

@SpringBootApplication
public class ThymeleafSpringDataJpaMvcApplication {
    @Autowired
    private PatientRepository patientRepository;
    public static void main(String[] args) {
        SpringApplication.run(ThymeleafSpringDataJpaMvcApplication.class, args);
    }

    @Bean
    CommandLineRunner start(PatientRepository patientRepository) {
        return args -> {
            // trois façons pour insérer des patients
            // 1 ere méthode
            Patient patient = new Patient();
            patient.setId(null);
            patient.setNom("user");
            patient.setDateNaissance(new Date());
            patient.setMalade(false);
            patient.setScore(23);
            //patientRepository.save(patient);

            // 2 eme méthode
            Patient patient2 = new Patient(null,"utilisateur",new Date(),false, 123);
            //patientRepository.save(patient2);

            // 3 eme méthode : en utilisant builder
            Patient patient3= Patient.builder()
                    .nom("user")
                    .dateNaissance(new Date())
                    .score(56)
                    .malade(true)
                    .build();
            //patientRepository.save(patient3);

            patientRepository.save(new Patient(null,"Mohammed",new Date(),false,134));
            patientRepository.save(new Patient(null, "Yuki", new Date(), false, 111));
            patientRepository.save(new Patient(null, "Zhang", new Date(), true, 222));
            patientRepository.save(new Patient(null, "Amina", new Date(), false, 333));
            patientRepository.save(new Patient(null, "Giovanni", new Date(), true, 444));
            patientRepository.save(new Patient(null, "Olga", new Date(), false, 555));
            patientRepository.save(new Patient(null, "Fatima", new Date(), false, 240));
            patientRepository.save(new Patient(null, "Amina", new Date(), true, 170));
            patientRepository.save(new Patient(null, "Khadija", new Date(), false, 390));
            patientRepository.save(new Patient(null, "Noura", new Date(), true, 125));
            patientRepository.save(new Patient(null, "Sara", new Date(), false, 290));
            patientRepository.save(new Patient(null, "Lina", new Date(), true, 200));
            patientRepository.save(new Patient(null, "Yasmin", new Date(), false, 360));
            patientRepository.save(new Patient(null, "Salma", new Date(), true, 230));
            patientRepository.save(new Patient(null, "Zineb", new Date(), false, 190));
            patientRepository.save(new Patient(null, "Leila", new Date(), true, 310));
            patientRepository.findAll().forEach(p ->{
                System.out.println(p.getNom());
            });
        };
    }

    //@Bean
    CommandLineRunner commandLineRunnerUserDetails(AccountService accountService) {
        return args -> {
            accountService.addNewRole("USER");
            accountService.addNewRole("ADMIN");
            accountService.addNewUser("user1","1234","user1@gmail.com","1234");
            accountService.addNewUser("user2","1234","user1@gmail.com","1234");
            accountService.addNewUser("admin","1234","admin@gmail.com","1234");

            accountService.addRoleToUser("user1","USER");
            accountService.addRoleToUser("user2","USER");
            accountService.addRoleToUser("admin","USER");
            accountService.addRoleToUser("admin","ADMIN");

        };

    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner commandLineRunner(JdbcUserDetailsManager jdbcUserDetailsManager){
        PasswordEncoder passwordEncoder = passwordEncoder();
        return args ->{

            if(!jdbcUserDetailsManager.userExists("user1")){
                jdbcUserDetailsManager.createUser(User.withUsername("user1").password(passwordEncoder.encode("1234")).roles("USER").build());
            }
            if(!jdbcUserDetailsManager.userExists("user2")){
                jdbcUserDetailsManager.createUser(User.withUsername("user2").password(passwordEncoder.encode("1234")).roles("USER").build());
            }
            if(!jdbcUserDetailsManager.userExists("admin")){
                jdbcUserDetailsManager.createUser(User.withUsername("admin").password(passwordEncoder.encode("1234")).roles("USER","ADMIN").build());
            }

        };
    }

}
