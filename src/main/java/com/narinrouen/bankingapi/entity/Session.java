package com.narinrouen.bankingapi.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "sessions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Session {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(nullable = false, length = 500)
	private String token;

	@Column(name = "refresh_token", length = 500)
	private String refreshToken;

	@Column(name = "is_active", nullable = false)
	private boolean isActive;

	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	@Column(name = "expires_at", nullable = false)
	private Instant expiresAt;

	@Column(name = "refresh_expires_at")
	private Instant refreshExpiresAt;

	@Column(name = "ip_address")
	private String ipAddress;

	@Column(name = "user_agent", length = 500)
	private String userAgent;

	@PrePersist
	protected void onCreate() {
		this.createdAt = Instant.now();
		this.isActive = true;
	}
}
