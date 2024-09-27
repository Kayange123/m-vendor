package dev.kayange.Multivendor.E.commerce.security;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ActiveUserStore {
    public List<String> users;

    public ActiveUserStore() {
        users = new ArrayList<>();
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
