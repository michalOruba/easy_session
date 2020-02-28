package com.michaloruba.obslugasesji.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "field_of_study")
@DiscriminatorColumn(
        discriminatorType = DiscriminatorType.INTEGER,
        name = "field_type_id",
        columnDefinition = "INT(11)"
)
public class FieldOfStudy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "fieldOfStudy")
    List<SpecKind> specializations = new ArrayList<>();

    public FieldOfStudy() {
    }

    public FieldOfStudy(String name) {
        this.name = name;
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

    public List<SpecKind> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(List<SpecKind> specializations) {
        this.specializations = specializations;
    }

    void addField(SpecKind specKind, boolean set){
        if (specKind != null) {
            if(getSpecializations().contains(specKind)){
                getSpecializations().set(getSpecializations().indexOf(specKind), specKind);
            }
            else{
                getSpecializations().add(specKind);
            }
            if (set){
                specKind.setFieldOfStudy(this, false);
            }
        }
    }

    public void removeSpec(SpecKind specKind){
        getSpecializations().remove(specKind);
        specKind.setFieldOfStudy(null);
    }
}
