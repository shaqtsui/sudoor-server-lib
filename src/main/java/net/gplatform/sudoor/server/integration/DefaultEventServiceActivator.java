package net.gplatform.sudoor.server.integration;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

@MessageEndpoint
public class DefaultEventServiceActivator {
	final Logger logger = LoggerFactory.getLogger(DefaultEventServiceActivator.class);

	@ServiceActivator(inputChannel = "eventPublishChannel")
	public void handle(Object event) throws Exception {
		logger.debug("Get Event: {} @ {}", event, new Date());
	}

}
