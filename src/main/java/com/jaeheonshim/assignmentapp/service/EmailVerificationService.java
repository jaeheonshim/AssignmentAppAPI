package com.jaeheonshim.assignmentapp.service;

import com.jaeheonshim.assignmentapp.domain.User;
import com.jaeheonshim.assignmentapp.repository.UserRepository;
import com.sendgrid.*;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.*;

@Service
public class EmailVerificationService {
    @Autowired
    private EmailCreatorService emailCreatorService;

    @Autowired
    private UserRepository userRepository;

    @Value("${security.emailverification.token.secret-key:secret}")
    private String secretKey = "secret";

    private long validityInMilliseconds = Duration.ofDays(3).toMillis();

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public ResponseEntity verifyEmail(String token) {
        Map<Object, Object> response = new HashMap<>();
        if(!validateToken(token)) {
            response.put("error", "The verification token could not be validated. This may be because the token is expired - try requesting another email verification link.");
            return ResponseEntity.badRequest().body(response);
        }

        String email = getEmail(token);

        Optional<User> userOptional = userRepository.findByEmailAddress(email);
        if(!userOptional.isPresent()) {
            response.put("error", "An error occured with the verification token. Please try email verification again");
            return ResponseEntity.badRequest().body(response);
        }

        User user = userOptional.get();

        if(user.isEnabled()) {
            response.put("error", "This account's email address is already verified!");
            return ResponseEntity.badRequest().body(response);
        }

        user.setAccountEnabled(true);

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    public void sendEmailVerifyEmail(String name, String email) throws IOException {
        Email from = new Email("noreply@assignmentapp.com", "AssignmentApp");
        String subject = "[AssignmentApp] Verify your email";
        Email to = new Email(email);

        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("verificationlink", System.getenv("API_ENDPOINT") + "/register/emailverify/" + createToken(email));

        String emailBody = emailCreatorService.createRawEmail(EmailVerificationService.class.getResourceAsStream("/email/verifyemail.txt"), params);

        Content content = new Content("text/plain", emailBody);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException e) {
            throw new IOException("An error occurred while trying to send the verification email. Please try again later.", e);
        }
    }

    public String createToken(String email) {
        Claims claims = Jwts.claims().setSubject("VERIFYEMAIL");
        claims.put("email", email);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getEmail(String token) {
        return (String) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("email");
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            if(claims.getBody().getExpiration().before(new Date())) {
                return false;
            }

            return claims.getBody().getSubject().equals("VERIFYEMAIL");
        } catch(JwtException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
}
