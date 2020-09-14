package com.jaeheonshim.assignmentapp.repository;

import com.jaeheonshim.assignmentapp.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    public Optional<User> findByEmailAddress(String emailAddress);
}
