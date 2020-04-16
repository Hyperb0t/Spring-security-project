package ru.itis.project.services;

import ru.itis.project.dto.UserFormDto;
import ru.itis.project.models.User;

import java.util.Optional;

public interface UserService {

    public void registerUser(User user);
    public Optional<User> loginUser(UserFormDto userFormDto);
    public void enableUser(String token);
    public void createToken(Long userId, String token);
}
