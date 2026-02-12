package com.narinrouen.bankingapi.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.narinrouen.bankingapi.service.CustomUserDetailsService;
import com.narinrouen.bankingapi.service.SessionService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.var;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class SessionAuthenticationFilter extends OncePerRequestFilter {

	private final SessionService sessionService;
	private final CustomUserDetailsService customUserDetailsService;

	@Override
	protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
			@NotNull FilterChain filterChain) throws ServletException, IOException {

		final String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Session ")) {
			filterChain.doFilter(request, response);
			return;
		}

		final String token = authHeader.substring(8);

		try {
			var sessionOptinal = sessionService.validateSession(token);
			if (sessionOptinal.isPresent()) {
				var session = sessionOptinal.get();
				var user = session.getUser();

				UserDetails userDetails = new SecurityUser(user);

				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());

				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);

				// Log authentication success
				log.debug("Authenticated user: {} with token: {}", user.getEmail(), token.substring(0, 10) + "...");
			}
		} catch (Exception e) {
			log.error("Session validation failed: {}", e.getMessage());

		}
		filterChain.doFilter(request, response);
	}

}
