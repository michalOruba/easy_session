package com.michaloruba.obslugasesji.entity;

import javax.persistence.*;

@Entity
@Table(name = "spec_type")
public class SpecKind {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch=FetchType.EAGER, cascade = {
            CascadeType.REFRESH
    })
    @JoinColumn(name = "field_of_study_id")
    private FieldOfStudy fieldOfStudy;

    public SpecKind() {
    }

    public SpecKind(String name) {
        this. name = name;
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

    public FieldOfStudy getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(FieldOfStudy fieldOfStudy){
        this.fieldOfStudy = fieldOfStudy;
    }

    @Override
    public String toString() {
        return "SpecKind{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fieldOfStudy=" + fieldOfStudy +
                '}';
    }
}
