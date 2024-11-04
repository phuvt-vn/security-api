package com.example.main.api.filter;

import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.main.constant.RedisConstant;
import com.example.main.service.RedisTokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.net.HttpHeaders;

public class RedisTokenFilter extends OncePerRequestFilter {

	private RedisTokenService tokenService;

	public RedisTokenFilter(RedisTokenService tokenService) {
		this.tokenService = tokenService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
		try {
			if (isValidRedis(request)) {
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

	private boolean isValidRedis(HttpServletRequest request)
			throws JsonMappingException, InvalidKeyException, NoSuchAlgorithmException, JsonProcessingException {
		var authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (!StringUtils.startsWith(authorizationHeader, "Bearer")) {
			return false;
		}

		var bearerToken = StringUtils.substring(authorizationHeader, "Bearer".length() + 1);
		var token = tokenService.read(bearerToken);

		if (token.isPresent()) {
			request.setAttribute(RedisConstant.REQUEST_ATTRIBUTE_USERNAME, token.get().getUsername());
			return true;
		} else {
			return false;
		}
	}
}
