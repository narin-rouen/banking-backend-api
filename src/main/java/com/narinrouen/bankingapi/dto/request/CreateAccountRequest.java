package com.narinrouen.bankingapi.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAccountRequest(

		@NotNull(message = "User ID is required") long userId,

		@DecimalMin(value = "0.0", inclusive = false, message = "Initial balance must be greater than zero") BigDecimal balance,

		@NotBlank(message = "Currency is required") @Size(min = 3, max = 3, message = "Currency must be a 3-letter code") String currency) {

}
