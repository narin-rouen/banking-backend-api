package com.narinrouen.bankingapi.dto.request.transaction;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record DepositRequest(

		@NotNull(message = "Account ID is required") long accountId,

		@NotNull(message = "Amount is required") @DecimalMin(value = "0.01", message = "Amount must be greater than zero") BigDecimal amount,

		String reference) {

}
