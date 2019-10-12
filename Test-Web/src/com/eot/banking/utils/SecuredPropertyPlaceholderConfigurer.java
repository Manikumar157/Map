package com.eot.banking.utils;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class SecuredPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	
	@Override
	protected String convertPropertyValue(String originalValue) {
		
		DesEncrypter enc = new DesEncrypter() ;
		if( originalValue!=null && originalValue.startsWith("<") && originalValue.endsWith(">") ){
			return enc.decrypt(originalValue.substring(1,originalValue.length()-1));
		}else
			return originalValue;
	}

}
