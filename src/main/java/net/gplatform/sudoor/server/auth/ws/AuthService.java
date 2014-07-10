package net.gplatform.sudoor.server.auth.ws;

import javax.jws.WebService;

import org.apache.cxf.feature.Features;

@WebService
@Features(features = "org.apache.cxf.feature.LoggingFeature")
public interface AuthService {
	public String authenticate();
}
