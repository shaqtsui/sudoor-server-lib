package net.gplatform.sudoor.server.ss;

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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 14-3-12.
 * This class object must be registered & retrieved from spring context, as @PreAuthorize need sping AOP support.
 */

@Component
public class SSChecker {
	final Logger logger = LoggerFactory.getLogger(SSChecker.class);

	@PreAuthorize("hasPermission(#target, #method)")
	public void check(Object target, String method) {
		logger.debug("Grant permission [{}] to name [{}]", method, target);
	}
}
