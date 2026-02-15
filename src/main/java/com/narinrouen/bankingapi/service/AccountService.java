package com.narinrouen.bankingapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.narinrouen.bankingapi.dto.common.PageRequest;
import com.narinrouen.bankingapi.dto.request.CreateAccountRequest;
import com.narinrouen.bankingapi.dto.request.UpdateAccountRequest;
import com.narinrouen.bankingapi.dto.response.AccountResponse;
import com.narinrouen.bankingapi.dto.response.PaginatedAccountResponse;
import com.narinrouen.bankingapi.entity.Account;
import com.narinrouen.bankingapi.entity.AccountStatus;
import com.narinrouen.bankingapi.entity.AccountType;
import com.narinrouen.bankingapi.entity.User;
import com.narinrouen.bankingapi.exception.InvalidAccountTypeException;
import com.narinrouen.bankingapi.repository.AccountRepository;
import com.narinrouen.bankingapi.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AccountService {

	private AccountRepository accountRepository;
	private UserRepository userRepository;

	@Transactional(readOnly = true)
	public PaginatedAccountResponse getAllAccounts(PageRequest pageRequest) {
		log.info("Fetching accounts with pagination: page={}, size={}", pageRequest.page(), pageRequest.size());
		Pageable pagable = pageRequest.toPageable();
		Page<Account> accountPage = accountRepository.findAll(pagable);
		return PaginatedAccountResponse.from(accountPage);
	}

	@Transactional(readOnly = true)
	public PaginatedAccountResponse getAccountsByUserId(long userId, PageRequest pageRequest) {
		log.info("Fetching accounts for userId={} with pagination: page={}, size={}", userId, pageRequest.page(),
				pageRequest.size());
		Pageable pagable = pageRequest.toPageable();
		Page<Account> accountPage = accountRepository.findByUserId(userId, pagable);
		return PaginatedAccountResponse.from(accountPage);
	}

	public AccountResponse createAccount(CreateAccountRequest request) {
		log.info("Creating account for userId={}", request.userId());
		User user = userRepository.findById(request.userId())
				.orElseThrow(() -> new IllegalArgumentException("User not found with id: " + request.userId()));

		Account account = new Account();
		account.setUser(user);
		account.setCurrency("EUR");

		try {
			AccountType accountType = AccountType.valueOf(request.accountType().toUpperCase());
			account.setType(accountType);
		} catch (IllegalArgumentException e) {
			log.error("Invalid account type: {}", request.accountType());
			throw new InvalidAccountTypeException("Invalid account type: " + request.accountType()
					+ ". Allowed values: " + AccountType.getAllowedValues());
		}

		account.setStatus(AccountStatus.ACTIVE);

		Account savedAccount = accountRepository.save(account);
		log.info("Account created with id={}", savedAccount.getId());
		return AccountResponse.from(savedAccount);
	}

	@Transactional(readOnly = true)
	public AccountResponse getAccountById(long accountId) {
		log.info("Fetching account with id={}", accountId);
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new IllegalArgumentException("Account not found with id: " + accountId));
		return AccountResponse.from(account);
	}

	@Transactional(readOnly = true)
	public AccountResponse getAccountByIdAndUserId(long accountId, long userId) {
		log.info("Fetching account with id={} for userId={}", accountId, userId);
		Account account = accountRepository.findByIdAndUserId(accountId, userId)
				.orElseThrow(() -> new IllegalArgumentException(
						"Account not found with id: " + accountId + " for user id: " + userId));
		return AccountResponse.from(account);
	}

	public AccountResponse updateAccount(Long accountId, UpdateAccountRequest request) {
		log.info("Updating account with id={}", accountId);
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new IllegalArgumentException("Account not found with id: " + accountId));

		if (request.status() != null) {
			account.setStatus(request.status());
		}

		if (request.type() != null) {
			account.setType(request.type());
		}

		Account updatedAccount = accountRepository.save(account);
		log.info("Account with id={} updated successfully", accountId);

		return AccountResponse.from(updatedAccount);
	}
}
