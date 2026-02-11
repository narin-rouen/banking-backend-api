package com.narinrouen.bankingapi.dto.request.user;

import com.narinrouen.bankingapi.entity.Role;
import com.narinrouen.bankingapi.entity.UserStatus;

import jakarta.validation.constraints.Size;

public record UpdateUserRequest(

		@Size(max = 50, message = "First name must be at most 50 characters") String firstName,

		@Size(max = 50, message = "Last name must be at most 50 characters") String lastName,

		@Size(min = 8, max = 100, message = "Email must be at most 100 characters") String password,

		Role role, UserStatus status) {

}
