package org.cashly.User;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByIdentifier(String identifier);

    void deleteByIdentifier(String identifier);
}
