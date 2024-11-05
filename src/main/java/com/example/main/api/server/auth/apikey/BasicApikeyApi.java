package com.example.main.api.server.auth.apikey;

import javax.servlet.http.HttpServletRequest;

import com.example.main.constant.ApikeyConstant;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth/apikey/v1")
public class BasicApikeyApi {

	@GetMapping(value = "/add", produces = MediaType.TEXT_PLAIN_VALUE)
	public String add(@RequestParam(required = true, name = "a") int a,
			@RequestParam(required = true, name = "b") int b, HttpServletRequest request) {
		return "Result is " + (a + b) + ", access by "
				+ request.getAttribute(ApikeyConstant.REQUEST_ATTRIBUTE_USERNAME);
	}

}
