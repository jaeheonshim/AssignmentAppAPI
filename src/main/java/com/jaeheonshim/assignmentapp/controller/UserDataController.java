package com.jaeheonshim.assignmentapp.controller;

import com.jaeheonshim.assignmentapp.domain.User;
import com.jaeheonshim.assignmentapp.domain.UserDetailsDto;
import com.jaeheonshim.assignmentapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserDataController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<User> getUser(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> userOptional = userRepository.findByEmailAddress(userDetails.getUsername());

        if (userOptional.isPresent()) {
            return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<User> saveUser(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UserDetailsDto userDetailsDto) {
        Optional<User> userOptional = userRepository.findByEmailAddress(userDetails.getUsername());

        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();

        if (userDetailsDto.getName() != null)
            user.setName(userDetailsDto.getName());

        userRepository.save(user);

        return ResponseEntity.ok(user);
    }
}
