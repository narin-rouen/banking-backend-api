package com.narinrouen.bankingapi.entity;

public enum AccountType {
	SAVINGS, CURRENT, BUSINESS;

	public static String getAllowedValues() {
		return String.join(", ", java.util.Arrays.stream(AccountType.values()).map(Enum::name).toArray(String[]::new));
	}
}
