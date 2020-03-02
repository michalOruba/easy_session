package com.michaloruba.obslugasesji.entity;

import javax.persistence.*;

@Entity
@Table(name = "subject")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "hours")
    private double hours;
    @Column(name = "ECTS")
    private int ECTS;
    @Column(name = "semester")
    private int semester;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "specialization_id")
    private InformationSpecialization specialization;

    public Subject() {
    }

    public Subject(String name, double hours, int ECTS, int semester) {
        this.name = name;
        this.hours = hours;
        this.ECTS = ECTS;
        this.semester = semester;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public int getECTS() {
        return ECTS;
    }

    public void setECTS(int ECTS) {
        this.ECTS = ECTS;
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
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", hours=" + hours +
                ", ECTS=" + ECTS +
                ", semester=" + semester +
                ", specialization=" + specialization +
                '}';
    }
}
