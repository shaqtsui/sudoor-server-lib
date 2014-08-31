/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.gplatform.sudoor.server.social.controller;

import javax.validation.Valid;

import net.gplatform.sudoor.server.security.model.auth.SSAuth;
import net.gplatform.sudoor.server.social.model.Message;
import net.gplatform.sudoor.server.social.model.MessageType;
import net.gplatform.sudoor.server.social.model.SignupForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

@Controller
public class SignupController {
	@Autowired
	SSAuth SSAuth;

	private final ProviderSignInUtils providerSignInUtils = new ProviderSignInUtils();

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public SignupForm signupForm(WebRequest request) {
		Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
		if (connection != null) {
			request.setAttribute("message", new Message(MessageType.INFO, "Your " + StringUtils.capitalize(connection.getKey().getProviderId())
					+ " account is not associated with a Spring Social Showcase account. If you're new, please sign up."), WebRequest.SCOPE_REQUEST);
			return SignupForm.fromProviderUser(connection.fetchUserProfile());
		} else {
			return new SignupForm();
		}
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(@Valid SignupForm form, BindingResult formBinding, WebRequest request) {
		if (formBinding.hasErrors()) {
			return null;
		}
		String status = createAccount(form, formBinding);
		if (status != null) {
			SSAuth.signin(form.getUsername(), null);
			providerSignInUtils.doPostSignUp(form.getUsername(), request);
			return "redirect:/";
		}
		return null;
	}

	// internal helpers

	private String createAccount(SignupForm form, BindingResult formBinding) {
		try {
			String username = form.getUsername();
			String password = form.getPassword();
			SSAuth.register(username, password);
			return "SUCCESS";
		} catch (Exception e) {
			formBinding.rejectValue("username", "user.duplicateUsername", "already in use");
			return null;
		}
	}

}
