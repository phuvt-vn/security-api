package com.example.main.api.filter;

import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.main.constant.JweConstant;
import com.example.main.service.JweService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;


import com.google.common.net.HttpHeaders;

public class JweFilter extends OncePerRequestFilter {

	private JweService jweService;

	public JweFilter(JweService jwtService) {
		this.jweService = jwtService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
		try {
			if (isValidJwe(request)) {
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

	private boolean isValidJwe(HttpServletRequest request) {
		var authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (!StringUtils.startsWith(authorizationHeader, "Bearer")) {
			return false;
		}

		var jwt = StringUtils.substring(authorizationHeader, "Bearer".length() + 1);
		var jwtData = jweService.read(jwt);

		if (jwtData.isPresent()) {
			request.setAttribute(JweConstant.REQUEST_ATTRIBUTE_USERNAME, jwtData.get().getUsername());
			return true;
		} else {
			return false;
		}
	}
}
