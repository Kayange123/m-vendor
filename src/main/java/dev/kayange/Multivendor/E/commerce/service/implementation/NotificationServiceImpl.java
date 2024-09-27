package dev.kayange.Multivendor.E.commerce.service.implementation;

import dev.kayange.Multivendor.E.commerce.dao.NotificationRepository;
import dev.kayange.Multivendor.E.commerce.entity.notification.Notification;
import dev.kayange.Multivendor.E.commerce.entity.users.Customer;
import dev.kayange.Multivendor.E.commerce.enumeration.EmailTemplate;
import dev.kayange.Multivendor.E.commerce.exception.ApiException;
import dev.kayange.Multivendor.E.commerce.service.MailService;
import dev.kayange.Multivendor.E.commerce.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

import static java.util.Collections.emptyMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final MailService mailService;

    @Override
    public Notification createNotification(Notification notification) {
       return notificationRepository.save(notification);
    }

    @Override
    public void createNotification(Notification notification, Customer vendor) {

    }

    @Override
    public void updateNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(Notification notification) {

    }

    @Override
    @Async
    public void sendNotification(Long notificationId) {
        Notification notification = findNotificationById(notificationId);

        mailService.sendNotification(notification);
    }

    private Notification findNotificationById(Long notificationId) {
        return notificationRepository.findById(notificationId).orElseThrow(
                ()-> new ApiException("Could not find notification to send")
        );
    }
}
