package com.narinrouen.bankingapi.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.narinrouen.bankingapi.dto.request.LoginRequest;
import com.narinrouen.bankingapi.dto.request.RegisterRequest;
import com.narinrouen.bankingapi.dto.response.AuthResponse;
import com.narinrouen.bankingapi.dto.response.UserSummaryResponse;
import com.narinrouen.bankingapi.service.AuthService;
import com.narinrouen.bankingapi.service.SessionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final SessionService sessionService;

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request,
			HttpServletRequest httpRequest) {
		AuthResponse response = authService.login(request, httpRequest);
		return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + response.accessToken()).body(response);
	}

	@PostMapping("/register")
	public ResponseEntity<UserSummaryResponse> register(@Valid @RequestBody RegisterRequest request) {
		return ResponseEntity.ok(authService.register(request));
	}

	@PostMapping("/refresh")
	public ResponseEntity<AuthResponse> refreshToken(@RequestHeader("Refresh-Token") String refreshToken,
			HttpServletRequest httpRequest) {
		AuthResponse response = authService.refreshToken(refreshToken, httpRequest);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
		String token = authHeader.substring(7);
		authService.logout(token);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/logout-all")
	public ResponseEntity<Void> logoutAllDevices(@RequestParam Long userId) {
		authService.logoutAllDevices(userId);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/sessions")
	public ResponseEntity<?> getActiveSessions(@RequestParam Long userId) {
		return ResponseEntity.ok(sessionService.getUserActiveSessions(userId));
	}
}
