package net.gplatform.sudoor.server.captcha.controller;

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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import net.gplatform.sudoor.server.captcha.model.CaptchaEngine;

import org.springframework.beans.factory.annotation.Autowired;

@Path("/sudoor/captcha")
@Produces(MediaType.TEXT_PLAIN)
public class CaptchaValidateController {
	@Autowired
	CaptchaEngine captchaEngine;

	/**
	 * parameter _captcha need to be availabe in request
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("validate")
	public boolean validate(@Context HttpServletRequest request) throws Exception {
		return captchaEngine.validate(request);
	}
}