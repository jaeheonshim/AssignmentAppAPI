package com.jaeheonshim.assignmentapp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class AssignmentDto {
    private String title;
    private String description;
    private String classId;
    private Boolean completed;
    private Long assignedDate;
    private Long dueDate;

    public AssignmentDto(String title, String description, String classId, Boolean completed, Long assignedDate, Long dueDate) {
        this.title = title;
        this.description = description;
        this.classId = classId;
        this.completed = completed;
        this.assignedDate = assignedDate;
        this.dueDate = dueDate;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getClassId() {
        return classId;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public Long getAssignedDate() {
        return assignedDate;
    }

    public Long getDueDate() {
        return dueDate;
    }
}
