package com.cts.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SendNotificationRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private String message;
}
