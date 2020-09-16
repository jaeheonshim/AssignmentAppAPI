package com.jaeheonshim.assignmentapp.repository;

import com.jaeheonshim.assignmentapp.domain.Assignment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AssignmentRepository extends MongoRepository<Assignment, String> {
    public List<Assignment> findAllByUserIdOrderByDueDateAsc(String userId);

    public int countAllByUserId(String userId);
}
