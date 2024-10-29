package com.example.main.util;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
public class HmacNonceStorage {

	private static final Map<String, LocalDateTime> NONCE_MAP = new HashMap<>();

	public static boolean addWhenNotExists(String nonce) {
		if (NONCE_MAP.containsKey(nonce) || StringUtils.isBlank(nonce)) {
			return false;
		}

		NONCE_MAP.put(nonce, LocalDateTime.now());

		return true;
	}

	//@Scheduled(fixedDelay = 2000)
	private static void cleanExpiredNonces() {
		var nonceTimeLimit = LocalDateTime.now().minusMinutes(5);

		var expiredKeys = NONCE_MAP.keySet().stream().filter(k -> NONCE_MAP.get(k).isBefore(nonceTimeLimit))
				.collect(Collectors.toList());
		expiredKeys.forEach(exp -> NONCE_MAP.remove(exp));
	}

}
