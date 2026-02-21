package com.narinrouen.bankingapi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.narinrouen.bankingapi.dto.request.UpdateUserRequest;
import com.narinrouen.bankingapi.dto.response.UserAccountResponse;
import com.narinrouen.bankingapi.dto.response.UserSummaryResponse;
import com.narinrouen.bankingapi.entity.User;
import com.narinrouen.bankingapi.exception.ResourceNotFoundException;
import com.narinrouen.bankingapi.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AccountService accountService;

	// update user info
	public UserAccountResponse updateUserInfo(Long userId, UpdateUserRequest request) {
		User existingUser = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Cannot find user with Id: " + userId));

		// Update only the fields that are provided in the request
		if (request.firstName() != null) {
			existingUser.setFirstName(request.firstName());
		}

		if (request.lastName() != null) {
			existingUser.setLastName(request.lastName());
		}

		if (request.password() != null && !request.password().isEmpty()) {
			existingUser.setPassword(passwordEncoder.encode(request.password()));
		}

		User updatedUser = userRepository.save(existingUser);

		// Get user's account information
		var accountDto = accountService.getAllAccountsByUserId(userId);

		return new UserAccountResponse(UserSummaryResponse.from(updatedUser), accountDto);

	}

	// Add this method to your UserService class
	@Transactional(readOnly = true)
	public User getUserById(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Cannot find user with Id: " + userId));
	}
}
