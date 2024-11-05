package com.example.main.api.server.auth.redisjwt;

import java.time.LocalTime;

import javax.servlet.http.HttpServletRequest;

import com.example.main.constant.RedisConstant;
import com.example.main.entity.RedisJwtData;
import com.example.main.service.RedisJwtService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/auth/redis-jwt/v1")
public class RedisJwtApi {

	@Autowired
	private RedisJwtService service;

	@GetMapping("/time")
	public String time(HttpServletRequest request) {
		var encryptedUsername = request.getAttribute(RedisConstant.REQUEST_ATTRIBUTE_USERNAME);
		return "Now is " + LocalTime.now() + ", accessed by " + encryptedUsername;
	}

	@PostMapping(value = "/login", produces = MediaType.TEXT_PLAIN_VALUE)
	public String login(HttpServletRequest request) throws Exception {
		var encryptedUsername = (String) request.getAttribute(RedisConstant.REQUEST_ATTRIBUTE_USERNAME);

		var jwtData = new RedisJwtData();
		jwtData.setUsername(encryptedUsername);

		var jwtToken = service.store(jwtData);

		return jwtToken;
	}

	@DeleteMapping("/logout")
	public ResponseEntity<String> logout(
			@RequestHeader(required = true, name = "Authorization") String authorizationHeader) {
		var token = StringUtils.substring(authorizationHeader, "Bearer".length() + 1);

		service.delete(token);
		return ResponseEntity.ok().body("Logged out");
	}

}
