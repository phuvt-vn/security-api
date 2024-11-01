package com.example.main.api.filter;

import com.example.main.constant.SessionCookieConstant;
import com.example.main.service.SessionCookieTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SessionCookieTokenFilter extends OncePerRequestFilter {

    private SessionCookieTokenService sessionCookieTokenService;

    public SessionCookieTokenFilter(SessionCookieTokenService sessionCookieTokenService) {
        this.sessionCookieTokenService = sessionCookieTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if(isValidSessionCookie(httpServletRequest)){
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } else {
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setContentType(MediaType.TEXT_PLAIN_VALUE);
            httpServletResponse.getWriter().println("Invalid token");
        }
    }

    private boolean isValidSessionCookie(HttpServletRequest request) {
        var providedTokenId = request.getHeader("X-CSRF");
        var token = sessionCookieTokenService.read(request, providedTokenId);
        if(token.isPresent()){
            request.setAttribute(SessionCookieConstant.REQUEST_ATTRIBUTE_USERNAME,token.get().getUsername());
            return true;
        }
        return false;
    }
}
