package com.example.main.api.filter;

import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.main.constant.JwtConstant;
import com.example.main.service.JwtService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;


import com.google.common.net.HttpHeaders;

public class JwtFilter extends OncePerRequestFilter {

	private JwtService jwtService;

	public JwtFilter(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
		try {
			if (isValidJwt(request)) {
				chain.doFilter(request, response);
			} else {
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				PrintWriter writer = response.getWriter();
				writer.print("{\"message\":\"Invalid token\"}");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isValidJwt(HttpServletRequest request) {
		var authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (!StringUtils.startsWith(authorizationHeader, "Bearer")) {
			return false;
		}

		var jwt = StringUtils.substring(authorizationHeader, "Bearer".length() + 1);
		var jwtData = jwtService.read(jwt);

		if (jwtData.isPresent()) {
			request.setAttribute(JwtConstant.REQUEST_ATTRIBUTE_USERNAME, jwtData.get().getUsername());
			return true;
		} else {
			return false;
		}
	}
}
