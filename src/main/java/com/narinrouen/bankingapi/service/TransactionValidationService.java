package com.narinrouen.bankingapi.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.narinrouen.bankingapi.dto.request.DepositRequest;
import com.narinrouen.bankingapi.dto.request.WithdrawRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransactionValidationService {

	public void validateDepositRequest(DepositRequest request) {
		if (request == null) {
			throw new IllegalArgumentException("Deposit request cannot be null");
		}

		if (request.accountId() == null) {
			throw new IllegalArgumentException("Account ID cannot be null");
		}

		if (request.amount() == null) {
			throw new IllegalArgumentException("Amount cannot be null");
		}

		// Additional business validations
		validateDepositLimits(request.amount());
	}

	private void validateDepositLimits(BigDecimal amount) {
		BigDecimal maxDeposit = new BigDecimal("10000");

		if (amount.compareTo(maxDeposit) > 0) {
			throw new IllegalArgumentException("Deposit amount exceeds maximum limit of " + maxDeposit);
		}
	}

	public void validateWithdrawRequest(WithdrawRequest request) {
		if (request == null) {
			throw new IllegalArgumentException("Withdraw request cannot be null");
		}

		if (request.accountId() == null) {
			throw new IllegalArgumentException("Account ID cannot be null");
		}

		if (request.amount() == null) {
			throw new IllegalArgumentException("Amount cannot be null");
		}

		// Additional business validations
		validateWithdrawLimits(request.amount());
	}

	private void validateWithdrawLimits(BigDecimal amount) {
		BigDecimal maxWithdraw = new BigDecimal("10000");

		if (amount.compareTo(maxWithdraw) > 0) {
			throw new IllegalArgumentException("Deposit amount exceeds maximum limit of " + maxWithdraw);
		}
	}
}
