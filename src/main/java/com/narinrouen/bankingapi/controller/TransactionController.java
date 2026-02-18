package com.narinrouen.bankingapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.narinrouen.bankingapi.dto.common.PageRequest;
import com.narinrouen.bankingapi.dto.response.PaginatedTransactionResponse;
import com.narinrouen.bankingapi.dto.response.TransactionResponse;
import com.narinrouen.bankingapi.service.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TransactionController {

	private final TransactionService transactionService;

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
}
