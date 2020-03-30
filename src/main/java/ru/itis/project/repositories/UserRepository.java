package ru.itis.project.repositories;

import ru.itis.project.models.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<Long, User> {
    public Optional<User> findByEmailAndPassword(String email, String password);
    public Optional<User> findByEmail(String email);
}
