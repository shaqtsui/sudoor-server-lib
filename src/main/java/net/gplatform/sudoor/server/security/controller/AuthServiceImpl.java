package net.gplatform.sudoor.server.security.controller;

/*
 * #%L
 * sudoor-server-lib
 * %%
 * Copyright (C) 2013 - 2015 Shark Xu
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

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@WebService
public class AuthServiceImpl implements AuthService {
	@Autowired
	net.gplatform.sudoor.server.security.model.auth.SSAuth SSAuth;

	@Override
	public String authenticate(String username, String password) {
		SSAuth.authenticate(username, password);
		return "SUCCESS";
	}

	@Override
	public String register(String username, String password) {
		SSAuth.register(username, password);
		return "SUCCESS";
	}
}
