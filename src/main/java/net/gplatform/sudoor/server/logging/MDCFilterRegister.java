package net.gplatform.sudoor.server.logging;

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

import net.gplatform.sudoor.server.constant.SudoorConstants;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.qos.logback.classic.helpers.MDCInsertingServletFilter;

/**
 * Use this to dyna register MDCInsertingServletFilter, so no need to have
 * web-fragment.xml/web.xml
 * 
 * @author xufucheng
 *
 */
@Configuration
public class MDCFilterRegister {
	
	@Bean
	public FilterRegistrationBean mDCInsertingServletFilterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		MDCInsertingServletFilter mdcInsertingServletFilter = new MDCInsertingServletFilter();
	    registrationBean.setFilter(mdcInsertingServletFilter);
	    registrationBean.setOrder(SudoorConstants.ORDER_FILTER_MDC_INSERTING);
	    return registrationBean;
	}
}
