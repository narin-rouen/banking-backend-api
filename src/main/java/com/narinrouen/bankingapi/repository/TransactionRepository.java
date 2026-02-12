package com.narinrouen.bankingapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.narinrouen.bankingapi.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
