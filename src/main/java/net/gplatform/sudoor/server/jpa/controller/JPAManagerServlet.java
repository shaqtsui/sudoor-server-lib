package net.gplatform.sudoor.server.jpa.controller;

/*
 * #%L
 * sudoor-server-lib
 * %%
 * Copyright (C) 2013 - 2014 Shark Xu
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

import java.io.IOException;
import java.io.Writer;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.gplatform.sudoor.server.jpa.model.Constants;
import net.gplatform.sudoor.server.spring.SpringContextsUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 14-3-5.
 */
@WebServlet(urlPatterns = { "/JPAManager" })
public class JPAManagerServlet extends HttpServlet {
	final Logger logger = LoggerFactory.getLogger(JPAManagerServlet.class);
	public final static String REFRESH_L1_CACHE = "refresh_l1_cache";
	public final static String REFRESH_L2_CACHE = "refresh_l2_cache";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		logger.debug("Enter JPAManager with parameters: {}", req.getParameterMap());

		if (req.getParameter(REFRESH_L2_CACHE) != null) {
			EntityManagerFactory factory = (EntityManagerFactory) SpringContextsUtil.getBean(Constants.ENTITY_MANAGER_FACTORY_ID);
			factory.getCache().evictAll();
			logger.debug("Successfully Evict L2 Cache");
			Writer out = resp.getWriter();
			out.write("Successfully Evict L2 Cache\n");
		}
	}
}
