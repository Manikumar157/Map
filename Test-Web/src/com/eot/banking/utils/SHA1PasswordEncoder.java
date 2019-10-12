package com.eot.banking.utils;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;

import com.eot.banking.common.EOTConstants;

public class SHA1PasswordEncoder implements PasswordEncoder, SaltSource {

	@Override
	public String encodePassword(String arg0, Object arg1) throws DataAccessException {
		return null;
	}

	@Override
	public boolean isPasswordValid(String encPass, String rawPass, Object salt) throws DataAccessException {
		
		try {
			String passwordHash = HashUtil.generateHash(rawPass.getBytes(), EOTConstants.PIN_HASH_ALGORITHM) ;
			
			if(encPass.equals(passwordHash)){
				return true;
			}else{
				return false;
			}
		} catch (Exception e1) {
			return false;
		}
		
	}

	@Override
	public Object getSalt(UserDetails arg0) {
		return "";
	}

}
