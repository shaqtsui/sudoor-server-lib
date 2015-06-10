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
import javax.servlet.http.HttpServletResponse;

import net.gplatform.sudoor.server.captcha.model.CaptchaEngine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * This class is used to generate image. The image config is in common-config-sudoor.xml.
 * 
 * Validation In Application :
	protected void validateCaptcha(
					HttpServletRequest request,
					Object command,
					Errors errors) throws Exception {
			String captchaId = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
			String response = ((RegistrationVO) command).getCaptchaResponse();
	
			if (log.isDebugEnabled()) {
					log.debug("Validating captcha response: '" + response + "'");
			}
	
			if (!StringUtils.equalsIgnoreCase(captchaId, response)) {
					errors.rejectValue("captchaResponse", "error.invalidcaptcha",
									"Invalid Entry");
			}
	}

 * 
 * @author xufucheng
 *
 */

@Controller
public class CaptchaImageCreateController {
		final Logger logger = LoggerFactory.getLogger(CaptchaImageCreateController.class);
		
		@Autowired
		CaptchaEngine captchaEngine;

        @RequestMapping("/sudoor/captcha-image.html")
        public ModelAndView handleRequest(
                        HttpServletRequest request,
                        HttpServletResponse response) throws Exception {
        		captchaEngine.renderCapcha(request, response);
                return null;
        }
}