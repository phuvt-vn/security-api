package com.example.main.api.filter;

import com.example.main.api.server.auth.basic.BasicAuthApi;
import com.example.main.repository.BasicAuthRepository;
import com.example.main.util.EncodeDecodeUtil;
import com.example.main.util.EncryptDecryptUtil;
import com.example.main.util.HashUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BasicAuthFilter extends OncePerRequestFilter {

    private BasicAuthRepository basicAuthRepository;

    public BasicAuthFilter(BasicAuthRepository basicAuthRepository) {
        this.basicAuthRepository = basicAuthRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        var basicAuthString = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            if(isValidBasicAuth(basicAuthString)){
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            }else {
                httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                httpServletResponse.setContentType(MediaType.TEXT_PLAIN_VALUE);
                httpServletResponse.getWriter().write("Invalid credentials");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValidBasicAuth(String basicAuthString) throws Exception {
        if(StringUtils.isBlank(basicAuthString)) {
            return false;
        }
        var encodeAuthorization = StringUtils.substring(basicAuthString, "Basic".length()).trim();
        var plainAuthorizationString = EncodeDecodeUtil.decodeBase64(encodeAuthorization);
        var plainAuthorization = plainAuthorizationString.split(":");
        var encryptUsername = EncryptDecryptUtil.encryptAES(plainAuthorization[0], BasicAuthApi.SECRET_KEY);
        var submittedPassword = plainAuthorization[1];

        var existingData = basicAuthRepository.findByUsername(encryptUsername);

        if(!existingData.isPresent()) {
            return false;
        }

        return HashUtil.isBcryptMatch(submittedPassword, existingData.get().getPasswordHash());
    }
}
