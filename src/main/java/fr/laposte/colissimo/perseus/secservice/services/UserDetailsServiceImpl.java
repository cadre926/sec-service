package fr.laposte.colissimo.perseus.secservice.services;

import java.util.ArrayList;
import java.util.Collection;

import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fr.laposte.colissimo.perseus.secservice.entities.AppUser;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private AccountService accountService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser appUser = accountService.loadByUsername(username);
		Collection<GrantedAuthority> authorities = new ArrayList<>();

		appUser.getAppRoles().forEach(r -> {
			authorities.add(new SimpleGrantedAuthority(r.getRoleName()));

		});
		return new User(appUser.getUsername(), appUser.getPassword(), authorities);

	}

}
