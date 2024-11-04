package com.example.main.api.filter.config;

import com.example.main.api.filter.RedisAuthFilter;
import com.example.main.api.filter.RedisTokenFilter;
import com.example.main.api.filter.SessionCookieAuthFilter;
import com.example.main.api.filter.SessionCookieTokenFilter;
import com.example.main.repository.BasicAuthRepository;
import com.example.main.service.RedisTokenService;
import com.example.main.service.SessionCookieTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class RedisFilterConfig {

	//@Autowired
	private BasicAuthRepository basicAuthRepository;

	//@Autowired
	private RedisTokenService redisTokenService;

	@Bean
	public FilterRegistrationBean <RedisAuthFilter>sessionCookieAuthFilter() {
		var registrationBean = new FilterRegistrationBean<RedisAuthFilter>();
		registrationBean.setFilter(new RedisAuthFilter(basicAuthRepository));
		registrationBean.addUrlPatterns("/api/auth/redis/v1/login");
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean <RedisTokenFilter>sessionCookieTokenFilter() {
		var registrationBean = new FilterRegistrationBean<RedisTokenFilter>();
		registrationBean.setFilter(new RedisTokenFilter(redisTokenService));
		registrationBean.addUrlPatterns("/api/auth/redis/v1/time");
		registrationBean.addUrlPatterns("/api/auth/redis/v1/logout");
		return registrationBean;
	}
}
