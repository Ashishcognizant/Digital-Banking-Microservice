package com.cts.account.service;

import com.cts.account.exception.AccountNotFoundException;
import com.cts.account.model.Account;
import com.cts.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account getAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account updateAccountStatus(Long id, String status) {
        Account account = getAccount(id);
        account.setStatus(status);
        return accountRepository.save(account);
    }

    public Double getBalance(Long id) {
        Account account = getAccount(id);
        return account.getBalance();
    }

    public void updateBalance(Long id, Double newBalance) {
        Account account = getAccount(id);
        account.setBalance(newBalance);
        accountRepository.save(account);
    }
}
