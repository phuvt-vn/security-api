package com.example.main.util;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;

public class HmacUtil {

	private static final String HMAC_SHA256 = "HmacSHA256";

	public static String hmac(String message, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
		Mac mac = Mac.getInstance(HMAC_SHA256);
		SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
		mac.init(secretKeySpec);

		var hmacBytes = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));

		return new String(Hex.encode(hmacBytes));
	}

	public static boolean isHmacMatch(String message, String secretKey, String hmacValue) throws InvalidKeyException, NoSuchAlgorithmException {
		var reHmacValue = hmac(message, secretKey);

		return StringUtils.equals(hmacValue, reHmacValue);
	}

}
