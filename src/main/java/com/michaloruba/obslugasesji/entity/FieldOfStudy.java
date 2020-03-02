package com.michaloruba.obslugasesji.entity;

import javax.persistence.*;

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
}
