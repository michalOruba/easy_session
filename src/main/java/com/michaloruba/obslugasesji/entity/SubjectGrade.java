package com.michaloruba.obslugasesji.entity;


import com.michaloruba.obslugasesji.helper.SubjectGradeTypes;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "subject_grade_map")
public class SubjectGrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "this field is required")
    @Column(name = "grade")
    @Enumerated(EnumType.STRING)
    private SubjectGradeTypes grade;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {
            CascadeType.REFRESH
    })
    @JoinColumn(name = "session_id")
    private Session session;

    public SubjectGrade() {
    }

    public SubjectGrade(SubjectGradeTypes grade) {
        this.grade = grade;
    }

    public SubjectGrade(SubjectGradeTypes grade, Subject subject, Session session){
        this(grade);
        this.subject = subject;
        this.session = session;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SubjectGradeTypes getGrade() {
        return grade;
    }

    public void setGrade(SubjectGradeTypes grade) {
        this.grade = grade;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public String toString() {
        return "SubjectGrade{" +
                "id=" + id +
                ", grade=" + grade +
                ", subject=" + subject +
                ", session=" + session +
                '}';
    }
}
