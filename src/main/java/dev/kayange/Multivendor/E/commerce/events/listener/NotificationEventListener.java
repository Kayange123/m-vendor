package dev.kayange.Multivendor.E.commerce.events.listener;

import dev.kayange.Multivendor.E.commerce.events.NotificationEvent;
import dev.kayange.Multivendor.E.commerce.service.NotificationService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationEventListener {
    private final NotificationService notificationService;

    @EventListener
    public void onNotificationEvent(@NotNull NotificationEvent notification){
        notificationService.sendNotification(notification.getId());
    }
}
