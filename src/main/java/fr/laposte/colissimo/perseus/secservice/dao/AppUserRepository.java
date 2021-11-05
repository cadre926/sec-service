package fr.laposte.colissimo.perseus.secservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.laposte.colissimo.perseus.secservice.entities.AppUser;




public interface AppUserRepository extends JpaRepository<AppUser, Long>{
 		AppUser findByUsername(String username);
}
 