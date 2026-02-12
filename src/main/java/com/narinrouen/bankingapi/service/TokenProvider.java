package com.narinrouen.bankingapi.service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenProvider {

	private static final SecureRandom secureRandom = new SecureRandom();
	private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();

	@Value("${app.token.access-expiration:3600}")
	private Long accessTokenExpirationSeconds;

	@Value("${app.token.refresh-expiration:86400}")
	private Long refreshTokenExpirationSeconds;

	public String generateAccessToken() {
		byte[] randomBytes = new byte[32];
		secureRandom.nextBytes(randomBytes);
		return base64Encoder.encodeToString(randomBytes);
	}

	public String generateRefreshToken() {
		return UUID.randomUUID().toString() + "-" + System.currentTimeMillis();
	}

	public Instant getAccessTokenExpiry() {
		return Instant.now().plusSeconds(accessTokenExpirationSeconds);
	}

	public Instant getRefreshTokenExpiry() {
		return Instant.now().plusSeconds(refreshTokenExpirationSeconds);
	}
}
