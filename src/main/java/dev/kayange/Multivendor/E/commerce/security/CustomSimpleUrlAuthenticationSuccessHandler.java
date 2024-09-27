package dev.kayange.Multivendor.E.commerce.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component("customSimpleUrlAuthenticationSuccessHandler")
@RequiredArgsConstructor
public class CustomSimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final ActiveUserStore activeUserStore;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        HttpSession session = request.getSession(false);
        if(session != null){
            LoggedUser user = new LoggedUser(authentication.getName(), activeUserStore);
            session.setAttribute("user", user);
        }
    }
}
