package com.example.main.service;

import com.example.main.constant.SessionCookieConstant;
import com.example.main.entity.SessionCookieToken;
import com.example.main.util.HashUtil;
import com.example.main.util.SecureStringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class SessionCookieTokenService {

    public String store(HttpServletRequest request, SessionCookieToken token) {
        var session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        session = request.getSession(true);

        session.setAttribute(SessionCookieConstant.SESSION_ATTRIBUTE_USERNAME, token.getUsername());
        try {
            return HashUtil.sha256(session.getId(), token.getUsername());
        } catch (Exception e) {
           e.printStackTrace();
           return StringUtils.EMPTY;
        }
    }

    public Optional<SessionCookieToken> read(HttpServletRequest request, String providedTokenId) {
        var session = request.getSession(false);
        if (session == null) {
            return Optional.empty();
        }
        var username = (String)session.getAttribute(SessionCookieConstant.SESSION_ATTRIBUTE_USERNAME);

        try {
            var computedTokenId = HashUtil.sha256(session.getId(), username);

            if(!SecureStringUtil.equals(providedTokenId, computedTokenId)){
                return Optional.empty();
            }

            var token = new SessionCookieToken();
            token.setUsername(username);
            return Optional.of(token);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void delete(HttpServletRequest request){
        var session = request.getSession(false);
        session.invalidate();
    }
}
