package com.narinrouen.bankingapi.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record TransferRequest(

		@NotNull(message = "From account ID is required") long fromAccountId,

		@NotNull(message = "To account ID is required") long toAccountId,

		@NotNull(message = "Amount is required") @DecimalMin(value = "0.01", message = "Amount must be greater than zero") BigDecimal amount) {

}
