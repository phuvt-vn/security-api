package com.example.main.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;

import com.example.main.entity.JweData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;

@Service
public class JweService {

	private static final String CONFIDENTIAL_DATA = "confidentialData001Sample";

	private static final String ENCRYPTION_KEY = "TheMandatory32BytesEncryptionKey";

	public String store(JweData jweData) throws JOSEException {
		var expiresAt = LocalDateTime.now(ZoneOffset.UTC).plusMinutes(15);

		var jweClaims = new JWTClaimsSet.Builder().expirationTime(Date.from(expiresAt.toInstant(ZoneOffset.UTC)))
				.subject(jweData.getUsername()).claim("dummyAttribute", jweData.getDummyAttribute())
				.claim("confidentialData", CONFIDENTIAL_DATA).build();
		var jweHeader = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A256GCM);

		var jwe = new EncryptedJWT(jweHeader, jweClaims);

		var encrypter = new DirectEncrypter(ENCRYPTION_KEY.getBytes());
		jwe.encrypt(encrypter);

		return jwe.serialize();
	}

	public Optional<JweData> read(String jweToken) {
		try {
			var jwe = EncryptedJWT.parse(jweToken);

			var decryptor = new DirectDecrypter(ENCRYPTION_KEY.getBytes());
			jwe.decrypt(decryptor);
			var jweClaims = jwe.getJWTClaimsSet();

			var now = new Date();

			if (now.before(jweClaims.getExpirationTime())
					&& StringUtils.equals(jweClaims.getStringClaim("confidentialData"), CONFIDENTIAL_DATA)) {
				var jweData = new JweData();
				jweData.setUsername(jweClaims.getSubject());
				jweData.setDummyAttribute(jweClaims.getStringClaim("dummyAttribute"));

				return Optional.of(jweData);
			}

			return Optional.empty();
		} catch (Exception ex) {
			return Optional.empty();
		}
	}

}
