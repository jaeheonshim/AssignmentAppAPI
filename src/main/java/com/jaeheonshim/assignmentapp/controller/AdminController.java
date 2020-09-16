package com.jaeheonshim.assignmentapp.controller;

import com.jaeheonshim.assignmentapp.domain.User;
import com.jaeheonshim.assignmentapp.domain.UserDetailsDto;
import com.jaeheonshim.assignmentapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getuser/{userId}")
    public ResponseEntity<User> getUser(@PathVariable("userId") String userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if(userOptional.isPresent()) {
            return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/saveuser/{userId}")
    public ResponseEntity<User> saveUser(@PathVariable("userId") String userId, @RequestBody UserDetailsDto userDetails) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();
        if (userDetails.getName() != null)
            user.setName(userDetails.getName());

        if (userDetails.getEmailAddress() != null)
            user.setEmailAddress(userDetails.getEmailAddress());

        if (userDetails.getPassword() != null)
            user.setPassword(userDetails.getPassword());

        if (userDetails.isAccountEnabled() != null)
            user.setAccountEnabled(userDetails.isAccountEnabled());

        return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
    }
}
