package com.narinrouen.bankingapi.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

import com.narinrouen.bankingapi.entity.Account;
import com.narinrouen.bankingapi.entity.AccountStatus;
import com.narinrouen.bankingapi.entity.AccountType;

public record AccountDto(long id, BigDecimal balance, String currency, AccountStatus status, AccountType type,
		Instant createdAt, Instant updatedAt) {

	public static AccountDto from(Account account) {
		return new AccountDto(account.getId(), account.getBalance(), account.getCurrency(), account.getStatus(),
				account.getType(), account.getCreatedAt(), account.getUpdatedAt());
	}
}
