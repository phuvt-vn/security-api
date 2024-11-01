package com.example.main.service;

import com.example.main.constant.SessionCookieConstant;
import com.example.main.entity.SessionCookieToken;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class SessionCookieTokenService {

    public String store(HttpServletRequest request, SessionCookieToken token) {
        var session = request.getSession(true);
        session.setAttribute(SessionCookieConstant.SESSION_ATTRIBUTE_USERNAME, token.getUsername());
        return session.getId();
    }

    public Optional<SessionCookieToken> read(HttpServletRequest request) {
        var session = request.getSession(false);
        if (session == null) {
            return Optional.empty();
        }
        var username = (String)session.getAttribute(SessionCookieConstant.SESSION_ATTRIBUTE_USERNAME);
        var token = new SessionCookieToken();
        token.setUsername(username);
        return Optional.of(token);
    }

}
