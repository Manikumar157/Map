package com.eot.banking.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.eot.banking.common.EOTConstants;
import com.thinkways.util.HexString;

public class HashUtil {
	
	public static String generateHash(byte[] data,String algorithm) throws NoSuchAlgorithmException, UnsupportedEncodingException  {

		MessageDigest digest = MessageDigest.getInstance(algorithm);
		byte[] hash = new byte[32];
		digest.update(data, 0, data.length);
		hash = digest.digest();
		return HexString.bufferToHex(hash);

	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		
		String str = HashUtil.generateHash("11111111".getBytes(), EOTConstants.PIN_HASH_ALGORITHM);
		
		System.out.println(str);
		
	}
	
}

