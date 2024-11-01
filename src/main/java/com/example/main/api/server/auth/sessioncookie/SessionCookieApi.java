package com.example.main.api.server.auth.sessioncookie;

import com.example.main.constant.SessionCookieConstant;
import com.example.main.entity.SessionCookieToken;
import com.example.main.service.SessionCookieTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/auth/session-cookie/v1")
public class SessionCookieApi {

    @Autowired
    private SessionCookieTokenService sessionCookieTokenService;

    @PostMapping(value = "/login", produces = MediaType.TEXT_PLAIN_VALUE)
    public String login (HttpServletRequest request) {
        var encryptUsername = (String)request.getAttribute(SessionCookieConstant.REQUEST_ATTRIBUTE_USERNAME);
        var token = new SessionCookieToken();
        token.setUsername(encryptUsername);

        var JSessionId = sessionCookieTokenService.store(request, token);
        return JSessionId;
    }

    @GetMapping(value="/time", produces = MediaType.TEXT_PLAIN_VALUE)
    public String time (HttpServletRequest request) {
        var encryptedUsername = request.getAttribute(SessionCookieConstant.REQUEST_ATTRIBUTE_USERNAME);

        return "Now is: "+ LocalTime.now() + ", access by: "+ encryptedUsername;
    }

}
