package com.cts.transaction.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "account-service")
public interface AccountClient {

    @GetMapping("/api/accounts/{id}/balance")
    Double getBalance(@PathVariable("id") Long id);

    @PutMapping("/api/accounts/{id}/balance")
    void updateBalance(@PathVariable("id") Long id, @RequestParam("newBalance") Double newBalance);
}
