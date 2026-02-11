package com.narinrouen.bankingapi.dto.response.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.narinrouen.bankingapi.dto.response.user.UserSummaryResponse;

public record AuthResponse(

		@JsonProperty("access_token") String accessToken,

		@JsonProperty("token_type") String tokenType,

		@JsonProperty("expires_in") Long expiresIn,

		UserSummaryResponse user) {
	public AuthResponse(String accessToken, UserSummaryResponse user) {
		this(accessToken, "Bearer", 3600L, user);
	}
}
