package com.narinrouen.bankingapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.narinrouen.bankingapi.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

	Page<Account> findByUserId(Long userId, Pageable pageable);

	List<Account> findAllByUserId(long userId);

	Optional<Account> findByIdAndUserId(long accountId, long userId);

	Long findAccountIdByUserId(Long id);

}
