package net.gplatform.sudoor.server.security;

/*
 * #%L
 * sudoor-server-lib
 * %%
 * Copyright (C) 2013 - 2014 Shark Xu
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.gplatform.sudoor.server.security.entity.CredentialUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Path("/sudoor/SpringSecurity")
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
