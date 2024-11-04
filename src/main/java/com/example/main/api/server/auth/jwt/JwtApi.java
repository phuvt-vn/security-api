package com.example.main.api.server.auth.jwt;

import java.time.LocalTime;

import javax.servlet.http.HttpServletRequest;

import com.example.main.constant.JwtConstant;
import com.example.main.entity.JwtData;
import com.example.main.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("api/auth/jwt/v1")
public class JwtApi {

	@Autowired
	private JwtService service;

	@GetMapping("/time")
	public String time(HttpServletRequest request) {
		var encryptedUsername = request.getAttribute(JwtConstant.REQUEST_ATTRIBUTE_USERNAME);
		return "Now is " + LocalTime.now() + ", accessed by " + encryptedUsername;
	}

	@PostMapping(value = "/login", produces = MediaType.TEXT_PLAIN_VALUE)
	public String login(HttpServletRequest request) throws Exception {
		var encryptedUsername = (String) request.getAttribute(JwtConstant.REQUEST_ATTRIBUTE_USERNAME);

		var jwtData = new JwtData();
		jwtData.setUsername(encryptedUsername);

		var jwtToken = service.store(jwtData);

		return jwtToken;
	}

}
