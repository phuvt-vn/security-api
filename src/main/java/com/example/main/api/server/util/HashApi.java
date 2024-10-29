package com.example.main.api.server.util;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.example.main.api.request.util.OriginalStringRequest;
import com.example.main.util.HashUtil;
import com.example.main.util.SecureStringUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hash")
public class HashApi {

	private static final int SALT_LENGTH = 16;

	private final Map<String, String> mapSalt = new HashMap<>();

	@GetMapping(value = "/sha256", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String sha256(@RequestBody(required = true) OriginalStringRequest original) throws Exception {
		var salt = SecureStringUtil.randomString(SALT_LENGTH);
		mapSalt.put(original.getText(), salt);
		return HashUtil.sha256(original.getText(), salt);
	}

	@GetMapping(value = "/sha256/match", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String sha256Match(@RequestHeader(name = "X-Hash", required = true) String hash,
			@RequestBody(required = true) OriginalStringRequest original) throws Exception {
		var salt = Optional.ofNullable(mapSalt.get(original.getText()));
		return HashUtil.isSha256Match(original.getText(), salt.get(), hash) ? "match" : "not match";
	}

	@GetMapping(value = "/bcrypt", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String bcrypt(@RequestBody(required = true) OriginalStringRequest original) throws NoSuchAlgorithmException {
		var salt = SecureStringUtil.randomString(SALT_LENGTH);
		return HashUtil.bcrypt(original.getText(), salt);
	}

	@GetMapping(value = "/bcrypt/match", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String bcryptMatch(@RequestHeader(name = "X-Hash", required = true) String hash,
							  @RequestBody(required = true) OriginalStringRequest original) throws NoSuchAlgorithmException {
		return HashUtil.isBcryptMatch(original.getText(), hash) ? "match" : "NOT match";
	}

}
