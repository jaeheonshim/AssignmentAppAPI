package com.jaeheonshim.assignmentapp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class Assignment {
    @Id
    private String id;
    private String userId;

    private String title;
    private String description;

    private String classId;

    private boolean completed;

    @Indexed
    private long assignedDate;

    @Indexed
    private long dueDate;

    public Assignment(String userId, String title, String description, String classId, long assignedDate, long dueDate) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.classId = classId;
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

    public String getClassId() {
        return classId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setAssignedDate(long assignedDate) {
        this.assignedDate = assignedDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }
}
