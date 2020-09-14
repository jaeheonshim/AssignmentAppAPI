package com.jaeheonshim.assignmentapp.controller;

import com.jaeheonshim.assignmentapp.domain.AssignmentClass;
import com.jaeheonshim.assignmentapp.domain.AssignmentClassDao;
import com.jaeheonshim.assignmentapp.domain.User;
import com.jaeheonshim.assignmentapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/classes")
public class AssignmentClassController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public ResponseEntity<List<AssignmentClass>> getAllAssignmentClasses(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> userOptional = userRepository.findByEmailAddress(userDetails.getUsername());

        if(!userOptional.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(userOptional.get().getAssignmentClasses());
    }

    @PutMapping("/new")
    public ResponseEntity<AssignmentClass> createNewAssignmentClass(@RequestBody AssignmentClassDao newAssignment, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> userOptional = userRepository.findByEmailAddress(userDetails.getUsername());

        if(!userOptional.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        User user = userOptional.get();
        AssignmentClass assignmentClass = new AssignmentClass(newAssignment.getIndex(), newAssignment.getName(), newAssignment.getTeacherName(), newAssignment.getColor());
        user.getAssignmentClasses().add(assignmentClass);
        userRepository.save(user);

        return ResponseEntity.ok(assignmentClass);
    }
}
