package dev.kayange.Multivendor.E.commerce.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureDisabledEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureDisabledEvent> {
    private final ApplicationEventPublisher publisher;

    @Override
    public void onApplicationEvent(AuthenticationFailureDisabledEvent event) {
        var data = event.getAuthentication().getDetails();
        log.info("Authentication {}", data);

    }
}
