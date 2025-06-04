package net.safae.thymeleaf_springdata_jpa_mvc.security.repo;

import net.safae.thymeleaf_springdata_jpa_mvc.security.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, String> {
    AppUser findByUsername(String username);
}
