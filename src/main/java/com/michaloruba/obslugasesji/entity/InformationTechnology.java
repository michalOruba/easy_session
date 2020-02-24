package com.michaloruba.obslugasesji.entity;

import java.util.List;

public class InformationTechnology extends FieldOfStudy {
    private int id;
    private String name;
    private List<Specialization> specializations;


    public InformationTechnology() {
    }

    public InformationTechnology(String name, List<Specialization> specializations) {
        this.name = name;
        this.specializations = specializations;
    }

    public void setSpecializations(List<Specialization> specializations) {
        this.specializations = specializations;
    }

    public List<Specialization> getSpecializations() {
        return specializations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "InformationTechnology{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", specialization=" + specializations +
                '}';
    }
}
