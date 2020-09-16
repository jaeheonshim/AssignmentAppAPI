package com.jaeheonshim.assignmentapp.domain;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

// fucking name it "Course" next time dumbass
public class AssignmentClass {
    @Id
    private String id = new ObjectId().toString();

    private int index;
    private String name;
    private String teacherName;
    private String color;

    public AssignmentClass(int index, String name, String teacherName, String color) {
        this.index = index;
        this.name = name;
        this.teacherName = teacherName;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getColor() {
        return color;
    }
}
