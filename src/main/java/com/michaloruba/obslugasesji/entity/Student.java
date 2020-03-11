package com.michaloruba.obslugasesji.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull(message = "this field is required")
    @Size(min=1, message = "this field is required")
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    @NotNull(message = "this field is required")
    @Size(min=1, message = "this field is required")
    private String lastName;

    @Column(name = "email")
    @NotNull(message = "this field is required")
    @Email(message = "wrong Email format")
    private String email;

    @NotNull(message = "this field is required")
    @Min(value = 1, message = "must be greater then or equal 1")
    @Max(value = 7, message = "must be less then or equal 7")
    @Column(name = "semester")
    private int semester;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "specialization_id")
    private InformationSpecialization specialization;


    public Student() {
    }

    public Student(String firstName, String lastName, String email, int semester, InformationSpecialization specialization) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.semester = semester;
        this.specialization = specialization;
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

    public InformationSpecialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(InformationSpecialization specialization) {
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", semester=" + semester +
                ", specialization=" + specialization +
                '}';
    }
}
