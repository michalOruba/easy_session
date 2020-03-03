package com.michaloruba.obslugasesji.entity;

import com.michaloruba.obslugasesji.helper.SessionStatus;

import javax.persistence.*;

@Entity
@Table(name = "session")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SessionStatus sessionStatus;
    @Column(name = "semester")
    private int semester;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "student_id")
    private Student student;

    public Session() {
        this.sessionStatus = SessionStatus.NOT_PASSED;
    }

    public Session(int semester) {
        this();
        this.semester = semester;
    }

    public Session(Student student, int semester){
        this(semester);
        this.student = student;
    }

    public SessionStatus getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(SessionStatus sessionStatus) {
        this.sessionStatus = sessionStatus;
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


    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", sessionStatus=" + sessionStatus +
                '}';
    }
}