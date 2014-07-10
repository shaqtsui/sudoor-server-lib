package net.gplatform.sudoor.server.auth.ws.test;

import net.gplatform.sudoor.server.auth.ws.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class AuthServiceTest {

	@Autowired
	AuthService authServiceClient;

	@RequestMapping("/authService")
	public String invokeAuthenticate() {
		authServiceClient.authenticate();
		return "layout/header";
	}
}
