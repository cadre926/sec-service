package fr.laposte.colissimo.perseus.secservice.services;


import java.util.List;
import fr.laposte.colissimo.perseus.secservice.entities.AppRole;
import fr.laposte.colissimo.perseus.secservice.entities.AppUser;

public interface AccountService {
	AppUser addnewUser(AppUser appUser);
	AppUser addnewRole(AppRole appRole);
	void addRoleToUser(String username ,String roleName);
	AppUser loadByusername(String username);
	List<AppUser> listUsers();
}
