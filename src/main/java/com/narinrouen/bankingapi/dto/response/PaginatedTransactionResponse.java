package com.narinrouen.bankingapi.dto.response;

import java.util.List;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.narinrouen.bankingapi.entity.Transaction;

public record PaginatedTransactionResponse(List<TransactionResponse> content, int page, int size, long totalElements,
		int totalPages, @JsonProperty("isLast") boolean isLast, @JsonProperty("isFirst") boolean isFirst) {
	public static PaginatedTransactionResponse from(Page<Transaction> transactionPage) {
		List<TransactionResponse> content = transactionPage.getContent().stream().map(TransactionResponse::from)
				.toList();

		return new PaginatedTransactionResponse(content, transactionPage.getNumber(), transactionPage.getSize(),
				transactionPage.getTotalElements(), transactionPage.getTotalPages(), transactionPage.isLast(),
				transactionPage.isFirst());
	}
}
