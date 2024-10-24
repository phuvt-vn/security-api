package com.example.main.api.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

//@Component
public class WhitelistIpFilter extends OncePerRequestFilter {

	private static final String[] AUTHORIZED_IP = { "0:0:0:0:0:0:0:1", "10.182.36.77" };

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		if (!ArrayUtils.contains(AUTHORIZED_IP, request.getRemoteAddr())) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType(MediaType.TEXT_PLAIN_VALUE);
			PrintWriter writer = response.getWriter();
			writer.print("Forbidden IP : " + request.getRemoteAddr());
			return;
		}
		chain.doFilter(request, response);
	}

}
