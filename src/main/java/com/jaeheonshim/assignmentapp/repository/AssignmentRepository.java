package com.jaeheonshim.assignmentapp.repository;

import com.jaeheonshim.assignmentapp.domain.Assignment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.expression.spel.ast.Assign;

import java.util.List;

public interface AssignmentRepository extends MongoRepository<Assignment, String> {
    public List<Assignment> findAllByUserIdOrderByDueDateAsc(String userId);

    public int countAllByUserId(String userId);

    public List<Assignment> findAllByUserIdAndDueDateAndCompletedTrue(String userId, long duedate);

    public List<Assignment> findAllByUserIdAndDueDateAndCompletedFalse(String userId, long duedate);

    public List<Assignment> findAllByUserIdAndDueDateBetweenAndCompletedFalse(String userId, long start, long end);

    public List<Assignment> findAllByUserIdAndDueDateBeforeAndCompletedFalse(String userId, long duedate);
}
