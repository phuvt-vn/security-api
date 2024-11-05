package com.example.main.api.filter.config;

import com.example.main.api.filter.RedisJwtAuthFilter;
import com.example.main.api.filter.RedisJwtFilter;
import com.example.main.repository.BasicAuthRepository;
import com.example.main.service.RedisJwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//@Configuration
public class RedisJwtFilterConfig {

	//@Autowired
	private BasicAuthRepository basicAuthUserRepository;

	//@Autowired
	private RedisJwtService jwtService;

	@Bean
	public FilterRegistrationBean<RedisJwtAuthFilter> redisJwtAuthFilter() {
		var registrationBean = new FilterRegistrationBean<RedisJwtAuthFilter>();

		registrationBean.setFilter(new RedisJwtAuthFilter(basicAuthUserRepository));
		registrationBean.addUrlPatterns("/api/auth/redis-jwt/v1/login");

		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<RedisJwtFilter> redisJwtFilter() {
		var registrationBean = new FilterRegistrationBean<RedisJwtFilter>();

		registrationBean.setFilter(new RedisJwtFilter(jwtService));
		registrationBean.addUrlPatterns("/api/auth/redis-jwt/v1/time");
		registrationBean.addUrlPatterns("/api/auth/redis-jwt/v1/logout");

		return registrationBean;
	}

}
