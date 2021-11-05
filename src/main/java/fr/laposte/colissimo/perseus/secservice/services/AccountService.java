package fr.laposte.colissimo.perseus.secservice.services;


import java.util.List;
import fr.laposte.colissimo.perseus.secservice.entities.AppRole;
import fr.laposte.colissimo.perseus.secservice.entities.AppUser;

public interface AccountService {
	AppUser addnewUser(AppUser appUser);
	AppRole addnewRole(AppRole appRole);
	void addRoleToUser(String username ,String roleName);
	AppUser loadByUsername(String username);
	List<AppUser> listUsers();
}
