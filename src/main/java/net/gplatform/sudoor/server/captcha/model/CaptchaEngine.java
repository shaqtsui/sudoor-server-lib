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

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.gplatform.sudoor.server.masterkey.MasterKey;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;

/**
 * Remove Autowired Request & Sesssion due to the Autowired issue (Refer to
 * Autowired.txt)
 * 
 * @author xufucheng
 *
 */
@Component
public class CaptchaEngine {
	final Logger logger = LoggerFactory.getLogger(CaptchaEngine.class);

	@Autowired
	MasterKey masterKey;

	private Producer captchaProducer = null;

	@Autowired
	public void setCaptchaProducer(Producer captchaProducer) {
		this.captchaProducer = captchaProducer;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public boolean validate(HttpServletRequest request) {
		String captchaFromPage = request.getParameter("_captcha");
		HttpSession session = request.getSession();
		String captchaFromSession = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);

		logger.debug("CaptchaValidator: Session ID:{} captchaFromPage:{} captchaFromSession:{}", session.getId(), captchaFromPage, captchaFromSession);

		if (masterKey.checkWithMasterKey(captchaFromPage)) {
			return true;
		}

		if (StringUtils.equalsIgnoreCase(captchaFromSession, captchaFromPage)) {
			return true;
		}
		return false;
	}

	public void renderCapcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Set to expire far in the past.
		response.setDateHeader("Expires", 0);
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");

		// return a jpeg
		response.setContentType("image/jpeg");

		// create the text for the image
		String capText = captchaProducer.createText();

		// store the text in the session
		request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
		logger.debug("Save Captcha: {} in Session:{}", capText, request.getSession().getId());

		// create the image with the text
		BufferedImage bi = captchaProducer.createImage(capText);

		ServletOutputStream out = response.getOutputStream();

		// write the data out
		ImageIO.write(bi, "jpg", out);
		try {
			out.flush();
		} finally {
			out.close();
		}
	}
}
