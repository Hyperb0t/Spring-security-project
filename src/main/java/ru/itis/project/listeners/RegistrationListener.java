package ru.itis.project.listeners;

import freemarker.template.Configuration;
import freemarker.template.TemplateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import ru.itis.project.events.RegistrationEvent;
import ru.itis.project.models.User;
import ru.itis.project.services.UserService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<RegistrationEvent> {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserService userService;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConf;

    @Override
    public void onApplicationEvent(RegistrationEvent event) {
        User user = event.getUser();
        userService.registerUser(user);
        String token = UUID.randomUUID().toString();
        userService.createToken(user.getId(), token);
        try {
            MimeMessageHelper helper = new MimeMessageHelper(javaMailSender.createMimeMessage(),true, "utf-8");
            helper.setTo(user.getEmail());
            helper.setSubject("Confirm registration");
            String text;
            try {
                text = FreeMarkerTemplateUtils.processTemplateIntoString(
                        freeMarkerConf.getConfiguration().getTemplate("/email/confirm.ftl"),
                        Collections.singletonMap("url", "localhost:8080/confirm?token=" + token));
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
            helper.setText(text, true);
            javaMailSender.send(helper.getMimeMessage());
        }catch (MessagingException e) {
            throw new IllegalStateException("problem with sending mail", e);
        }
    }
}
