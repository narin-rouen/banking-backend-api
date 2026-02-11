package com.narinrouen.bankingapi.dto.common;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import jakarta.validation.constraints.Min;

public record PageRequest(

		@Min(value = 0, message = "Page number must be non-negative") int page,

		@Min(value = 1, message = "Page size must be at least 1") int size,

		String sortBy, Sort.Direction direction, String search) {
	public Pageable toPageable() {
		if (sortBy != null && direction != null) {
			return org.springframework.data.domain.PageRequest.of(page, size, Sort.by(direction, sortBy));
		}
		return org.springframework.data.domain.PageRequest.of(page, size);
	}

	public PageRequest {
		if (page < 0)
			page = 0;
		if (size <= 0)
			size = 10;
	}
}
