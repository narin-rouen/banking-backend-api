package com.narinrouen.bankingapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.narinrouen.bankingapi.entity.Transfer;

public interface TransferRepository extends JpaRepository<Transfer, Long> {

}
