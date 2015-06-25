package net.gplatform.sudoor.server.test.unit;

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

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.AbstractConfigurableEmbeddedServletContainer;
import org.springframework.stereotype.Component;

@Component
public class TestUtils {

	@Autowired
	AbstractConfigurableEmbeddedServletContainer abstractConfigurableEmbeddedServletContainer;

	public String getEmbeddedServletContainerBaseURL() {
		InetAddress address = abstractConfigurableEmbeddedServletContainer.getAddress();
		String addressStr = "localhost";
		if (address != null) {
			addressStr = address.getHostName();
		}

		int port = abstractConfigurableEmbeddedServletContainer.getPort();
		String contextPath = abstractConfigurableEmbeddedServletContainer.getContextPath();

		return "http://" + addressStr + ":" + port + contextPath;
	}

	public static void copyCookies(Builder targetRequstBuilder, Response fromResponse) {
		for (Iterator iterator = fromResponse.getCookies().values().iterator(); iterator.hasNext();) {
			Cookie cookie = (Cookie) iterator.next();
			targetRequstBuilder.cookie(cookie);
		}
	}

	/**
	 * Take screen short & save to: target/test/selenium/
	 * 
	 * @param driver
	 * @param name
	 */
	public static void takeScreenshot(WebDriver driver, String name) {
		byte[] bScreenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
		File target = new File("target/test/selenium/" + name);
		try {
			FileUtils.writeByteArrayToFile(target, bScreenshot);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
