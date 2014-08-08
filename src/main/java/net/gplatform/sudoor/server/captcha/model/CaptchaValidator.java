package net.gplatform.sudoor.server.captcha.model;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.google.code.kaptcha.Constants;

@Component
public class CaptchaValidator {
	public boolean validate(HttpServletRequest request) {
		String captchaFromSession = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
		String captchaFromPage = request.getParameter("_captcha");

		if (StringUtils.equalsIgnoreCase(captchaFromSession, captchaFromPage)) {
			return true;
		}
		return false;

	}
}
