package ru.itis.project.servlets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import java.util.Map;

@RequestMapping("/login")
@Controller
public class LoginController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping
    public String page() {
        System.out.println(passwordEncoder.encode("qwerty"));
        return "login";
    }

    

}
