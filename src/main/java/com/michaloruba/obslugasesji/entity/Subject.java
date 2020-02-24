package com.michaloruba.obslugasesji.entity;

public class Subject {
    private int id;
    private String name;
    private double hours;
    private int ECTS;
    private int semester;
    private Specialization specialization;

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

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
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
