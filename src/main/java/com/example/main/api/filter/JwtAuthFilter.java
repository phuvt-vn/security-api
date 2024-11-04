package com.example.main.api.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.main.api.server.auth.basic.BasicAuthApi;
import com.example.main.constant.SessionCookieConstant;
import com.example.main.repository.BasicAuthRepository;
import com.example.main.util.EncodeDecodeUtil;
import com.example.main.util.EncryptDecryptUtil;
import com.example.main.util.HashUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;


public class JwtAuthFilter extends OncePerRequestFilter {


	private BasicAuthRepository basicAuthUserRepository;

	public JwtAuthFilter(BasicAuthRepository basicAuthUserRepository) {
		this.basicAuthUserRepository = basicAuthUserRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		var basicAuthString = request.getHeader("Authorization");

		try {
			if (isValidBasicAuth(basicAuthString, request)) {
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

	private boolean isValidBasicAuth(String basicAuthString, HttpServletRequest request) throws Exception {
		try {
			var encodedAuthorizationString = StringUtils.substring(basicAuthString, "Basic".length()).trim();
			var plainAuthorizationString = EncodeDecodeUtil.decodeBase64(encodedAuthorizationString);
			var plainAuthorization = plainAuthorizationString.split(":");

			var encryptedUsername = EncryptDecryptUtil.encryptAES(plainAuthorization[0], BasicAuthApi.SECRET_KEY);
			var submittedPassword = plainAuthorization[1];

			var existingData = basicAuthUserRepository.findByUsername(encryptedUsername);

			if (existingData.isEmpty()) {
				return false;
			}

			if (HashUtil.isBcryptMatch(submittedPassword, existingData.get().getPasswordHash())) {
				request.setAttribute(SessionCookieConstant.REQUEST_ATTRIBUTE_USERNAME, encryptedUsername);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
}
