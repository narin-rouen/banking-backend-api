package com.narinrouen.bankingapi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.narinrouen.bankingapi.dto.request.LoginRequest;
import com.narinrouen.bankingapi.dto.request.RegisterRequest;
import com.narinrouen.bankingapi.dto.response.AuthResponse;
import com.narinrouen.bankingapi.dto.response.UserSummaryResponse;
import com.narinrouen.bankingapi.entity.Role;
import com.narinrouen.bankingapi.entity.User;
import com.narinrouen.bankingapi.entity.UserStatus;
import com.narinrouen.bankingapi.repository.SessionRepository;
import com.narinrouen.bankingapi.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.var;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

	private final UserRepository userRepository;
	private final SessionService sessionService;
	private final PasswordEncoder passwordEncoder;
	private final SessionRepository sessionRepository;

	@Transactional
	public AuthResponse login(LoginRequest request, HttpServletRequest httpRequest) {
		User user = userRepository.findByEmail(request.email())
				.orElseThrow(() -> new RuntimeException("Invalid email or password"));

		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new RuntimeException("Invalid email or password");
		}

		var session = sessionService.createSession(user, httpRequest);
		log.info("User {} logged in successfully", user.getEmail());
		return new AuthResponse(session.getToken(), UserSummaryResponse.from(user));
	}

	@Transactional
	public AuthResponse refreshToken(String refreshToken, HttpServletRequest httpRequest) {
		var sessionOptinal = sessionService.refreshSession(refreshToken, httpRequest);
		if (sessionOptinal.isEmpty()) {
			throw new RuntimeException("Invalid refresh token");
		}
		var session = sessionOptinal.get();
		log.info("Refreshed token for user: {}", session.getUser().getEmail());
		return new AuthResponse(session.getToken(), UserSummaryResponse.from(session.getUser()));
	}

	@Transactional
	public void logout(String token) {
		var sessionOptinal = sessionService.validateSession(token);
		if (sessionOptinal.isPresent()) {
			var session = sessionOptinal.get();
			session.setActive(false);
			sessionRepository.save(session);
			log.info("User {} logged out successfully", session.getUser().getEmail());
		} else {
			log.warn("Attempted to logout with invalid token: {}", token.substring(0, 10) + "...");
		}
	}

	@Transactional
	public void logoutAllDevices(Long userId) {
		sessionService.invalidateAllUsersSessions(userId);
		log.info("User with ID {} logged out from all devices", userId);
	}

	@Transactional
	public UserSummaryResponse register(RegisterRequest request) {
		if (userRepository.existsByEmail(request.email())) {
			throw new RuntimeException("Email already in use");
		}

		// check if role is provided and valid, otherwise default to USER
		Role role = Role.USER; // default
		if (request.role() != null && !request.role().isEmpty()) {
			try {
				role = Role.valueOf(request.role().toUpperCase());
			} catch (IllegalArgumentException e) {
				log.warn("Invalid role provided: {}, using default USER", request.role());
			}
		}

		User user = User.builder().email(request.email()).password(passwordEncoder.encode(request.password()))
				.firstName(request.firstName()).lastName(request.lastName()).role(role).status(UserStatus.ACTIVE)
				.build();

		User savedUser = userRepository.save(user);

		log.info("Registered new user: {} {}", savedUser.getFirstName(), savedUser.getLastName());

		return UserSummaryResponse.from(savedUser);
	}

}
