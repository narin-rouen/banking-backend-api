package com.narinrouen.bankingapi.dto.response.transaction;

import java.math.BigDecimal;
import java.time.Instant;

import com.narinrouen.bankingapi.entity.Transaction;
import com.narinrouen.bankingapi.entity.TransactionStatus;
import com.narinrouen.bankingapi.entity.TransactionType;

public record TransactionResponse(

		long id, long accountId, TransactionType type, BigDecimal amount, BigDecimal balanceBefore,
		BigDecimal balanceAfter, TransactionStatus status, String reference, Instant createdAt) {
	public static TransactionResponse from(Transaction transaction) {
		return new TransactionResponse(transaction.getId(), transaction.getAccount().getId(), transaction.getType(),
				transaction.getAmount(), transaction.getBalanceBefore(), transaction.getBalanceAfter(),
				transaction.getStatus(), transaction.getReference(), transaction.getCreatedAt());
	}
}
