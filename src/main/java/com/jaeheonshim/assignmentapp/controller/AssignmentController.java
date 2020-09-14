package com.jaeheonshim.assignmentapp.controller;

import com.jaeheonshim.assignmentapp.domain.Assignment;
import com.jaeheonshim.assignmentapp.domain.User;
import com.jaeheonshim.assignmentapp.repository.AssignmentRepository;
import com.jaeheonshim.assignmentapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @GetMapping("/all")
    public ResponseEntity getAllAssignments(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> userOptional = userRepository.findByEmailAddress(userDetails.getUsername());
        if(!userOptional.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        List<Assignment> assignmentList = assignmentRepository.findAllByUserIdOrderByDueDateAsc(userOptional.get().getId());
        return ResponseEntity.ok(assignmentList);
    }
}
