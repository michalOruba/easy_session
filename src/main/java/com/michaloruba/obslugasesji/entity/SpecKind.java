package com.michaloruba.obslugasesji.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "spec_type")
public class SpecKind {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;

    @ManyToOne(fetch=FetchType.LAZY, cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    @JoinColumn(name = "field_of_study_id")
    private FieldOfStudy fieldOfStudy;

    @OneToMany(mappedBy = "specKind")
    private List<InformationSpecialization> specializations = new ArrayList<>();

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
        setFieldOfStudy(fieldOfStudy, true);
    }
    void setFieldOfStudy(FieldOfStudy fieldOfStudy, boolean add) {
        this.fieldOfStudy = fieldOfStudy;
        if (fieldOfStudy != null && add){
            fieldOfStudy.addField(this, false);
        }
    }

    public List<InformationSpecialization> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(List<InformationSpecialization> specializations) {
        this.specializations = specializations;
    }

    public void addSpec(InformationSpecialization specialization){
        addSpec(specialization, true);
    }

    void addSpec(InformationSpecialization specialization, boolean set){
        if (specialization != null) {
            if(getSpecializations().contains(specialization)){
                getSpecializations().set(getSpecializations().indexOf(specialization), specialization);
            }
            else{
                getSpecializations().add(specialization);
            }
            if (set){
                specialization.setSpecKind(this, false);
            }
        }
    }

    public void removeSpec(InformationSpecialization specialization){
        getSpecializations().remove(specialization);
        specialization.setSpecKind(null);
    }

}
