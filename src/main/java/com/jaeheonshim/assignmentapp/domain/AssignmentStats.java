package com.jaeheonshim.assignmentapp.domain;

public class AssignmentStats {
    private int late;
    private int dueToday;
    private int completed;
    private int dueInWeek;
    private int dueTomorrow;

    public int getDueToday() {
        return dueToday;
    }

    public void setDueToday(int dueToday) {
        this.dueToday = dueToday;
    }

    public void setLate(int late) {
        this.late = late;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public void setDueInWeek(int dueInWeek) {
        this.dueInWeek = dueInWeek;
    }

    public void setDueTomorrow(int dueTomorrow) {
        this.dueTomorrow = dueTomorrow;
    }

    public int getLate() {
        return late;
    }

    public int getCompleted() {
        return completed;
    }

    public int getDueInWeek() {
        return dueInWeek;
    }

    public int getDueTomorrow() {
        return dueTomorrow;
    }
}
