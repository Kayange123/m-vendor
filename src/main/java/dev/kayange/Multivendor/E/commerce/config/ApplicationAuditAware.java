package dev.kayange.Multivendor.E.commerce.config;

import dev.kayange.Multivendor.E.commerce.entity.users.Customer;
import dev.kayange.Multivendor.E.commerce.security.UserPrincipal;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;


public class ApplicationAuditAware implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) return Optional.empty();

        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();

        return Optional.ofNullable(user.getId());
    }
}
