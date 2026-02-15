package com.narinrouen.bankingapi.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

import com.narinrouen.bankingapi.entity.Account;
import com.narinrouen.bankingapi.entity.AccountStatus;
import com.narinrouen.bankingapi.entity.AccountType;

public record AccountResponse(

		long id, UserSummaryResponse user, BigDecimal balance, String currency, AccountStatus status, AccountType type,
		Instant createdAt, Instant updatedAt) {
	public static AccountResponse from(Account account) {
		return new AccountResponse(account.getId(), UserSummaryResponse.from(account.getUser()), account.getBalance(),
				account.getCurrency(), account.getStatus(), account.getType(), account.getCreatedAt(),
				account.getUpdatedAt());
	}
}
