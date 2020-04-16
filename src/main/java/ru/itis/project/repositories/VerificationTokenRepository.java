package ru.itis.project.repositories;

import ru.itis.project.models.VerificationToken;

import java.util.Optional;

public interface VerificationTokenRepository extends CrudRepository<Long, VerificationToken> {
    public Optional<VerificationToken> findByToken(String token);
}
