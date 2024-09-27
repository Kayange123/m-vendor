package dev.kayange.Multivendor.E.commerce.events;

import dev.kayange.Multivendor.E.commerce.entity.users.Customer;
import dev.kayange.Multivendor.E.commerce.enumeration.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class UserEvent {
    private Customer user;
    private EventType type;
    private Map<?, ?> data;
}
