package com.eot.banking.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * The Enum AccessTypeEnum.
 */
public enum AccessTypeEnum {

	/** The web access. */
	WEB_ACCESS( 1, "WebAccess" ),
	
	/** The device access. */
	DEVICE_ACCESS( 2, "DeviceAccess" );
	
	/** The code. */
	private Integer code;
	
	/** The text. */
	private String text;
	
	/** The access type map. */
	private static Map<Integer, String> accessTypeMap = new HashMap<Integer, String>();

	static {
		for ( AccessTypeEnum accessTypeEnum : AccessTypeEnum.values() ) {
			accessTypeMap.put( accessTypeEnum.getCode(), accessTypeEnum.getText() );
		}
	}
	
	/**
	 * Instantiates a new access type enum.
	 *
	 * @param code the code
	 * @param text the text
	 */
	private AccessTypeEnum(Integer code, String text) {
		this.code = code;
		this.text = text;
	}
	
	/**
	 * Gets the access type map.
	 *
	 * @return the access type map
	 */
	public static Map<Integer, String> getAccessTypeMap() {
		return accessTypeMap;
	}

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * Gets the application type.
	 *
	 * @param code the code
	 * @return the application type
	 */
	public static String getApplicationType( final String code ) {
		if ( accessTypeMap.get( code ) != null )  {
			return accessTypeMap.get( code );
		}
		return "";
	}
	
}
