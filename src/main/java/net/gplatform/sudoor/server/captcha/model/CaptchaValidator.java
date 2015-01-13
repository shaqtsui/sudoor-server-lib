package net.gplatform.sudoor.server.captcha.model;

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
		
		logger.debug("CaptchaValidator: captchaFromPage:{} captchaFromSession:{}", captchaFromPage, captchaFromSession);

		if (StringUtils.equalsIgnoreCase(captchaFromSession, captchaFromPage)) {
			return true;
		}
		return false;
	}
}
