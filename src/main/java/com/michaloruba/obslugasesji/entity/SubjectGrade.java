package com.michaloruba.obslugasesji.entity;


import javax.persistence.*;

@Entity
@Table(name = "subject_grade_map")
public class SubjectGrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "grade")
    @Enumerated(EnumType.STRING)
    private com.michaloruba.obslugasesji.helper.SubjectGrade grade;

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

    public SubjectGrade(com.michaloruba.obslugasesji.helper.SubjectGrade grade) {
        this.grade = grade;
    }

    public SubjectGrade(com.michaloruba.obslugasesji.helper.SubjectGrade grade, Subject subject, Session session){
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

    public com.michaloruba.obslugasesji.helper.SubjectGrade getGrade() {
        return grade;
    }

    public void setGrade(com.michaloruba.obslugasesji.helper.SubjectGrade grade) {
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
}
