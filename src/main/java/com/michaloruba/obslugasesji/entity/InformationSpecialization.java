package com.michaloruba.obslugasesji.entity;

import java.time.LocalDate;

public abstract class InformationSpecialization implements Specialization {
    private int id;
    private String name;
    private LocalDate specializationStartDate;
    private LocalDate specializationEndDate;
    private FieldOfStudy fieldOfStudy;



    @Override
    public String toString() {
        return "InformationSpecialization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", specializationStartDate=" + specializationStartDate +
                ", specializationEndDate=" + specializationEndDate +
                ", fieldOfStudy=" + fieldOfStudy +
                '}';
    }
}
