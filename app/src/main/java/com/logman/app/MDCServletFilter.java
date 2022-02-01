package com.logman.app;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.MDC;

public class MDCServletFilter implements Filter {

	public static final String USER_NAME_MDC_KEY = "username";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		insertIntoMDC(request, "admin");
		try {
			chain.doFilter(request, response);
		} finally {
			clearMDC();
		}
	}

	void insertIntoMDC(ServletRequest request, String userName) {
		MDC.put(USER_NAME_MDC_KEY, userName);
//        MDC.put(ClassicConstants.REQUEST_REMOTE_HOST_MDC_KEY, request.getRemoteHost());
//
//        if (request instanceof HttpServletRequest) {
//            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//            MDC.put(ClassicConstants.REQUEST_REQUEST_URI, httpServletRequest.getRequestURI());
//            StringBuffer requestURL = httpServletRequest.getRequestURL();
//            if (requestURL != null) {
//                MDC.put(ClassicConstants.REQUEST_REQUEST_URL, requestURL.toString());
//            }
//            MDC.put(ClassicConstants.REQUEST_METHOD, httpServletRequest.getMethod());
//            MDC.put(ClassicConstants.REQUEST_QUERY_STRING, httpServletRequest.getQueryString());
//            MDC.put(ClassicConstants.REQUEST_USER_AGENT_MDC_KEY, httpServletRequest.getHeader("User-Agent"));
//            MDC.put(ClassicConstants.REQUEST_X_FORWARDED_FOR, httpServletRequest.getHeader("X-Forwarded-For"));
//        }

	}

	void clearMDC() {
		MDC.remove(USER_NAME_MDC_KEY);
//        MDC.remove(ClassicConstants.REQUEST_REMOTE_HOST_MDC_KEY);
//        MDC.remove(ClassicConstants.REQUEST_REQUEST_URI);
//        MDC.remove(ClassicConstants.REQUEST_QUERY_STRING);
//        // removing possibly inexistent item is OK
//        MDC.remove(ClassicConstants.REQUEST_REQUEST_URL);
//        MDC.remove(ClassicConstants.REQUEST_METHOD);
//        MDC.remove(ClassicConstants.REQUEST_USER_AGENT_MDC_KEY);
//        MDC.remove(ClassicConstants.REQUEST_X_FORWARDED_FOR);
	}

}
