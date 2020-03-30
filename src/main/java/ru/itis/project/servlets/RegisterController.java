package ru.itis.project.servlets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itis.project.events.RegistrationEvent;
import ru.itis.project.models.User;
import ru.itis.project.services.UserService;

@RequestMapping("/register")
@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @GetMapping
    public String page() {
        return "register";
    }

    @PostMapping
    public String register(@RequestParam("email")String email, @RequestParam("password")String password) {
        User user = User.builder()
                .email(email)
                .password(password)
                .build();
        eventPublisher.publishEvent(new RegistrationEvent(user));
        return "redirect: /register";
    }
}
