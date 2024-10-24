package com.example.main.api.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.util.concurrent.RateLimiter;

public class RateLimitFilter extends OncePerRequestFilter {

	private RateLimiter rateLimiter;

	public RateLimitFilter(double tps) {
		this.rateLimiter = RateLimiter.create(tps);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		if (!rateLimiter.tryAcquire()) {
			response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
			response.setHeader(HttpHeaders.RETRY_AFTER, "5");

			return;
		}

		chain.doFilter(request, response);
	}

}
