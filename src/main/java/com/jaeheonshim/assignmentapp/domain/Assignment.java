package com.jaeheonshim.assignmentapp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class Assignment {
    @Id
    private String id;
    private String userId;

    private String title;
    private String description;

    @Indexed
    private long assignedDate;

    @Indexed
    private long dueDate;

    public Assignment(String userId, String title, String description, long assignedDate, long dueDate) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.assignedDate = assignedDate;
        this.dueDate = dueDate;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public long getAssignedDate() {
        return assignedDate;
    }

    public long getDueDate() {
        return dueDate;
    }
}
