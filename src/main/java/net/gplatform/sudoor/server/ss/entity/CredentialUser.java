package net.gplatform.sudoor.server.ss.entity;

import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class CredentialUser {
	@Id
	@NotNull
	@Email
	String username;
	@NotNull
	String password;
	@NotNull
	Boolean enabled;

	@OneToOne(cascade = CascadeType.PERSIST)
	CredentialAuthority authorities;

	// @OneToMany
	// List<CredentialAuthorities> credentialAuthorities;

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

	public CredentialAuthority getAuthorities() {
		return authorities;
	}

	public void setAuthorities(CredentialAuthority authorities) {
		this.authorities = authorities;
	}
}
