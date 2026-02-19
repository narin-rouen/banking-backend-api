package com.narinrouen.bankingapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.narinrouen.bankingapi.config.SecurityUser;
import com.narinrouen.bankingapi.dto.common.PageRequest;
import com.narinrouen.bankingapi.dto.request.DepositRequest;
import com.narinrouen.bankingapi.dto.request.WithdrawRequest;
import com.narinrouen.bankingapi.dto.response.PaginatedTransactionResponse;
import com.narinrouen.bankingapi.dto.response.TransactionResponse;
import com.narinrouen.bankingapi.entity.User;
import com.narinrouen.bankingapi.service.AccountService;
import com.narinrouen.bankingapi.service.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TransactionController {

	private final TransactionService transactionService;
	private final AccountService accountService;

	// for admin
	@GetMapping("/api/admin/transactions")
	@PreAuthorize("ADMIN")
	public ResponseEntity<PaginatedTransactionResponse> getAllTransactionsForAdmin(
			@Valid @ModelAttribute PageRequest pageRequest) {
		if (pageRequest == null) {
			pageRequest = new PageRequest(0, 10, null, null, null);
		}
		log.info("Admin fetching all transactions with pagination: page={}, size={}", pageRequest.page(),
				pageRequest.size());

		return ResponseEntity.ok(transactionService.getAllTransaction(pageRequest));
	}

	@GetMapping("/api/admin/transactions/{id}")
	@PreAuthorize("ADMIN")
	public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long id) {
		log.info("Admin fect a transaction with the ID: {}", id);
		return ResponseEntity.ok(transactionService.getTransactionById(id));
	}

	// for user

	@GetMapping("/api/user/transactions")
	@PreAuthorize("USER")
	public ResponseEntity<PaginatedTransactionResponse> getTransactionsByAccountId(@Valid PageRequest pageRequest,
			@AuthenticationPrincipal SecurityUser securityUser) {
		if (pageRequest == null) {
			pageRequest = new PageRequest(0, 10, null, null, null);
		}

		User currentUser = securityUser.getUser();
		Long accountId = accountService.findAccountIdByuserId(currentUser.getId());

		log.info("User ID = {} fetchs their transaction records with pagination: page = {}, size = {}",
				currentUser.getId(), pageRequest.page(), pageRequest.size());

		return ResponseEntity.ok(transactionService.getTransactionByAccountId(accountId, pageRequest));
	}

	@GetMapping("/api/user/transactions/{id}")
	@PreAuthorize("USER")
	public ResponseEntity<TransactionResponse> getTransactionByIdAndAccountId(@PathVariable Long id,
			@AuthenticationPrincipal SecurityUser securityUser) {
		User currentUser = securityUser.getUser();
		Long accountId = accountService.findAccountIdByuserId(currentUser.getId());

		log.info("User Id = {} fetch transaction Id = {}", currentUser.getId(), id);

		return ResponseEntity.ok(transactionService.getTransactionByIdAndAccountId(id, accountId));
	}

	// user operation
	@PostMapping("/api/user/deposit")
	@PreAuthorize("USER")
	public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody DepositRequest request,
			@AuthenticationPrincipal SecurityUser securityUser) {

		log.info("User {} initiating deposit of {} to account {}", securityUser.getUser().getId(), request.amount(),
				request.accountId());

		// Get the user's account ID
		Long userAccountId = accountService.findAccountIdByuserId(securityUser.getUser().getId());

		// Verify the deposit account matches the user's account
		if (!userAccountId.equals(request.accountId())) {
			log.warn("User {} attempted to deposit to account {} but their account is {}",
					securityUser.getUser().getId(), request.accountId(), userAccountId);
			throw new AccessDeniedException("Cannot deposit to this account");
		}

		TransactionResponse response = transactionService.deposit(request);

		log.info("Deposit successful: transactionId={}", response.id());
		return ResponseEntity.ok(response);
	}

	@PostMapping("/api/user/withdraw")
	@PreAuthorize("USER")
	public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody WithdrawRequest request,
			@AuthenticationPrincipal SecurityUser securityUser) {

		log.info("User {} initiating withdrawal of {} from account {}", securityUser.getUser().getId(),
				request.amount(), request.accountId());

		// Get the user's account ID
		Long userAccountId = accountService.findAccountIdByuserId(securityUser.getUser().getId());

		// Verify the withdrawal account matches the user's account
		if (!userAccountId.equals(request.accountId())) {
			log.warn("User {} attempted to withdraw from account {} but their account is {}",
					securityUser.getUser().getId(), request.accountId(), userAccountId);
			throw new AccessDeniedException("Cannot withdraw from this account");
		}

		TransactionResponse response = transactionService.withdraw(request);

		log.info("Withdrawal successful: transactionId={}", response.id());
		return ResponseEntity.ok(response);
	}

}
