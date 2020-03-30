package ru.itis.project.servlets;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import java.util.Map;

@RequestMapping("/login")
@Controller
public class LoginController {

    @GetMapping
    public String page() {
        return "login";
    }

    

}
