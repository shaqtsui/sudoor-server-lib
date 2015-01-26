package net.gplatform.sudoor.server.captcha.model;

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
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.code.kaptcha.Constants;

@Component
public class CaptchaValidator {
	final Logger logger = LoggerFactory.getLogger(CaptchaValidator.class);

	@Autowired
	HttpSession session;

	@Autowired
	HttpServletRequest request;

	/**
	 * Deprecated, pls use validate()
	 * 
	 * @param request
	 * @return
	 */
	@Deprecated
	public boolean validate(HttpServletRequest request) {
		String captchaFromPage = request.getParameter("_captcha");
		return validate(captchaFromPage);
	}

	public boolean validate() {
		String captchaFromPage = request.getParameter("_captcha");
		return validate(captchaFromPage);
	}

	public boolean validate(String captchaFromPage) {
		String captchaFromSession = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);

		logger.debug("CaptchaValidator: Session ID:{} captchaFromPage:{} captchaFromSession:{}", session.getId(), captchaFromPage, captchaFromSession);

		if (StringUtils.equalsIgnoreCase(captchaFromSession, captchaFromPage)) {
			return true;
		}
		return false;
	}
}
