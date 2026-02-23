package com.cts.notification.controller;

import com.cts.notification.dto.NotificationResponse;
import com.cts.notification.dto.SendNotificationRequest;
import com.cts.notification.security.JwtService;
import com.cts.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final JwtService jwtService;

    private Long getUserIdFromHeader(String authHeader) {
        String token = authHeader.substring(7);
        return jwtService.extractClaim(token, claims -> claims.get("uid", Long.class));
    }

    private String getRoleFromHeader(String authHeader) {
        String token = authHeader.substring(7);
        return jwtService.extractClaim(token, claims -> claims.get("role", String.class));
    }

    /** Logged-in user's notifications */
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> myNotifications(
            @RequestHeader("Authorization") String authHeader) {
        Long me = getUserIdFromHeader(authHeader);
        var list = notificationService.listForUser(me).stream()
                .map(notificationService::toResponse)
                .toList();
        return ResponseEntity.ok(list);
    }

    /** Unread count for logged-in user */
    @GetMapping("/unread-count")
    public ResponseEntity<Long> unreadCount(
            @RequestHeader("Authorization") String authHeader) {
        Long me = getUserIdFromHeader(authHeader);
        return ResponseEntity.ok(notificationService.unreadCount(me));
    }

    /** Mark a notification as READ (owner OR ADMIN/OFFICER) */
    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markRead(
            @PathVariable Long id, 
            @RequestHeader("Authorization") String authHeader) {

        Long me = getUserIdFromHeader(authHeader);
        String role = getRoleFromHeader(authHeader);

        // Fetch to check owner...
        boolean isOwner = notificationService.listForUser(me).stream().anyMatch(n -> n.getId().equals(id));
        boolean elevated = role.equals("ADMIN") || role.equals("OFFICER");

        if (!isOwner && !elevated) {
            return ResponseEntity.status(403).build();
        }

        var saved = notificationService.markRead(id);
        return ResponseEntity.ok(notificationService.toResponse(saved));
    }

    /** ADMIN/OFFICER: Send manual notification to any user */
    @PostMapping("/admin/send")
    @PreAuthorize("hasAnyRole('ADMIN','OFFICER')")
    public ResponseEntity<NotificationResponse> send(@Valid @RequestBody SendNotificationRequest req) {
        // Assume valid user since it's Admin acting
        var saved = notificationService.create(req.getUserId(), req.getMessage());
        return ResponseEntity.ok(notificationService.toResponse(saved));
    }

    /** ADMIN/OFFICER: View notifications for a specific user */
    @GetMapping("/admin/users/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','OFFICER')")
    public ResponseEntity<List<NotificationResponse>> listForUser(@PathVariable Long userId) {
        var list = notificationService.listForUser(userId).stream()
                .map(notificationService::toResponse)
                .toList();
        return ResponseEntity.ok(list);
    }
}
