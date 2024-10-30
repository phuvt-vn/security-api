package com.example.main.api.filter.config;

import com.example.main.api.filter.BasicAuthFilter;
import com.example.main.repository.BasicAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class BasicAuthFilterConfig {

    //@Autowired
    private BasicAuthRepository basicAuthRepository;

    @Bean
    public FilterRegistrationBean <BasicAuthFilter>basicAuthFilter() {
        var registrationBean = new FilterRegistrationBean<BasicAuthFilter>();
        registrationBean.setFilter(new BasicAuthFilter(basicAuthRepository));
        registrationBean.addUrlPatterns("/api/auth/basic/v1/time");
        return registrationBean;
    }
}
