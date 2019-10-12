package com.eot.banking.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * The Class NoCacheFilter.
 */
public class NoCacheFilter implements Filter {

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {

	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletResponse hsr = (HttpServletResponse) servletResponse;
		hsr.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP
																				// 1.1.
		hsr.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		hsr.setDateHeader("Expires", 0); // Proxies.
		filterChain.doFilter(servletRequest, servletResponse);

	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
