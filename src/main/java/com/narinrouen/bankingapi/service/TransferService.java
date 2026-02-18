package com.narinrouen.bankingapi.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.narinrouen.bankingapi.dto.request.TransferRequest;
import com.narinrouen.bankingapi.dto.response.TransferResponse;
import com.narinrouen.bankingapi.entity.Account;
import com.narinrouen.bankingapi.entity.Transaction;
import com.narinrouen.bankingapi.entity.TransactionStatus;
import com.narinrouen.bankingapi.entity.TransactionType;
import com.narinrouen.bankingapi.entity.Transfer;
import com.narinrouen.bankingapi.entity.TransferStatus;
import com.narinrouen.bankingapi.exception.InsufficientFundsException;
import com.narinrouen.bankingapi.exception.SameAccountTransferException;
import com.narinrouen.bankingapi.repository.TransferRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransferService {

	private final TransferRepository transferRepository;
	private final AccountService accountService;
	private final TransactionService transactionService;

	// Transfer logic
	@Transactional
	public TransferResponse transfer(TransferRequest request) {
		log.info("Starting transfer from account {} to account {} with amount {}", request.fromAccountId(),
				request.toAccountId(), request.amount());

		validateTransferRequest(request);

		// check if both sender and receiver exist
		Account senderAccount = accountService.findAndValidateAccount(request.fromAccountId());
		Account recipientAccount = accountService.findAndValidateAccount(request.toAccountId());

		// check if the sender has enough money
		BigDecimal balanceBefore = senderAccount.getBalance();
		validateSufficientFunds(balanceBefore, request.amount());

		BigDecimal senderBalanceAfter = balanceBefore.subtract(request.amount());
		BigDecimal recipientBalanceAfter = recipientAccount.getBalance().add(request.amount());

		// record transfer
		Transfer tranfer = buildTransfer(senderAccount, recipientAccount, request);
		Transfer savedTransfer = transferRepository.save(tranfer);

		try {
			// update balance for both account
			accountService.updateAccountBalance(senderAccount, senderBalanceAfter);
			accountService.updateAccountBalance(recipientAccount, recipientBalanceAfter);

			// record transaction data
			createTransactionRecords(senderAccount, recipientAccount, request, balanceBefore, senderBalanceAfter,
					recipientBalanceAfter);

			savedTransfer.setStatus(TransferStatus.SUCCESS);
			transferRepository.save(savedTransfer);

			log.info("Transfer completed successfully. Transfer ID: {}", savedTransfer.getId());
		} catch (Exception e) {
			log.error("Transfer failed: {}", e.getMessage());
			savedTransfer.setStatus(TransferStatus.FAILED);
			transferRepository.save(savedTransfer);
			throw e;
		}

		return TransferResponse.from(savedTransfer);
	}

	private void validateTransferRequest(TransferRequest request) {
		if (request.fromAccountId() == request.toAccountId()) {
			throw new SameAccountTransferException("Cannot transfer to the same account");
		}

		if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Transfer amount must be greater than zero");
		}
	}

	private void validateSufficientFunds(BigDecimal balance, BigDecimal amount) {
		if (balance.compareTo(amount) < 0) {
			throw new InsufficientFundsException(
					String.format("Insufficient funds. Available: %s, Requested: %s", balance, amount));
		}
	}

	private void createTransactionRecords(Account sender, Account recipient, TransferRequest request,
			BigDecimal senderBalanceBefore, BigDecimal senderBalanceAfter, BigDecimal recipientBalanceAfter) {
		// Create debit transaction for sender (money going out)
		Transaction senderTransaction = Transaction.builder().account(sender).type(TransactionType.TRANSFER_OUT)
				.amount(request.amount()).balanceBefore(senderBalanceBefore).balanceAfter(senderBalanceAfter)
				.status(TransactionStatus.SUCCESS).reference(String.format("Transfer to account %s", recipient.getId()))
				.build();

		// Create credit transaction for recipient (money coming in)
		Transaction recipientTransaction = Transaction.builder().account(recipient).type(TransactionType.TRANSFER_IN)
				.amount(request.amount()).balanceBefore(recipient.getBalance()).balanceAfter(recipientBalanceAfter)
				.status(TransactionStatus.SUCCESS).reference(String.format("Transfer from account %s", sender.getId()))
				.build();

		transactionService.saveTransaction(senderTransaction);
		transactionService.saveTransaction(recipientTransaction);
	}

	private Transfer buildTransfer(Account senderAccount, Account recipientAccount, TransferRequest request) {
		return Transfer.builder().senderAccount(senderAccount).recipientAccount(recipientAccount)
				.amount(request.amount()).status(TransferStatus.PENDING) // Start with PENDING status
				.build();
	}

}
