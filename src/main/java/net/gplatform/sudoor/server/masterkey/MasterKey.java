package net.gplatform.sudoor.server.masterkey;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sudoor.masterkey")
public class MasterKey {
	final Logger logger = LoggerFactory.getLogger(MasterKey.class);

	String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isMasterAvailable() {
		return StringUtils.isNotBlank(value);
	}

	public boolean checkWithMasterKey(String targetKey) {
		if (isMasterAvailable()) {
			logger.warn(
					"Master key be used!!! Master Key should only be used for test purpose!!! U can comment out: sudoor.masterkey.value={} in application.properties to disable it",
					value);
			if (StringUtils.equals(value, targetKey)) {
				return true;
			}
		}
		return false;
	}

}
