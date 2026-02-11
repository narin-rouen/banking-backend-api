package com.narinrouen.bankingapi.dto.response.user;

import java.time.Instant;

import com.narinrouen.bankingapi.entity.Role;
import com.narinrouen.bankingapi.entity.User;
import com.narinrouen.bankingapi.entity.UserStatus;

public record UserResponse(long id, String firstName, String lastName, String email, Role role, UserStatus status,
		Instant createdAt, Instant updatedAt) {
	public static UserResponse from(User user) {
		return new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole(),
				user.getStatus(), user.getCreatedAt(), user.getUpdatedAt());
	}
}
