package com.example.main.api.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

//@Component
public class XssRequestFilter extends OncePerRequestFilter {

	private static final String[] XSS_REGEX = {
			"onclick|onkeypress|onkeydown|onkeyup|onerror|onchange|onmouseover|onmouseout|onblur|onselect|onfocus",
			"<\s*script\b[^>]*>(.*?)<\s*/script\b[^>]*>", "script\s+src\s*=", "<\s*script\b[^>]*>",
			"<\s*/script\b[^>]*>", "javascript.*:" };

	private List<Pattern> xssValidationPatterns;

	public XssRequestFilter() {
		xssValidationPatterns = new ArrayList<Pattern>();

		for (String xssScript : XSS_REGEX) {
			var pattern = Pattern.compile(xssScript, Pattern.CASE_INSENSITIVE);
			xssValidationPatterns.add(pattern);
		}
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		var cachedHttpRequest = new CachedBodyHttpServletRequest(request);

		var queryString = URLDecoder.decode(
				Optional.ofNullable(cachedHttpRequest.getQueryString()).orElse(StringUtils.EMPTY),
				StandardCharsets.UTF_8);
		var pathVariables = URLDecoder.decode(
				Optional.ofNullable(cachedHttpRequest.getRequestURI()).orElse(StringUtils.EMPTY),
				StandardCharsets.UTF_8);
		var requestBody = IOUtils.toString(cachedHttpRequest.getReader()).replaceAll("\\r\\n|\\r|\\n", "");

		if (isXssSafe(queryString) && isXssSafe(pathVariables) && isXssSafe(requestBody)) {
			chain.doFilter(cachedHttpRequest, response);
		} else {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			PrintWriter writer = response.getWriter();
			writer.print("{\"message\":\"XSS detected\"}");
			return;
		}
	}

	private boolean isXssSafe(String stringToValidate) {
		if (StringUtils.isBlank(stringToValidate)) {
			return true;
		}

		for (var pattern : xssValidationPatterns) {
			if (pattern.matcher(stringToValidate).find()) {
				return false;
			}
		}

		return true;
	}

}
