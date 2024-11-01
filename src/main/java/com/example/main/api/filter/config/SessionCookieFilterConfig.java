package com.example.main.api.filter.config;

import com.example.main.api.filter.BasicAuthFilter;
import com.example.main.api.filter.SessionCookieAuthFilter;
import com.example.main.api.filter.SessionCookieTokenFilter;
import com.example.main.repository.BasicAuthRepository;
import com.example.main.service.SessionCookieTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SessionCookieFilterConfig {

    @Autowired
    private BasicAuthRepository basicAuthRepository;

    @Autowired
    private SessionCookieTokenService sessionCookieTokenService;

    @Bean
    public FilterRegistrationBean <SessionCookieAuthFilter>sessionCookieAuthFilter() {
        var registrationBean = new FilterRegistrationBean<SessionCookieAuthFilter>();
        registrationBean.setFilter(new SessionCookieAuthFilter(basicAuthRepository));
        registrationBean.addUrlPatterns("/api/auth/session-cookie/v1/login");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean <SessionCookieTokenFilter>sessionCookieTokenFilter() {
        var registrationBean = new FilterRegistrationBean<SessionCookieTokenFilter>();
        registrationBean.setFilter(new SessionCookieTokenFilter(sessionCookieTokenService));
        registrationBean.addUrlPatterns("/api/auth/session-cookie/v1/time");
        registrationBean.addUrlPatterns("/api/auth/session-cookie/v1/logout");
        return registrationBean;
    }
}
