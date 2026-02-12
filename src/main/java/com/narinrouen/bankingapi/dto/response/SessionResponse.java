package com.narinrouen.bankingapi.dto.response;

import java.time.Instant;

import com.narinrouen.bankingapi.entity.Session;

public record SessionResponse(

		long id, long userId, boolean isActive, Instant createdAt, Instant expiredAt) {
	public static SessionResponse from(Session session) {
		return new SessionResponse(session.getId(), session.getUser().getId(), session.isActive(),
				session.getCreatedAt(), session.getExpiresAt());
	}
}
