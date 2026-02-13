package com.narinrouen.bankingapi.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.narinrouen.bankingapi.dto.response.SessionResponse;
import com.narinrouen.bankingapi.entity.Session;
import com.narinrouen.bankingapi.entity.User;
import com.narinrouen.bankingapi.repository.SessionRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SessionService {

	private final SessionRepository sessionRepository;
	private final TokenProvider tokenProvider;

	public Session createSession(User user, HttpServletRequest request) {

		// Deactivate existing sessions
		int deactivedCount = sessionRepository.deactivateAllUserSessions(user.getId());
		log.debug("Deactivated {} existing sessions for user: {}", deactivedCount, user.getEmail());

		Session session = Session.builder().user(user).token(tokenProvider.generateAccessToken())
				.refreshToken(tokenProvider.generateRefreshToken()).isActive(true).ipAddress(getClientIp(request))
				.userAgent(getUserAgent(request)).expiresAt(tokenProvider.getAccessTokenExpiry())
				.refreshExpiresAt(tokenProvider.getRefreshTokenExpiry()).build();

		Session savedSession = sessionRepository.save(session);
		log.info("Created new session for user: {} with token: {}", user.getEmail(),
				savedSession.getToken().substring(0, 10) + "...");

		return savedSession;
	}

	@Transactional(readOnly = true)
	public Optional<Session> validateSession(String token) {
		return sessionRepository.findValidSessionByToken(token, Instant.now());
	}

	@Transactional(readOnly = true)
	public Optional<Session> validateRefreshToken(String refreshToken) {
		return sessionRepository.findValidSessionByRefreshToken(refreshToken, Instant.now());
	}

	@Transactional
	public Optional<Session> refreshSession(String refreshToken, HttpServletRequest request) {
		return validateRefreshToken(refreshToken).map(session -> {
			// Generate new tokens
			session.setToken(tokenProvider.generateAccessToken());
			session.setRefreshToken(tokenProvider.generateRefreshToken());
			session.setExpiresAt(tokenProvider.getAccessTokenExpiry());
			session.setRefreshExpiresAt(tokenProvider.getRefreshTokenExpiry());
			session.setIpAddress(getClientIp(request));
			session.setUserAgent(getUserAgent(request));

			Session refreshedSession = sessionRepository.save(session);
			log.info("Refreshed session for user: {}", session.getUser().getEmail());

			return refreshedSession;
		});
	}

	@Transactional
	public void invalidateSession(String token) {
		sessionRepository.findByToken(token).ifPresent(session -> {
			session.setActive(false);
			sessionRepository.save(session);
			log.info("Invalidated session for user: {}", session.getUser().getEmail());
		});
	}

	@Transactional
	public void invalidateAllUsersSessions(Long userId) {
		int deactivedCount = sessionRepository.deactivateAllUserSessions(userId);
		log.info("Invalidated {} sessions for user ID: {}", deactivedCount, userId);
	}

	@Transactional // Was @Tansactional
	@Scheduled(cron = "0 0 * * * ?") // Was @Sheduling
	public void cleanupExpiredSessions() {
		int deletedCount = sessionRepository.deleteExpiredSessions(Instant.now());
		log.info("Cleaned up {} expired sessions", deletedCount);
	}

	@Transactional(readOnly = true)
	public List<SessionResponse> getUserActiveSessions(Long userId) {
		return sessionRepository.findByUserIdAndIsActiveTrue(userId).stream().map(SessionResponse::from)
				.collect(Collectors.toList());
	}

	private String getClientIp(HttpServletRequest request) {
		String xfHeader = request.getHeader("X-Forwarded-For");
		if (xfHeader == null) {
			return request.getRemoteAddr();
		}
		return xfHeader.split(",")[0];
	}

	private String getUserAgent(HttpServletRequest request) {
		return request.getHeader("User-Agent");
	}
}
