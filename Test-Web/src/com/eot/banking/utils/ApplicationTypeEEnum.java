package com.eot.banking.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * The Enum ApplicationTypeEnum.
 */
public class ApplicationTypeEEnum {
	
	/** The application type map. */
	private static Map<Integer, String> applicationTypeMap = new HashMap<Integer, String>();

	/** The admin. */
	public static final ApplicationTypeEEnum ADMIN = new ApplicationTypeEEnum( 1, "Admin" );

	/** The cbo. */
	public static final ApplicationTypeEEnum CBO = new ApplicationTypeEEnum( 2, "CBO" );

	/** The kyc. */
	public static final ApplicationTypeEEnum KYC = new ApplicationTypeEEnum( 3, "KYC" );

	/** The wallet. */
	public static final ApplicationTypeEEnum WALLET = new ApplicationTypeEEnum( 4, "Wallet" );
	
	/** The Constant BB. */
	public static final ApplicationTypeEEnum BB = new ApplicationTypeEEnum( 5, "BB" );
	
	/** The Constant REMIT. */
	public static final ApplicationTypeEEnum REMIT = new ApplicationTypeEEnum( 6, "REMIT" );
	
	/** The code. */
	private Integer code;
	
	/** The text. */
	private String text;
	
	/**
	 * Gets the terminal status map.
	 *
	 * @return the terminal status map
	 */
	public static Map<Integer, String> getApplicationTypeMap() {
		return applicationTypeMap;
	}
	/**
	 * Instantiates a new application type enum.
	 *
	 * @param code the code
	 * @param text the text
	 */
	private ApplicationTypeEEnum(Integer code, String text) {
		this.code = code;
		this.text = text;
		applicationTypeMap.put( code, text);
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
	 * Gets the message.
	 *
	 * @param code the code
	 * @return the message
	 */
	public static String getApplicationType( final Integer code ) {
		if ( applicationTypeMap.get( code ) != null )  {
			return applicationTypeMap.get( code );
		}
		return "";
	}
	
}
