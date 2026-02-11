package com.narinrouen.bankingapi.entity;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "first_name", nullable = false, length = 100)
	private String firstName;

	@Column(name = "last_name", nullable = false, length = 100)
	private String lastName;

	@Column(nullable = false, unique = true, length = 100)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, length = 20)
	private String status;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private String role;

	@OneToMany(mappedBy = "user")
	private List<Account> accounts;

	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createAt;

	@Column(name = "updated_at", nullable = false)
	private Instant updateAt;

	@PrePersist
	protected void onCreate() {
		createAt = Instant.now();
		updateAt = Instant.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updateAt = Instant.now();
	}
}
