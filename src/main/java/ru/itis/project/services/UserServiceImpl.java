package ru.itis.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.itis.project.dto.UserFormDto;
import ru.itis.project.models.User;
import ru.itis.project.models.VerificationToken;
import ru.itis.project.repositories.UserRepository;
import ru.itis.project.repositories.VerificationTokenRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Optional;

@Component
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void registerUser(User user) {
        //System.out.println("encoding this: " + user.getPassword());
        //String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(user.getPassword());
        user.setEnabled(false);
        userRepository.save(user);
    }

    public void createToken(Long userId, String token) {
        VerificationToken verificationToken = new VerificationToken(userId, token,
                LocalDate.now().plus(1, ChronoUnit.DAYS));
        verificationTokenRepository.save(verificationToken);
    }

    public void enableUser(String token) {
        Optional<VerificationToken> tokenOptional = verificationTokenRepository.findByToken(token);
        if(!tokenOptional.isPresent()) {
            throw new IllegalArgumentException("token " + token + " not found");
        }
        Long id = tokenOptional.get().getId();
        Optional<User> userOptional =  userRepository.find(id);
        if(!userOptional.isPresent()) {
            throw new IllegalArgumentException("user with id=" + id + " not found");
        }
        User user = userOptional.get();
        user.setEnabled(true);
        userRepository.delete(user.getId());
        userRepository.save(user);
    }

    public Optional<User> loginUser(UserFormDto userFormDto) {
        userFormDto.setPassword(encoder.encode(userFormDto.getPassword()));
        return userRepository.findByEmailAndPassword(userFormDto.getEmail(), userFormDto.getPassword());
    }
}
