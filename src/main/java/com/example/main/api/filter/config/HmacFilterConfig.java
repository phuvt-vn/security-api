package com.example.main.api.filter.config;

import com.example.main.api.filter.HmacFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//@Configuration
public class HmacFilterConfig {

	@Bean
	public FilterRegistrationBean<HmacFilter> hmacFilter() {
		var registrationBean = new FilterRegistrationBean<HmacFilter>();

		registrationBean.setFilter(new HmacFilter());
		registrationBean.addUrlPatterns("/api/hmac/info");

		return registrationBean;
	}

}
