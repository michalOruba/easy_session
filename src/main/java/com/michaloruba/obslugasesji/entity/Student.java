package com.michaloruba.obslugasesji.entity;

public class Student {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private int semester;
    private Session session;
    private Specialization specialization;
    private FieldOfStudy fieldOfStudy;


    public Student() {
    }

    public Student(String firstName, String lastName, String email, int semester, Session session,Specialization specialization, FieldOfStudy fieldOfStudy) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.semester = semester;
        this.session = session;
        this.specialization = specialization;
        this.fieldOfStudy = fieldOfStudy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }


    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public FieldOfStudy getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(FieldOfStudy fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", semester=" + semester +
                ", session=" + session +
                ", specialization=" + specialization +
                ", fieldOfStudy=" + fieldOfStudy +
                '}';
    }
}
