package com.eot.banking.common;

import java.util.HashMap;
import java.util.Map;

public enum KycStatusEnum {

	KYC_PENDING(0, "KYC Pending"),
	
	KYC_APPROVE_PENDING(1, "KYC Approval Pending"),
	
	KYC_APPROVED(11, "KYC Approved"),
	
	KYC_REJECTED(21, "KYC Rejected");
	
	
	/** The name. */
	private String name;

	/** The code. */
	private Integer code;
	
	private static Map<Integer, String> KycStatuMap = new HashMap<Integer, String>();
	
	static {
		for ( KycStatusEnum status : KycStatusEnum.values() ) {
			KycStatuMap.put( status.getCode(), status.getName() );
		}
	}

	KycStatusEnum(Integer code, String name) {
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
		
		for(KycStatusEnum s:KycStatusEnum.values())
		{
		if(s.getCode().equals(enumValue))
		
			return s.getName();
		}
		return "";
	}
		
	public static Map<Integer, String> getKycStatuMap() {
		return KycStatuMap;
	}
	
	public static String getKycStatuMap( final Integer code ) {
		if ( KycStatuMap.get( code ) != null )  {
			return KycStatuMap.get( code );
		}
		return "";
	}

}
