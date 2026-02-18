package com.narinrouen.bankingapi.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.narinrouen.bankingapi.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	Page<Transaction> findByAccountId(Long accountId, Pageable pagable);

	Optional<Transaction> findByIdAndAccountId(Long id, Long accountId);

}
