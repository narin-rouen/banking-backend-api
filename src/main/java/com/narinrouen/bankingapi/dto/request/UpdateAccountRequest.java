package com.narinrouen.bankingapi.dto.request;

import com.narinrouen.bankingapi.entity.AccountStatus;
import com.narinrouen.bankingapi.entity.AccountType;

public record UpdateAccountRequest(AccountStatus status, AccountType type) {

	public UpdateAccountRequest {
		if (status == null && type == null) {
			throw new IllegalArgumentException("At least one of status or type must be provided");
		}
	}
}
