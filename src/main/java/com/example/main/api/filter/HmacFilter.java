package com.example.main.api.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.main.api.request.util.HmacRequest;
import com.example.main.api.server.util.HmacApi;
import com.example.main.util.HmacNonceStorage;
import com.example.main.util.HmacUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;


import com.fasterxml.jackson.databind.ObjectMapper;

public class HmacFilter extends OncePerRequestFilter {

	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		var cachedHttpRequest = new CachedBodyHttpServletRequest(request);
		var nonce = request.getHeader("X-Nonce");

		try {
			if (isValidHmac(cachedHttpRequest, nonce) && HmacNonceStorage.addWhenNotExists(nonce)) {
				chain.doFilter(cachedHttpRequest, response);
			} else {
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				PrintWriter writer = response.getWriter();
				writer.print("{\"message\":\"HMAC signature invalid\"}");
            }
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private boolean isValidHmac(CachedBodyHttpServletRequest httpRequest, String nonce) throws Exception {
		var requestBody = objectMapper.readValue(httpRequest.getReader(), HmacRequest.class);
		var xRegisterDate = httpRequest.getHeader("X-Register-Date");
		var hmacFromClient = httpRequest.getHeader("X-Hmac");
		var hmacMessage = HmacApi.constructHmacMessage(httpRequest.getMethod(), httpRequest.getRequestURI(),
				requestBody.getAmount(), requestBody.getFullName(), xRegisterDate, nonce);

		return HmacUtil.isHmacMatch(hmacMessage, HmacApi.SECRET_KEY, hmacFromClient);
	}

}
