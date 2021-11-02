package fr.laposte.colissimo.perseus.secservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.laposte.colissimo.perseus.secservice.entities.AppRole;



public interface AppRoleRepository extends JpaRepository<AppRole, Long>{
 		AppRole findByRoleName(String roleName);
}
 