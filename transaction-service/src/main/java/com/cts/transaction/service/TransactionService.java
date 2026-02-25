package com.cts.transaction.service;

import com.cts.transaction.client.AccountClient;
import com.cts.transaction.client.NotificationClient;
import com.cts.transaction.dto.AccountDTO;
import com.cts.transaction.dto.SendNotificationRequest;
import com.cts.transaction.dto.TransactionRequest;
import com.cts.transaction.dto.TransactionResponse;
import com.cts.transaction.exception.InsufficientFundsException;
import com.cts.transaction.model.Transaction;
import com.cts.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountClient accountClient;
    private final NotificationClient notificationClient;

    @Transactional
    public TransactionResponse deposit(TransactionRequest request) {
        AccountDTO account = accountClient.getAccount(request.getAccountId());

        // ✅ Only Active accounts can accept deposits
        ensureActive(account, "Deposit not allowed: account is not Active");

        double amt = safeAmount(request.getAmount());
        double current = safeBalance(account.getBalance());
        double newBalance = current + amt;

        accountClient.updateBalance(request.getAccountId(), newBalance);

        Transaction transaction = Transaction.builder()
                .accountId(request.getAccountId())
                .type("DEPOSIT")
                .amount(amt)
                .date(LocalDateTime.now())
                .status("SUCCESS")
                .build();

        transactionRepository.save(transaction);

        // 🔔 Notify owner
        if (account.getCustomerId() != null) {
            String msg = String.format(
                    "Deposit of ₹%.2f to Account #%d successful. New balance: ₹%.2f",
                    amt, account.getId(), newBalance);
            try {
                notificationClient.send(new SendNotificationRequest(account.getCustomerId(), msg));
            } catch (Exception e) {
                // Log but don't fail the operation if notification service is down
            }
        }

        return new TransactionResponse("Deposit Successful", newBalance);
    }

    @Transactional
    public TransactionResponse withdraw(TransactionRequest request) {
        AccountDTO account = accountClient.getAccount(request.getAccountId());

        // ✅ Only Active accounts can withdraw
        ensureActive(account, "Withdrawal not allowed: account is not Active");

        double amt = safeAmount(request.getAmount());
        double current = safeBalance(account.getBalance());

        if (current < amt) {
            throw new InsufficientFundsException(
                    String.format("Insufficient Balance: available ₹%.2f, requested ₹%.2f", current, amt));
        }

        double newBalance = current - amt;
        accountClient.updateBalance(request.getAccountId(), newBalance);

        Transaction transaction = Transaction.builder()
                .accountId(request.getAccountId())
                .type("WITHDRAWAL")
                .amount(amt)
                .date(LocalDateTime.now())
                .status("SUCCESS")
                .build();

        transactionRepository.save(transaction);

        // 🔔 Notify owner
        if (account.getCustomerId() != null) {
            String msg = String.format(
                    "Withdrawal of ₹%.2f from Account #%d successful. New balance: ₹%.2f",
                    amt, account.getId(), newBalance);
            try {
                notificationClient.send(new SendNotificationRequest(account.getCustomerId(), msg));
            } catch (Exception e) {
                // Log but don't fail the operation if notification service is down
            }
        }

        return new TransactionResponse("Withdrawal Successful", newBalance);
    }

    @Transactional
    public TransactionResponse transfer(TransactionRequest request) {
        AccountDTO fromAccount = accountClient.getAccount(request.getAccountId());
        AccountDTO toAccount = accountClient.getAccount(request.getToAccountId());

        // ✅ Both accounts must be Active
        ensureActive(fromAccount, "Transfer not allowed: sender account is not Active");
        ensureActive(toAccount, "Transfer not allowed: receiver account is not Active");

        double amt = safeAmount(request.getAmount());
        double fromBal = safeBalance(fromAccount.getBalance());
        double toBal = safeBalance(toAccount.getBalance());

        if (fromBal < amt) {
            throw new InsufficientFundsException(
                    String.format("Insufficient Balance: available ₹%.2f, requested ₹%.2f", fromBal, amt));
        }

        double newFromBal = fromBal - amt;
        double newToBal = toBal + amt;

        accountClient.updateBalance(request.getAccountId(), newFromBal);
        accountClient.updateBalance(request.getToAccountId(), newToBal);

        Transaction transaction = Transaction.builder()
                .accountId(request.getAccountId())
                .type("TRANSFER")
                .amount(amt)
                .date(LocalDateTime.now())
                .status("SUCCESS")
                .build();

        transactionRepository.save(transaction);

        // 🔔 Notify sender
        if (fromAccount.getCustomerId() != null) {
            String msg = String.format(
                    "Transfer of ₹%.2f from Account #%d to Account #%d successful. New balance: ₹%.2f",
                    amt, fromAccount.getId(), toAccount.getId(), newFromBal);
            try {
                notificationClient.send(new SendNotificationRequest(fromAccount.getCustomerId(), msg));
            } catch (Exception e) {
                // Log but don't fail
            }
        }

        // 🔔 Notify receiver
        if (toAccount.getCustomerId() != null) {
            String msg = String.format(
                    "You received ₹%.2f in Account #%d from Account #%d. New balance: ₹%.2f",
                    amt, toAccount.getId(), fromAccount.getId(), newToBal);
            try {
                notificationClient.send(new SendNotificationRequest(toAccount.getCustomerId(), msg));
            } catch (Exception e) {
                // Log but don't fail
            }
        }

        return new TransactionResponse("Transfer Successful", newFromBal);
    }

    /** Helper: throw an IllegalStateException if account is not Active. */
    private void ensureActive(AccountDTO account, String messageIfNotActive) {
        String status = (account.getStatus() == null) ? "" : account.getStatus().trim();
        if (!"Active".equalsIgnoreCase(status)) {
            throw new IllegalStateException(messageIfNotActive + " (status: " + account.getStatus() + ")");
        }
    }

    /** Helper: validate amount presence and positivity. */
    private double safeAmount(Double amount) {
        if (amount == null) {
            throw new IllegalArgumentException("amount is required");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be > 0");
        }
        return amount;
    }

    /** Helper: normalize null balance to 0. */
    private double safeBalance(Double balance) {
        return balance == null ? 0.0 : balance;
    }
}