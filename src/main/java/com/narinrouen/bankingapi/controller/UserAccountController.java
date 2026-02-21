package com.narinrouen.bankingapi.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.narinrouen.bankingapi.config.SecurityUser;
import com.narinrouen.bankingapi.dto.request.UpdateUserRequest;
import com.narinrouen.bankingapi.dto.response.UserAccountResponse;
import com.narinrouen.bankingapi.service.UserAccountService;
import com.narinrouen.bankingapi.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserAccountController {

	private final UserAccountService userAccountService;
	private final UserService userService;

	@GetMapping("/api/user/me")
	@PreAuthorize("hasRole('USER')")
	public UserAccountResponse getAllAccountByUserId(@AuthenticationPrincipal SecurityUser currentUser) {
		Long userId = currentUser.getUser().getId();
		return userAccountService.getUserWithAccountList(userId);
	}

	@PostMapping("/api/user/me")
	@PreAuthorize("hasRole('USER')")
	public UserAccountResponse updateAccountInfo(@RequestBody UpdateUserRequest request,
			@AuthenticationPrincipal SecurityUser currentUser) {
		Long userId = currentUser.getUser().getId();
		return userService.updateUserInfo(userId, request);
	}
}
