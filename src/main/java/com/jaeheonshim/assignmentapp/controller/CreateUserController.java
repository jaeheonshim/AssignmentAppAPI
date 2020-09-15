package com.jaeheonshim.assignmentapp.controller;

import com.jaeheonshim.assignmentapp.UserCreationException;
import com.jaeheonshim.assignmentapp.domain.NewUserDao;
import com.jaeheonshim.assignmentapp.domain.User;
import com.jaeheonshim.assignmentapp.service.UserCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/register")
public class CreateUserController {
    @Autowired
    private UserCreationService userCreationService;

    @PostMapping("/emailexists/{email}")
    public boolean emailExists(@PathVariable("email") String email) {
        return userCreationService.emailExists(email);
    }

    @PostMapping("/create")
    public ResponseEntity createNewUser(@RequestBody NewUserDao newUser) {
        Map<String, Object> response = new HashMap<>();

        if(newUser == null || newUser.getEmail() == null || newUser.getName() == null || newUser.getPassword() == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            return ResponseEntity.ok(userCreationService.createNewUser(newUser));
        } catch(UserCreationException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
