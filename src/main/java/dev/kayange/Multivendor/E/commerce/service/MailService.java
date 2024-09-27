package dev.kayange.Multivendor.E.commerce.service;

import dev.kayange.Multivendor.E.commerce.entity.notification.Notification;
import dev.kayange.Multivendor.E.commerce.enumeration.EmailTemplate;

import java.util.Map;

public interface MailService {
    void sendMail(String to, String username, EmailTemplate emailTemplate, Map<?, ?> parameters);
    void sendNotification(Notification notification);
}
