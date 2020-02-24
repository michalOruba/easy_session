package com.michaloruba.obslugasesji.entity;

import java.time.LocalDate;

public class Programming extends InformationSpecialization {

    private int id;
    private String name;
    private LocalDate specializationStartDate;
    private LocalDate specializationEndDate;
    private FieldOfStudy fieldOfStudy;


    public Programming() {
    }

    public Programming(String name, LocalDate specializationStartDate, LocalDate specializationEndDate) {
        this.name = name;
        this.specializationStartDate = specializationStartDate;
        this.specializationEndDate = specializationEndDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public LocalDate getSpecializationEndDate() {
        return specializationEndDate;
    }

    public void setSpecializationEndDate(LocalDate specializationEndDate) {
        this.specializationEndDate = specializationEndDate;
    }

    public FieldOfStudy getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(FieldOfStudy fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public LocalDate getSpecializationStartDate() {
        return specializationStartDate;
    }

    public void setSpecializationStartDate(LocalDate specializationStartDate) {
        this.specializationStartDate = specializationStartDate;
    }
}
