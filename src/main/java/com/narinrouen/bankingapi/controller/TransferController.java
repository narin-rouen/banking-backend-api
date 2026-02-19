package com.narinrouen.bankingapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.narinrouen.bankingapi.config.SecurityUser;
import com.narinrouen.bankingapi.dto.request.TransferRequest;
import com.narinrouen.bankingapi.dto.response.TransferResponse;
import com.narinrouen.bankingapi.entity.User;
import com.narinrouen.bankingapi.service.AccountService;
import com.narinrouen.bankingapi.service.TransferService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TransferController {

	private final TransferService transferService;
	private final AccountService accountService;

	@PostMapping("/api/user/transfer")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<TransferResponse> transfer(@RequestBody TransferRequest request,
			@AuthenticationPrincipal SecurityUser securityUser) {
		log.info("User {} initiating transfer of {} from account {} to account {}", securityUser.getUser().getId(),
				request.amount(), request.fromAccountId(), request.toAccountId());

		User currentUser = securityUser.getUser();
		accountService.verifyAccountOwnership(request.fromAccountId(), currentUser.getId());

		TransferResponse response = transferService.transfer(request);

		return ResponseEntity.ok(response);
	}
}
