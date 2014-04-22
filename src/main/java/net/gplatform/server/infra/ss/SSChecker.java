package net.gplatform.server.infra.ss;

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
