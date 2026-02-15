package com.narinrouen.bankingapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.narinrouen.bankingapi.dto.common.PageRequest;
import com.narinrouen.bankingapi.dto.request.CreateAccountRequest;
import com.narinrouen.bankingapi.dto.request.UpdateAccountRequest;
import com.narinrouen.bankingapi.dto.response.AccountResponse;
import com.narinrouen.bankingapi.dto.response.PaginatedAccountResponse;
import com.narinrouen.bankingapi.entity.User;
import com.narinrouen.bankingapi.service.AccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountController {

	private AccountService accountService;

	// for admin
	@GetMapping("/api/admin/accounts")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<PaginatedAccountResponse> getAllAccounts(@Valid PageRequest pageRequest) {
		log.info("Admin fetching all accounts with pagination: page={}, size={}", pageRequest.page(),
				pageRequest.size());
		return ResponseEntity.ok(accountService.getAllAccounts(pageRequest));
	}

	@PostMapping("/api/admin/accounts")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<AccountResponse> createAccount(@Valid CreateAccountRequest request) {
		log.info("Admin creating account for userId={}", request.userId());
		return ResponseEntity.ok(accountService.createAccount(request));
	}

	@GetMapping("/api/admin/accounts/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<AccountResponse> getAccountById(@PathVariable long id) {
		log.info("Admin fetching account with id={}", id);
		return ResponseEntity.ok(accountService.getAccountById(id));
	}

	@PostMapping("/api/admin/accounts/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<AccountResponse> updateAccount(@PathVariable long id, @Valid UpdateAccountRequest request) {
		log.info("Admin updating account with id={}", id);
		return ResponseEntity.ok(accountService.updateAccount(id, request));
	}

	// for user
	@GetMapping("/api/user/accounts")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<PaginatedAccountResponse> getUserAccounts(@Valid PageRequest pageRequest,
			@AuthenticationPrincipal User currentUser) {
		log.info("User Id {} fetching their accounts with pagination: page={}, size={}", currentUser.getId(),
				pageRequest.page(), pageRequest.size());
		return ResponseEntity.ok(accountService.getAccountsByUserId(currentUser.getId(), pageRequest));
	}
}
