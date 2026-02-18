package com.narinrouen.bankingapi.service;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.narinrouen.bankingapi.dto.common.PageRequest;
import com.narinrouen.bankingapi.dto.request.DepositRequest;
import com.narinrouen.bankingapi.dto.request.WithdrawRequest;
import com.narinrouen.bankingapi.dto.response.PaginatedTransactionResponse;
import com.narinrouen.bankingapi.dto.response.TransactionResponse;
import com.narinrouen.bankingapi.entity.Account;
import com.narinrouen.bankingapi.entity.Transaction;
import com.narinrouen.bankingapi.entity.TransactionStatus;
import com.narinrouen.bankingapi.entity.TransactionType;
import com.narinrouen.bankingapi.exception.InsufficientFundsException;
import com.narinrouen.bankingapi.exception.ResourceNotFoundException;
import com.narinrouen.bankingapi.exception.TransactionFailedException;
import com.narinrouen.bankingapi.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

	private final TransactionRepository transactionRepository;
	private final AccountService accountService;
	private final TransactionValidationService transactionValidationService;

	// deposit logic
	@Transactional
	public TransactionResponse deposit(DepositRequest request) {
		validateDepositRequest(request);

		log.info("Processing deposit for account {}: amount={}, reference={}", request.accountId(), request.amount(),
				request.reference());

		try {
			Account account = accountService.findAndValidateAccount(request.accountId());

			BigDecimal balanceBefore = account.getBalance();
			BigDecimal balanceAfter = balanceBefore.add(request.amount());

			Transaction transaction = buildDepositTransaction(account, request, balanceBefore, balanceAfter);
			Transaction savedTransaction = transactionRepository.save(transaction);

			accountService.updateAccountBalance(account, balanceAfter);

			log.info("Deposit successful for account {}: transactionId={}, newBalance={}", account.getId(),
					savedTransaction.getId(), balanceAfter);

			return TransactionResponse.from(savedTransaction);

		} catch (Exception e) {
			log.error("Failed to process deposit for account {}: {}", request.accountId(), e.getMessage(), e);
			throw new TransactionFailedException("Failed to process deposit", e);
		}

	}

	// withdraw logic
	@Transactional
	public TransactionResponse withdraw(WithdrawRequest request) {
		validateWithdrawRequest(request);

		log.info("Processing withdraw for account {}: amount={}, reference={}", request.accountId(), request.amount(),
				request.reference());

		try {
			Account account = accountService.findAndValidateAccount(request.accountId());

			BigDecimal balanceBefore = account.getBalance();

			validateSufficientFunds(balanceBefore, request.amount());

			BigDecimal balanceAfter = balanceBefore.subtract(request.amount());

			Transaction transaction = buildWithdrawTransaction(account, request, balanceBefore, balanceAfter);
			Transaction savedTransaction = transactionRepository.save(transaction);

			accountService.updateAccountBalance(account, balanceAfter);

			log.info("Withdraw successful for account {}: transactionId={}, newBalance={}", account.getId(),
					savedTransaction.getId(), balanceAfter);

			return TransactionResponse.from(savedTransaction);

		} catch (Exception e) {
			log.error("Failed to process withdraw for account {}: {}", request.accountId(), e.getMessage(), e);
			throw new TransactionFailedException("Failed to process withdraw", e);
		}

	}

	private void validateDepositRequest(DepositRequest request) {
		transactionValidationService.validateDepositRequest(request);

		if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Amount must be greater than zero");
		}

		if (request.accountId() <= 0) {
			throw new IllegalArgumentException("Invalid account ID" + request.accountId());
		}
	}

	private Transaction buildDepositTransaction(Account account, DepositRequest request, BigDecimal balanceBefore,
			BigDecimal balanceAfter) {
		return Transaction.builder().account(account).type(TransactionType.DEPOSIT).amount(request.amount())
				.balanceBefore(balanceBefore).balanceAfter(balanceAfter).status(TransactionStatus.SUCCESS)
				.reference(request.reference()).build();
	}

	private void validateWithdrawRequest(WithdrawRequest request) {
		transactionValidationService.validateWithdrawRequest(request);

		if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Amount must be greater than zero");
		}

		if (request.accountId() <= 0) {
			throw new IllegalArgumentException("Invalid account ID" + request.accountId());
		}
	}

	public void validateSufficientFunds(BigDecimal balanceBefore, BigDecimal amount) {
		if (balanceBefore.compareTo(amount) < 0) {
			throw new InsufficientFundsException("Insufficient funds: available balance is " + balanceBefore);
		}
	}

	private Transaction buildWithdrawTransaction(Account account, WithdrawRequest request, BigDecimal balanceBefore,
			BigDecimal balanceAfter) {
		return Transaction.builder().account(account).type(TransactionType.WITHDRAW).amount(request.amount())
				.balanceBefore(balanceBefore).balanceAfter(balanceAfter).status(TransactionStatus.SUCCESS)
				.reference(request.reference()).build();
	}

	public Transaction saveTransaction(Transaction transaction) {
		log.debug("Saving transaction for account {}: type={}, amount={}", transaction.getAccount().getId(),
				transaction.getType(), transaction.getAmount());
		return transactionRepository.save(transaction);
	}

	@Transactional(readOnly = true)
	public PaginatedTransactionResponse getAllTransaction(PageRequest pageRequest) {
		log.info("Fetching accounts with pagination: page={}, size={}", pageRequest.page(), pageRequest.size());
		Pageable pagable = pageRequest.toPageable();
		Page<Transaction> transactionPage = transactionRepository.findAll(pagable);

		return PaginatedTransactionResponse.from(transactionPage);
	}

	@Transactional(readOnly = true)
	public PaginatedTransactionResponse getTransactionByAccountId(Long accountId, PageRequest pageRequest) {
		log.info("Fetching Transactions for accountId={} with pagination: page={}, size={}", accountId,
				pageRequest.page(), pageRequest.size());

		accountService.findAndValidateAccount(accountId);

		Pageable pagable = pageRequest.toPageable();
		Page<Transaction> accountPage = transactionRepository.findByAccountId(accountId, pagable);
		return PaginatedTransactionResponse.from(accountPage);
	}

	@Transactional(readOnly = true)
	public TransactionResponse getTransactionById(Long id) {
		log.info("Fetching transaction with id={}", id);
		Transaction transaction = transactionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
		return TransactionResponse.from(transaction);
	}

	@Transactional(readOnly = true)
	public TransactionResponse getTransactionByIdAndAccountId(Long id, Long accountId) {
		log.info("Fetching transaction with id={} for userId={}", id, accountId);
		Transaction transaction = transactionRepository.findByIdAndAccountId(id, accountId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Transaction not found with id: " + id + " for user id: " + accountId));
		return TransactionResponse.from(transaction);
	}

}
