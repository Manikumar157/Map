package com.eot.banking.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * The Enum FrontendAppTypeEnum.
 */
public enum DevAppTypeEnum {

	/** The admin. */
	FI_ENROLLMENT_TRANSACTION( "1", "FI Enrollment Transaction", "FI" ),
	
	/** The cbo member enrollment. */
	CBO_MEMBER_ENROLLMENT( "2", "Family Data Collection Application", "FDC" ),
	
	/** The cbo group meeting. */
	CBO_GROUP_MEETING( "3", "Group Meeting Application", "GMT" ),
	
	/** The wallet. */
	WALLET( "4", "Wallet", "WALLET" );
	
	/** The code. */
	private String code;
	
	/** The text. */
	private String text;
	
	/** The short name. */
	private String shortName;
	
	/** The application type map. */
	private static Map<String, String> devAppTypeMap = new HashMap<String, String>();
	
	/** The dev app type short name map. */
	private static Map<String, String> devAppTypeShortNameMap = new HashMap<String, String>();

	static {
		for ( DevAppTypeEnum frontendAppTypeEnum : DevAppTypeEnum.values() ) {
			devAppTypeMap.put( frontendAppTypeEnum.getCode(), frontendAppTypeEnum.getText() );
			devAppTypeShortNameMap.put( frontendAppTypeEnum.getCode(), frontendAppTypeEnum.getShortName() );
		}
	}
	
	/**
	 * Instantiates a new application type enum.
	 *
	 * @param code the code
	 * @param text the text
	 * @param shortName the short name
	 */
	private DevAppTypeEnum(String code, String text, String shortName) {
		this.code = code;
		this.text = text;
		this.shortName = shortName;
	}
	
	/**
	 * Gets the frontend app type map.
	 *
	 * @return the frontend app type map
	 */
	public static Map<String, String> getFrontendAppTypeMap() {
		return devAppTypeMap;
	}
	

	/**
	 * Gets the dev app type short name map.
	 *
	 * @return the dev app type short name map
	 */
	public static Map<String, String> getDevAppTypeShortNameMap() {
		return devAppTypeShortNameMap;
	}

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public String getCode() {
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
	 * Gets the short name.
	 *
	 * @return the short name
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * Gets the message.
	 *
	 * @param code the code
	 * @return the message
	 */
	public static String getApplicationType( final String code ) {
		if ( devAppTypeMap.get( code ) != null )  {
			return devAppTypeMap.get( code );
		}
		return "";
	}
	
	/**
	 * Gets the application short name.
	 *
	 * @param code the code
	 * @return the application short name
	 */
	public static String getApplicationShortName( final String code ) {
		if ( devAppTypeShortNameMap.get( code ) != null )  {
			return devAppTypeShortNameMap.get( code );
		}
		return code;
	}
	
}
