package net.gplatform.sudoor.server.ss.entity;

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

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;

@Entity
@Table(name = "users")
public class CredentialUser {
	// Start Files required by SS
	@Id
	@NotNull
	@Email
	String username;
	@NotNull
	String password;
	@NotNull
	Boolean enabled;
	// End Files required by SS

	@OneToMany(cascade = CascadeType.ALL)
	List<CredentialAuthority> credentialAuthorities;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<CredentialAuthority> getCredentialAuthorities() {
		return credentialAuthorities;
	}

	public void setCredentialAuthorities(List<CredentialAuthority> credentialAuthorities) {
		this.credentialAuthorities = credentialAuthorities;
	}

}
