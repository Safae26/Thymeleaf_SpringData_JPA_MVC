package net.safae.thymeleaf_springdata_jpa_mvc.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.*;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(min = 4, max = 40)
    private String nom;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateNaissance;
    private boolean malade;
    @DecimalMin("100")
    private int score;


}
// screenshots of classes, structure, SCREENSHOT DE DB MEM SI POSSIBLE  ou a mentionner, copier code au deepseek, screen de mysql data et table
// Mettre le bootstrap  encore jolie, userdetailservice in service security, screenshots de linterface et du code

// code mis a jour :
// package ma.enset.hospitalapp.security;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//@Bean
//public InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder passwordEncoder){
//String encodedPassword = passwordEncoder.encode("1234");
//System.out.println(encodedPassword);
//return new InMemoryUserDetailsManager(
//User.withUsername("user1").password(encodedPassword).roles("USER").build(),
//User.withUsername("user2").password(encodedPassword).roles("USER").build(),
//User.withUsername("admin").password(encodedPassword).roles("USER","ADMIN").build()
//);
//}
//@Bean
//public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//return httpSecurity
//.formLogin(Customizer.withDefaults())
//.authorizeHttpRequests(ar->ar.requestMatchers("/deletePatient/**").hasRole("ADMIN"))
//.authorizeHttpRequests(ar->ar.requestMatchers("/admin/**").hasRole("ADMIN"))
//.authorizeHttpRequests(ar->ar.requestMatchers("/user/**").hasRole("USER"))
//.authorizeHttpRequests(ar->ar.anyRequest().authenticated())
//.build();
//}
//}