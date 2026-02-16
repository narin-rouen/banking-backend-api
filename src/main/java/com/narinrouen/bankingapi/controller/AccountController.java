package com.narinrouen.bankingapi.controller;

import org.springframework.http.ResponseEntity;
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

	private final AccountService accountService;

	// for admin
	@GetMapping("/api/admin/accounts")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<PaginatedAccountResponse> getAllAccounts(@Valid @ModelAttribute PageRequest pageRequest) {
		if (pageRequest == null) {
			pageRequest = new PageRequest(0, 10, null, null, null);
		}
		log.info("Admin fetching all accounts with pagination: page={}, size={}", pageRequest.page(),
				pageRequest.size());

		return ResponseEntity.ok(accountService.getAllAccounts(pageRequest));
	}

	@PostMapping("/api/admin/accounts")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request) {
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
	public ResponseEntity<AccountResponse> updateAccount(@PathVariable long id,
			@Valid @RequestBody UpdateAccountRequest request) {
		log.info("Admin updating account with id={}", id);
		return ResponseEntity.ok(accountService.updateAccount(id, request));
	}

	// for user
	@GetMapping("/api/user/accounts")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<PaginatedAccountResponse> getUserAccounts(@Valid PageRequest pageRequest,
			@AuthenticationPrincipal SecurityUser securityUser) {

		if (pageRequest == null) {
			pageRequest = new PageRequest(0, 10, null, null, null);
		}

		User currentUser = securityUser.getUser();
		log.info("User Id {} fetching their accounts with pagination: page={}, size={}", currentUser.getId(),
				pageRequest.page(), pageRequest.size());

		return ResponseEntity.ok(accountService.getAccountsByUserId(currentUser.getId(), pageRequest));
	}
}
