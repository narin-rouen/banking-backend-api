package com.narinrouen.bankingapi.dto.response.user;

import com.narinrouen.bankingapi.entity.Role;
import com.narinrouen.bankingapi.entity.User;

public record UserSummaryResponse(

		long id, String firstName, String lastName, String email, Role role) {

	public static UserSummaryResponse from(User user) {
		return new UserSummaryResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),
				user.getRole());
	}
}
