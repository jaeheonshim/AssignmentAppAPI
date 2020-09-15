package com.jaeheonshim.assignmentapp.service;

import com.jaeheonshim.assignmentapp.UserCreationException;
import com.jaeheonshim.assignmentapp.domain.NewUserDao;
import com.jaeheonshim.assignmentapp.domain.User;
import com.jaeheonshim.assignmentapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserCreationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailVerificationService emailVerificationService;

    public boolean emailExists(String email) {
        return userRepository.findByEmailAddress(email).isPresent();
    }

    public boolean validateEmail(String email) {
        return email.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$");
    }

    public boolean validatePassword(String password) {
        return password.length() >= 8;
    }

    public User createNewUser(NewUserDao newUser) throws UserCreationException, IOException {
        if(emailExists(newUser.getEmail())) {
            throw new UserCreationException("A user with that email already exists");
        }

        if(!validateEmail(newUser.getEmail())) {
            throw new UserCreationException("Please input a valid email address");
        }

        if(!validatePassword(newUser.getPassword())) {
            throw new UserCreationException("Please input a valid password");
        }

        User user = new User();
        user.setName(newUser.getName());
        user.setEmailAddress(newUser.getEmail());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setAccountEnabled(false);

        emailVerificationService.sendEmailVerifyEmail(user.getName(), user.getEmailAddress());

        return userRepository.save(user);
    }
}
