package fr.laposte.colissimo.perseus.secservice.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.laposte.colissimo.perseus.secservice.entities.AppRole;
import fr.laposte.colissimo.perseus.secservice.entities.AppUser;
import fr.laposte.colissimo.perseus.secservice.services.AccountService;
import fr.laposte.colissimo.perseus.secservice.utils.JwtUtil;
import lombok.Data;

@RestController
public class AccountRestController {

	private AccountService accountService;

	public AccountRestController(AccountService accountService) {
		super();
		this.accountService = accountService;
	}

	@GetMapping(path = "/users")
	@PostAuthorize("hasAuthority('USER')")
	public List<AppUser> appUsers() {
		return accountService.listUsers();
	}

	@PostMapping(path = "/users")
	@PostAuthorize("hasAuthority('ADMIN')")
	public AppUser saveUser(@RequestBody AppUser appUser) {
		return accountService.addnewUser(appUser);
	}

	@PostMapping(path = "/roles")
	public AppRole saveUser(@RequestBody AppRole appRole) {
		return accountService.addnewRole(appRole);

	}

	@PostMapping(path = "/addRoleToUser")
	@PostAuthorize("hasAuthority('ADMIN')")
	public void addRoleToUser(@RequestBody RoleUserForm roleUserForm) {
		accountService.addRoleToUser(roleUserForm.getUsername(), roleUserForm.getRoleName());

	}

	@GetMapping(path = "/refreshToken")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		String authToekn=request.getParameter(JwtUtil.AUTH_HEADER);
		if (authToekn != null && authToekn.startsWith(JwtUtil.PREFIX)) {
			try {
				String jwt = authToekn.substring(JwtUtil.PREFIX.length());
				Algorithm algorithm = Algorithm.HMAC256(JwtUtil.SECRET);
				JWTVerifier jwtVerifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
				String username = decodedJWT.getSubject();
  				AppUser appUser =accountService.loadByUsername(username);
  				
  				String jwtAccessToken = JWT.create().withSubject(appUser.getUsername())

  						.withExpiresAt(new Date(System.currentTimeMillis() + JwtUtil.EXPIRE_ACCESS_TOKEN))
  						.withIssuer(request.getRequestURL().toString())
  						.withClaim("roles",
  								appUser.getAppRoles().stream().map(r-> r.getRoleName()).collect(Collectors.toList()))
  						.sign(algorithm);
  				Map<String, String> idToken = new HashMap<>();
  				idToken.put("access-token", jwtAccessToken);
  				idToken.put("refresh-token", jwt);
  				response.setContentType("application/json ");
  				new ObjectMapper().writeValue(response.getOutputStream(), idToken);
			} catch (Exception e) {
				throw e;
			}

		}
		
		else {
			throw new RuntimeException("Refresh token required!");}
	}
		
		@GetMapping(path = "/profile")
		public AppUser profile(Principal principal) {
			return accountService.loadByUsername(principal.getName());
			
		}
		
		
	}



@Data
class RoleUserForm {
	private String username;
	private String roleName;

}
