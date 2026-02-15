package com.narinrouen.bankingapi.dto.response;

import java.util.List;

import org.springframework.data.domain.Page;

import com.narinrouen.bankingapi.entity.Account;

public record PaginatedAccountResponse(List<AccountResponse> accounts, int currentPage, int totalPages, long totalItems,
		int pageSize, boolean hasNext, boolean hasPrevious) {
	public static PaginatedAccountResponse from(Page<Account> accountPage) {
		List<AccountResponse> accountResponses = accountPage.getContent().stream().map(AccountResponse::from).toList();

		return new PaginatedAccountResponse(accountResponses, accountPage.getNumber(), accountPage.getTotalPages(),
				accountPage.getTotalElements(), accountPage.getSize(), accountPage.hasNext(),
				accountPage.hasPrevious());
	}
}
