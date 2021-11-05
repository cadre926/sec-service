package fr.laposte.colissimo.perseus.secservice;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import fr.laposte.colissimo.perseus.secservice.entities.AppUser;
import fr.laposte.colissimo.perseus.secservice.filters.JwtAuthenticationFitlter;
import fr.laposte.colissimo.perseus.secservice.filters.JwtAuthorizationFitlter;
import fr.laposte.colissimo.perseus.secservice.services.AccountService;
import fr.laposte.colissimo.perseus.secservice.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private UserDetailsServiceImpl userDetailsService;
	
	 

	public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
		super();
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userDetailsService);
		 
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.headers().frameOptions().disable();
		// http.formLogin();

		http.authorizeRequests().antMatchers("/h2-console/**", "/refreshToken/**").permitAll();
		http.authorizeRequests().anyRequest().authenticated();
		http.addFilter(new JwtAuthenticationFitlter(authenticationManagerBean()));
		http.addFilterBefore(new JwtAuthorizationFitlter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {

		return super.authenticationManagerBean();
	}

}
