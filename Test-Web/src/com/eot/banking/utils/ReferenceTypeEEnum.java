package com.eot.banking.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * The Enum ReferenceTypeEnum.
 */
public class ReferenceTypeEEnum {

	/** The reference type map. */
	private static Map<Integer, String> referenceTypeMap = new HashMap<Integer, String>();
	
	/** The Constant ROLE. */
	public static final ReferenceTypeEEnum ROLE = new ReferenceTypeEEnum( 1 , "Role" );
	
	/** The Constant USER. */
	public static final ReferenceTypeEEnum USER = new ReferenceTypeEEnum( 2 , "User" );	
	
	/** The code. */
	private Integer code;
	
	/** The text. */
	private String text;
	
	/**
	 * Gets the reference type map.
	 *
	 * @return the reference type map
	 */
	public static Map<Integer, String> getReferenceTypeMap() {
		return referenceTypeMap;
	}
	
	/**
	 * Instantiates a new reference type enum.
	 *
	 * @param code the code
	 * @param text the text
	 */
	public ReferenceTypeEEnum( Integer code, String text ) {
		this.code = code;
		this.text = text;
		String exisitngValue = referenceTypeMap.put( code, text );
		if( exisitngValue != null ) {
			
			throw new IllegalArgumentException("The code " + code + " already exists in ReferenceTypeEEnum");
		}
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

}
