package com.narinrouen.bankingapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.narinrouen.bankingapi.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
