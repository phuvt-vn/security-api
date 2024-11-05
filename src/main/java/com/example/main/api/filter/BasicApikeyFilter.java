package com.example.main.api.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.main.constant.ApikeyConstant;
import com.example.main.repository.BasicApikeyRepository;
import com.example.main.repository.BasicAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;


//@Configuration
public class BasicApikeyFilter extends OncePerRequestFilter {

	//@Autowired
	private BasicAuthRepository basicAuthUserRepository;

	//@Autowired
	private BasicApikeyRepository basicApikeyRepository;

	private boolean isValidApikey(String apikey, HttpServletRequest request) throws Exception {
		// find valid api key
		var apikeyFromStorage = basicApikeyRepository.findByApikeyAndExpiredAtAfter(apikey, LocalDateTime.now());

		if (apikeyFromStorage.isPresent()) {
			var currentUser = basicAuthUserRepository.findById(apikeyFromStorage.get().getUserId());
			request.setAttribute(ApikeyConstant.REQUEST_ATTRIBUTE_USERNAME, currentUser.get().getUsername());

			return true;
		}

		return false;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		var apikey = request.getHeader("X-Apikey");

		try {
			if (isValidApikey(apikey, request)) {
				chain.doFilter(request, response);
			} else {
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				PrintWriter writer = response.getWriter();
				writer.print("{\"message\":\"Invalid credential\"}");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
