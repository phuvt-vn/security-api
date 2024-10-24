package com.example.main.api.filter.config;

import com.example.main.api.filter.RateLimitFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//@Configuration
public class RateLimitFilterConfig {

	@Bean
	public FilterRegistrationBean<RateLimitFilter> rateLimitFilter_Blue() {
		var registrationBean = new FilterRegistrationBean<RateLimitFilter>();

		registrationBean.setFilter(new RateLimitFilter(3));
		registrationBean.setName("rateLimitFilter_Blue");
		registrationBean.addUrlPatterns("/api/dos/v1/blue");

		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<RateLimitFilter> rateLimitFilter_Red() {
		var registrationBean = new FilterRegistrationBean<RateLimitFilter>();

		registrationBean.setFilter(new RateLimitFilter(2));
		registrationBean.setName("rateLimitFilter_Red");
		registrationBean.addUrlPatterns("/api/dos/v1/red");

		return registrationBean;
	}

}
