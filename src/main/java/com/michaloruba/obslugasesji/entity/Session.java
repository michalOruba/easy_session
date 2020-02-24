package com.michaloruba.obslugasesji.entity;

import com.michaloruba.obslugasesji.helper.SessionStatus;

import java.util.Map;

public class Session {
    private int id;
    private SessionStatus sessionStatus;
    private int semester;
    private Map<Subject, Integer> subjectList;
    private Student student;

    public Session() {
        this.sessionStatus = SessionStatus.NOT_PASSED;
    }

    public SessionStatus getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(SessionStatus sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public Map<Subject, Integer> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(Map<Subject, Integer> subjectList) {
        this.subjectList = subjectList;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", sessionStatus=" + sessionStatus +
                ", semester=" + semester +
                ", subjectList=" + subjectList +
                ", student=" + student +
                '}';
    }
}