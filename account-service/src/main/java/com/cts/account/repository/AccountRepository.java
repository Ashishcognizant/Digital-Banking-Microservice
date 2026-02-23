package com.cts.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cts.account.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
