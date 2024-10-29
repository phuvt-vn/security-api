package com.example.main.api.server.util;

import com.example.main.api.request.util.HmacRequest;
import com.example.main.util.HmacUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hmac")
public class HmacApi {

	public static final String SECRET_KEY = "The123HmacSecretKey";
	private static final String MESSAGE_DELIMITER = "\n";

	@GetMapping(value = "/calculate", produces = MediaType.TEXT_PLAIN_VALUE)
	public String hmac(@RequestHeader(name = "X-Verb-Calculate", required = true) String verbCalculate,
			@RequestHeader(name = "X-Uri-Calculate", required = true) String uriCalculate,
			@RequestHeader(name = "X-Register-Date", required = true) String registerDate,
			@RequestHeader(name = "X-Nonce", required = true) String nonce,
			@RequestBody(required = true) HmacRequest requestBody) throws Exception {
		var hmacMessage = constructHmacMessage(verbCalculate, uriCalculate, requestBody.getAmount(),
				requestBody.getFullName(), registerDate, nonce);

		return HmacUtil.hmac(hmacMessage, SECRET_KEY);
	}

	public static String constructHmacMessage(String verb, String requestURI, int amount, String fullName,
			String xRegisterDate, String nonce) {
		var sb = new StringBuilder();

		sb.append(verb.toLowerCase());
		sb.append(MESSAGE_DELIMITER);
		sb.append(requestURI);
		sb.append(MESSAGE_DELIMITER);
		sb.append(amount);
		sb.append(MESSAGE_DELIMITER);
		sb.append(fullName);
		sb.append(MESSAGE_DELIMITER);
		sb.append(xRegisterDate);
		sb.append(nonce);

		return sb.toString();
	}

	@RequestMapping(value = "/info", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String info(@RequestBody(required = true) HmacRequest original) {
		return "The request body is " + original.toString();
	}

}
