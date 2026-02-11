package com.narinrouen.bankingapi.dto.request.user;

import com.narinrouen.bankingapi.entity.Role;
import com.narinrouen.bankingapi.entity.UserStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(

		@NotBlank(message = "First name is required") @Size(max = 50, message = "First name must be at most 50 characters") String firstName,

		@NotBlank(message = "Last name is required") @Size(max = 50, message = "Last name must be at most 50 characters") String lastName,

		@NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,

		@NotBlank(message = "Password is required") @Size(min = 8, message = "Password must be at least 8 characters") String password,

		@NotNull(message = "Role is required") Role role,

		@NotNull(message = "Status is required") UserStatus status) {

}
