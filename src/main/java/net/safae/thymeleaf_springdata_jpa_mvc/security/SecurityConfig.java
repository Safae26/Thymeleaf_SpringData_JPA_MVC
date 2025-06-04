package net.safae.thymeleaf_springdata_jpa_mvc.security;

import lombok.AllArgsConstructor;
import net.safae.thymeleaf_springdata_jpa_mvc.security.service.UserDetailServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)// Il faut proteger les endpoints moi meme au niveau de controleur
public class SecurityConfig {

    private UserDetailServiceImpl userDetailServiceImpl;

    // définir les utilisateurs qui ont le droit d'accéder à l'application
    //@Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder passwordEncoder){
        String encodedPassword = passwordEncoder.encode("1234");
        System.out.println(encodedPassword);
        return new InMemoryUserDetailsManager(
                User.withUsername("user1").password(encodedPassword).roles("USER").build(),
                User.withUsername("user2").password(encodedPassword).roles("USER").build(),
                User.withUsername("admin").password(encodedPassword).roles("USER","ADMIN").build()
        );
    }
    //JDBC authentication
    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource){
        // on spécifie le data source, où on a les rôles et les tables
        return new JdbcUserDetailsManager(dataSource);
    }
    @Bean // Exécuter au démarrage
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .formLogin(form ->
                        form.loginPage("/login")
                                .defaultSuccessUrl("/user/index")
                                .permitAll()
                ).rememberMe(remember -> remember
                        .key("my-unique-key")
                        .tokenValiditySeconds(1209600)
                )
                .authorizeHttpRequests(ar ->ar.requestMatchers("/webjars/**").permitAll()
                        //.requestMatchers("/deletePatient/").hasRole("ADMIN")
                        //.requestMatchers("/admin/**").hasRole("ADMIN")
                        //.requestMatchers("/user/**").hasRole("USER")
                        .anyRequest().authenticated())
                .exceptionHandling(exception ->{
                    exception.accessDeniedPage("/notAuthorized");
                }).userDetailsService(userDetailServiceImpl)
                .build();
    }
}
