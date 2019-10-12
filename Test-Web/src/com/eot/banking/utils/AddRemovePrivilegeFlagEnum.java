package com.eot.banking.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * The Enum AddRemovePrivilegeFlagEnum.
 */
public enum AddRemovePrivilegeFlagEnum {

	
	/** The added. */
	ADDED( 1, "Added" ),
	
	/** The removed. */
	REMOVED( 0, "Removed" );
	
	/** The code. */
	private Integer code;
	
	/** The text. */
	private String text;

	
	/** The add remove privilege flag map. */
	private static Map<Integer, String> addRemovePrivilegeFlagMap = new HashMap<Integer, String>();

	static {
		for ( AddRemovePrivilegeFlagEnum addRemovePrivilegeFlagEnum : AddRemovePrivilegeFlagEnum.values() ) {
			addRemovePrivilegeFlagMap.put( addRemovePrivilegeFlagEnum.getCode(), addRemovePrivilegeFlagEnum.getText());
		}
	}
	
	
	/**
	 * Instantiates a new adds the remove privilege flag enum.
	 *
	 * @param code the code
	 * @param text the text
	 */
	private AddRemovePrivilegeFlagEnum(int code, String text) {
		this.code = code;
		this.text = text;
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
	 * Gets the adds the remove privilege flag map.
	 *
	 * @return the adds the remove privilege flag map
	 */
	public static Map<Integer, String> getAddRemovePrivilegeFlagMap() {
		return addRemovePrivilegeFlagMap;
	}
	
}
