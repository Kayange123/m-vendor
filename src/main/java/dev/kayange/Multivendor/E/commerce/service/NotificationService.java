package dev.kayange.Multivendor.E.commerce.service;

import dev.kayange.Multivendor.E.commerce.entity.notification.Notification;
import dev.kayange.Multivendor.E.commerce.entity.users.Customer;

public interface NotificationService {
    Notification createNotification(Notification notification);
    void createNotification(Notification notification, Customer vendor);
    void updateNotification(Notification notification);
    void deleteNotification(Notification notification);
    void sendNotification(Long notificationId);
}
