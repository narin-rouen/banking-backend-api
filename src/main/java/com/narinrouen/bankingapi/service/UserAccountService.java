package com.narinrouen.bankingapi.service;

import org.springframework.stereotype.Service;

import com.narinrouen.bankingapi.dto.response.UserAccountResponse;
import com.narinrouen.bankingapi.dto.response.UserSummaryResponse;
import com.narinrouen.bankingapi.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAccountService {

	private final AccountService accountService;
	private final UserService userService;

	// get user + account list
	public UserAccountResponse getUserWithAccountList(Long userId) {
		User user = userService.getUserById(userId); // You'll need to add this method
		var accountDto = accountService.getAllAccountsByUserId(userId);

		return new UserAccountResponse(UserSummaryResponse.from(user), accountDto);
	}

}
