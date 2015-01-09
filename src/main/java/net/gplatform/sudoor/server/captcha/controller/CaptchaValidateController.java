package net.gplatform.sudoor.server.captcha.controller;

import javax.ws.rs.Path;

import net.gplatform.sudoor.server.captcha.model.CaptchaValidator;

import org.springframework.beans.factory.annotation.Autowired;

@Path("/sudoor/captcha")
public class CaptchaValidateController {
	@Autowired
	CaptchaValidator captchaValidator;

	/**
	 * parameter _captcha need to be availabe in request
	 * No need to pass request as parameter, since it's injected into CaptchaValidator
	 * @return
	 * @throws Exception
	 */
	@Path("validate")
	public boolean validate() throws Exception {
		return captchaValidator.validate();
	}
}