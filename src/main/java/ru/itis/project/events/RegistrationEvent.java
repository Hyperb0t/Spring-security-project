package ru.itis.project.events;

import org.springframework.context.ApplicationEvent;
import ru.itis.project.models.User;

public class RegistrationEvent extends ApplicationEvent {
    private User user;

    public RegistrationEvent(User user) {
        super(user);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
