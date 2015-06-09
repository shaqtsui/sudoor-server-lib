package net.gplatform.sudoor.server.constant;

public class SudoorConstants {
	/**
	 * Default Spring Security Filter Chain filter order is 0.
	 */
	public static final int ORDER_FILTER_CORS = -1000;
	
	public static final int ORDER_FILTER_MDC_INSERTING = -3000;
	
	public static final int ORDER_FILTER_MDC_SESSION_INSERTING = -2000;
	
}
