package ru.itis.project.servlets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.itis.project.services.UserService;

@RequestMapping("/confirm")
@Controller
public class ConfirmController {

    @Autowired
    UserService userService;

    @GetMapping
    @ResponseBody
    public String confirm(@RequestParam("token") String token) {
        userService.enableUser(token);
        return "email confirmed";
    }

}
