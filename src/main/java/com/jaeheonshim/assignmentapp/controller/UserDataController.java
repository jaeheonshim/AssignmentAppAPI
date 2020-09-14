package com.jaeheonshim.assignmentapp.controller;

import com.jaeheonshim.assignmentapp.User;
import com.jaeheonshim.assignmentapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserDataController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<User> getUser(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> userOptional = userRepository.findByEmailAddress(userDetails.getUsername());

        if(userOptional.isPresent()) {
            return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
