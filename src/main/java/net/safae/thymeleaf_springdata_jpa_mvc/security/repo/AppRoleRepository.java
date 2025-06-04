package net.safae.thymeleaf_springdata_jpa_mvc.security.repo;

import net.safae.thymeleaf_springdata_jpa_mvc.security.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole, String> {
}
