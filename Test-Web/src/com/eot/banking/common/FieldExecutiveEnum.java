package com.eot.banking.common;

import java.util.HashMap;
import java.util.Map;

public enum FieldExecutiveEnum {

	CUSTOMER(0, "Customer"),
	
	AGENT(1, "Agent"),
	
	SOLE_MERCHANT(2, "Sole Merchant"),
	
	ASM(3, "ASM");
	
	/** The name. */
	private String name;

	/** The code. */
	private Integer code;
	
	private static Map<Integer, String> fieldExecutiveMap = new HashMap<Integer, String>();
	
	static {
		for ( FieldExecutiveEnum status : FieldExecutiveEnum.values() ) {
			fieldExecutiveMap.put( status.getCode(), status.getName() );
		}
	}

	FieldExecutiveEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	
	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public Integer getCode() {
		return code;
	}
	
	public static String getEnumText(Integer enumValue) {
		
		for(FieldExecutiveEnum s:FieldExecutiveEnum.values())
		{
		if(s.getCode().equals(enumValue))
		
			return s.getName();
		}
		return "";
	}
		
	public static Map<Integer, String> getFieldExecutiveMap() {
		return fieldExecutiveMap;
	}
	
	public static String getFieldExecutiveMap( final Integer code ) {
		if ( fieldExecutiveMap.get( code ) != null )  {
			return fieldExecutiveMap.get( code );
		}
		return "";
	}

}
