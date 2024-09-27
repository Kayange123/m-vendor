package dev.kayange.Multivendor.E.commerce.security;

import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionBindingListener;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class LoggedUser implements HttpSessionBindingListener {
    private  String username;
    private ActiveUserStore activeUserStore;

    public LoggedUser(){}

    public LoggedUser(String username, ActiveUserStore activeUserStore){
        this.username = username;
        this.activeUserStore = activeUserStore;
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        List<String> users =activeUserStore.getUsers();
        LoggedUser user = (LoggedUser) event.getValue();
        if(!users.contains(user.getUsername())){
            users.add(user.getUsername());
        }
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        List<String> users = activeUserStore.getUsers();
        LoggedUser user = (LoggedUser) event.getValue();
        users.remove(user.getUsername());
    }
}
