/* Copyright © EasOfTech 2015. All rights reserved.
*
* This software is the confidential and proprietary information
* of EasOfTech. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EasOfTech.
*
* Id: CSRFTokenUtil.java
*
* Date Author Changes
* 19 Dec, 2015 Saroj.Biswal Created
*/
package com.eot.banking.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * The Class CSRFTokenUtil.
 */
public final class CSRFTokenUtil {
	
	/** The Constant DEFAULT_PRNG. */
	private final static String DEFAULT_PRNG = "SHA1PRNG";
	
	/** The Constant SESSION_ATTR_KEY. */
	public final static String SESSION_ATTR_KEY = "csrfToken";
	
	/** The Constant NO_SESSION_ERROR. */
	private final static String NO_SESSION_ERROR = "No valid session found";
	
	/** The unique per request. */
	private static boolean uniquePerRequest = true;

	/**
	 * Gets the token.
	 *
	 * @return the token
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	private static String getToken() throws NoSuchAlgorithmException {
		return getToken(DEFAULT_PRNG);
	}

	/**
	 * Gets the token.
	 *
	 * @param prng the prng
	 * @return the token
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	private static String getToken(String prng) throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstance(prng);
		return "" + sr.nextLong();
	}

	/**
	 * Gets the token.
	 *
	 * @param session the session
	 * @return the token
	 * @throws ServletException the servlet exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	public static String getToken(HttpSession session) throws ServletException,
			NoSuchAlgorithmException {
		// throw exception if session is null
		if (session == null) {
			throw new ServletException(NO_SESSION_ERROR);
		}

		// Now attempt to retrieve existing token from session. If it doesn't
		// exist then
		// add it
		String last_token_val = (String) session.getAttribute(SESSION_ATTR_KEY);
		if (last_token_val == null || uniquePerRequest) {
			String new_token_val = getToken();
			if (last_token_val == null) {
				last_token_val = new_token_val;
			}
			session.setAttribute(SESSION_ATTR_KEY, new_token_val);
		}
		return last_token_val;
	}

	/**
	 * Checks if is valid.
	 *
	 * @param request the request
	 * @return true, if is valid
	 * @throws ServletException the servlet exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	public static boolean isValid(HttpServletRequest request)
			throws ServletException, NoSuchAlgorithmException {
		// throw exception if session is null
		if (request.getSession(false) == null) {
			throw new ServletException(NO_SESSION_ERROR);
		}
		String lastToken = getToken(request.getSession(false));
		System.out.println("LastToken: "+lastToken);
		String requestToken = request.getParameter("csrfToken");
		System.out.println("Request Token: "+requestToken);
		return lastToken.equals(requestToken);
	}

}
