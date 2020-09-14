package com.jaeheonshim.assignmentapp.controller;

import com.jaeheonshim.assignmentapp.AuthenticationRequest;
import com.jaeheonshim.assignmentapp.domain.User;
import com.jaeheonshim.assignmentapp.repository.UserRepository;
import com.jaeheonshim.assignmentapp.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository users;

    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody AuthenticationRequest data) {
        try {
            String email = data.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, data.getPassword()));
            Optional<User> userOptional = users.findByEmailAddress(email);

            if (!userOptional.isPresent()) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }

            String token = jwtTokenProvider.createToken(email, userOptional.get().getRoles());

            Map<Object, Object> returnModel = new HashMap<>();
            returnModel.put("email", email);
            returnModel.put("token", token);
            return ResponseEntity.ok(returnModel);
        } catch (AuthenticationException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
