package net.gplatform.sudoor.server.ss;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.gplatform.sudoor.server.ss.entity.CredentialUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Path("/SpringSecurity")
public class SpringSecurityResource {
	@Autowired
	private CredentialUserRepository credentialUserRepository;

	@GET
	@Path("Authentication")
	@Produces(MediaType.APPLICATION_JSON)
	public Authentication getAuthentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication;
	}

	@GET
	@Path("CredentialUser/{userName}")
	@Produces(MediaType.APPLICATION_JSON)
	public CredentialUser getCredentialUser(@PathParam("userName") String userName) {
		if(userName == null){
			return null;
		}
		CredentialUser user=credentialUserRepository.findOne(userName);
		if(user == null){
			return null;
		}
		user.setPassword("######");
		return user;
	}
}
