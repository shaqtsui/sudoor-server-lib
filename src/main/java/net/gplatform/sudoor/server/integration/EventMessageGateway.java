package net.gplatform.sudoor.server.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface EventMessageGateway {

	@Gateway(requestChannel = "eventPublishChannel")
	public void publishEvent(Object event);

}
