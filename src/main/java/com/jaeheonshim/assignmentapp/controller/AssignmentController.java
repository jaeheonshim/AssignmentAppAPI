package com.jaeheonshim.assignmentapp.controller;

import com.jaeheonshim.assignmentapp.domain.*;
import com.jaeheonshim.assignmentapp.repository.AssignmentRepository;
import com.jaeheonshim.assignmentapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/all")
    public ResponseEntity getAllAssignments(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> userOptional = userRepository.findByEmailAddress(userDetails.getUsername());
        if(!userOptional.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        List<Assignment> assignmentList = assignmentRepository.findAllByUserIdOrderByDueDateAsc(userOptional.get().getId());
        return ResponseEntity.ok(assignmentList);
    }

    @GetMapping("/limitall/{page}/{size}")
    public ResponseEntity getLimitAssignments(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("page") int page, @PathVariable("size") int size, @RequestBody(required = false) AssignmentListOptionsDto assignmentListOptionsDto, @RequestParam(name = "showCompleted", defaultValue = "false", required = false) String showCompleted) {
        Optional<User> userOptional = userRepository.findByEmailAddress(userDetails.getUsername());
        if(!userOptional.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        Pageable pageable = PageRequest.of(page, size);
        Query query = new Query();
        query.with(pageable);
        query.addCriteria(Criteria.where("userId").is(userOptional.get().getId()));
        query.with(Sort.by(Sort.Direction.ASC, "dueDate"));

        if(!Boolean.parseBoolean(showCompleted)) {
            query.addCriteria(Criteria.where("completed").is(false));
        }

        List<Assignment> assignments = mongoTemplate.find(query, Assignment.class);

        Map<String, Object> response = new HashMap<>();
        response.put("size", assignmentRepository.countAllByUserId(userOptional.get().getId()));
        response.put("assignments", assignments);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats/{date}")
    public ResponseEntity<AssignmentStats> getAssignmentStats(@AuthenticationPrincipal UserDetails userDetails, @PathVariable int date) {
        Optional<User> userOptional = userRepository.findByEmailAddress(userDetails.getUsername());

        if(!userOptional.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        User user = userOptional.get();
        AssignmentStats stats = new AssignmentStats();

        stats.setCompleted(assignmentRepository.findAllByUserIdAndCompletedTrue(user.getId()).size());
        stats.setDueTomorrow(assignmentRepository.findAllByUserIdAndDueDateAndCompletedFalse(user.getId(), date + 1).size());
        stats.setDueInWeek(assignmentRepository.findAllByUserIdAndDueDateBetweenAndCompletedFalse(user.getId(), date + 1, date + 7).size());
        stats.setLate(assignmentRepository.findAllByUserIdAndDueDateBeforeAndCompletedFalse(user.getId(), date).size());
        stats.setDueToday(assignmentRepository.findAllByUserIdAndDueDateAndCompletedFalse(user.getId(), date).size());

        return ResponseEntity.ok(stats);
    }

    @PostMapping("/save/{id}")
    public ResponseEntity<Assignment> saveAssignment(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("id") String assignmentId, @RequestBody AssignmentDto assignmentDto) {
        Optional<User> userOptional = userRepository.findByEmailAddress(userDetails.getUsername());
        Optional<Assignment> assignmentOptional = assignmentRepository.findById(assignmentId);

        if(!userOptional.isPresent() || !assignmentOptional.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        if(!assignmentOptional.get().getUserId().equals(userOptional.get().getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Assignment assignment = assignmentOptional.get();

        if(assignmentDto.getCompleted() != null) {
            assignment.setCompleted(assignmentDto.getCompleted());
        }

        if(assignmentDto.getTitle() != null) {
            assignment.setTitle(assignmentDto.getTitle());
        }

        if(assignmentDto.getDescription() != null) {
            assignment.setDescription(assignmentDto.getDescription());
        }

        if(assignmentDto.getClassId() != null) {
            assignment.setClassId(assignmentDto.getClassId());
        }

        if(assignmentDto.getCompleted() != null) {
            assignment.setCompleted(assignmentDto.getCompleted());
        }

        if(assignmentDto.getAssignedDate() != null) {
            assignment.setAssignedDate(assignmentDto.getAssignedDate());
        }

        if(assignmentDto.getDueDate() != null) {
            assignment.setDueDate(assignmentDto.getDueDate());
        }

        assignmentRepository.save(assignment);

        return ResponseEntity.ok(assignment);
    }

    @PostMapping("/new")
    public ResponseEntity<Assignment> newAssignment(@AuthenticationPrincipal UserDetails userDetails, @RequestBody AssignmentDto assignmentDto) {
        Optional<User> userOptional = userRepository.findByEmailAddress(userDetails.getUsername());

        if(!userOptional.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        boolean classValid = false;
        for(AssignmentClass assignmentClass : userOptional.get().getAssignmentClasses()) {
            if(assignmentClass.getId().equals(assignmentDto.getClassId())) {
                classValid = true;
                break;
            }
        }

        if(!classValid) {
            return ResponseEntity.badRequest().build();
        }

        Assignment assignment = new Assignment(userOptional.get().getId(), assignmentDto.getTitle(), assignmentDto.getDescription(), assignmentDto.getClassId(), assignmentDto.getAssignedDate(), assignmentDto.getDueDate());
        assignmentRepository.save(assignment);

        return ResponseEntity.ok(assignment);
    }
}
