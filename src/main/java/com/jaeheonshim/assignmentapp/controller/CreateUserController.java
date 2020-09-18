package com.jaeheonshim.assignmentapp.controller;

import com.jaeheonshim.assignmentapp.UserCreationException;
import com.jaeheonshim.assignmentapp.domain.NewUserDao;
import com.jaeheonshim.assignmentapp.domain.User;
import com.jaeheonshim.assignmentapp.repository.UserRepository;
import com.jaeheonshim.assignmentapp.service.EmailVerificationService;
import com.jaeheonshim.assignmentapp.service.UserCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/register")
public class CreateUserController {
    @Autowired
    private UserCreationService userCreationService;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Autowired
    private UserRepository userRepository;

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

        boolean recaptchaSuccess;
        try {
            recaptchaSuccess = userCreationService.checkRecaptcha(newUser.getRecaptcha());
        } catch(IOException e) {
            response.put("error", "reCAPTCHA request to Google failed");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            if(recaptchaSuccess) {
                return ResponseEntity.ok(userCreationService.createNewUser(newUser));
            } else {
                response.put("error", "reCAPTCHA response invalid");
                return ResponseEntity.badRequest().body(response);
            }
        } catch(UserCreationException | IOException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/emailverify/{token}")
    public ResponseEntity verifyEmail(@PathVariable("token") String token) {
        return emailVerificationService.verifyEmail(token);
    }

    @PostMapping("/sendemailverify/{email}")
    public ResponseEntity sendEmailVerify(@PathVariable("email") String emailAddress) {
        Optional<User> user = userRepository.findByEmailAddress(emailAddress);
        Map<String, String> response = new HashMap<>();

        if(!user.isPresent()) {
            response.put("error", "A user with that email is not registered.");
            return ResponseEntity.badRequest().body(response);
        }

        if(user.get().isEnabled()) {
            response.put("error", "Your email address is already verified!");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            emailVerificationService.sendEmailVerifyEmail(user.get().getName(), emailAddress);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
            response.put("error", "There was an error sending the verification email. Please try again later.");
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
