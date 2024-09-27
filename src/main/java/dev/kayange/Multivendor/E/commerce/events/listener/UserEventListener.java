package dev.kayange.Multivendor.E.commerce.events.listener;

import dev.kayange.Multivendor.E.commerce.enumeration.EmailTemplate;
import dev.kayange.Multivendor.E.commerce.events.UserEvent;
import dev.kayange.Multivendor.E.commerce.service.MailService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventListener {
    private final MailService mailService;

    @EventListener
    public void onUserEvent(@NotNull UserEvent event){
        switch (event.getType()){
            case REGISTRATION -> mailService.sendMail(event.getUser().getEmail(), event.getUser().getFullName(), EmailTemplate.ACTIVATE_ACCOUNT, event.getData());
            case PASSWORD_RESET -> mailService.sendMail(event.getUser().getEmail(), event.getUser().getFullName(), EmailTemplate.RESET_PASSWORD, event.getData());
            default -> {}
        }
    }
}
