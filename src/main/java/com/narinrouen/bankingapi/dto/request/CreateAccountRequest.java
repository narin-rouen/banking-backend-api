package com.narinrouen.bankingapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAccountRequest(

		@NotNull(message = "User ID is required") long userId,
		@NotBlank(message = "Account type is required") String accountType) {

}
