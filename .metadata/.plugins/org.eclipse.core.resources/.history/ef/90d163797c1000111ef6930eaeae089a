package com.cts.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cts.notification.model.Notification;
import com.cts.notification.model.NotificationStatus;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
    Long countByUserIdAndStatus(Long userId, NotificationStatus status);
}
