package fr.laposte.colissimo.perseus.secservice.services;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.laposte.colissimo.perseus.secservice.dao.AppRoleRepository;
import fr.laposte.colissimo.perseus.secservice.dao.AppUserRepository;
import fr.laposte.colissimo.perseus.secservice.entities.AppRole;
import fr.laposte.colissimo.perseus.secservice.entities.AppUser;


@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	private AppUserRepository appUserRepository;
	private AppRoleRepository appRoleRepository;
	private PasswordEncoder passwordEncoder;

	 

	public AccountServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository,
			PasswordEncoder passwordEncoder) {
		super();
		this.appUserRepository = appUserRepository;
		this.appRoleRepository = appRoleRepository;
		this.passwordEncoder = passwordEncoder;
	}
	@Override
	public AppUser addnewUser(AppUser appUser) {
		
		String pw=appUser.getPassword();
		appUser.setPassword(passwordEncoder.encode(pw));
		return appUserRepository.save(appUser);
	}
	@Override
	public AppRole addnewRole(AppRole appRole) {
		return appRoleRepository.save(appRole);
	}

	@Override
	public void addRoleToUser(String username, String roleName) {
		AppUser appUser=appUserRepository.findByUsername(username);
		AppRole appRole=appRoleRepository.findByRoleName(roleName);
		appUser.getAppRoles().add(appRole);
	}

	@Override
	public AppUser loadByUsername(String username) {
		
		return appUserRepository.findByUsername(username);
	 
	}

	@Override
	public List<AppUser> listUsers() {
		return appUserRepository.findAll();
	}

}
