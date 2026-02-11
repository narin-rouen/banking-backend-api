package com.narinrouen.bankingapi.dto.response.transfer;

import java.math.BigDecimal;
import java.time.Instant;

import com.narinrouen.bankingapi.entity.Transfer;
import com.narinrouen.bankingapi.entity.TransferStatus;

public record TransferResponse(

		long id, long senderAccountId, long recipientAccountId, BigDecimal amount, TransferStatus status,
		Instant createdAt) {
	public static TransferResponse from(Transfer transfer) {
		return new TransferResponse(transfer.getId(), transfer.getSenderAccount().getId(),
				transfer.getRecipientAccount().getId(), transfer.getAmount(), transfer.getStatus(),
				transfer.getCreatedAt());
	}
}
