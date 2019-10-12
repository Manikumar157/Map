package com.eot.test;

import java.security.MessageDigest;

public class HashTest {

	public static void main(String[] args) throws Exception {
		
		String str ="26746362";

		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		
		digest.update(str.getBytes());
		
		byte[] bt = digest.digest();
		
		System.out.println(bt.length);
		
		System.out.println(new String(bt));
		
	}

}
