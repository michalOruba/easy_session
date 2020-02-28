package com.michaloruba.obslugasesji.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "specialization")
@DiscriminatorColumn(
        name = "spec_type_id",
        discriminatorType = DiscriminatorType.INTEGER,
        columnDefinition = "int(2)"
)
@DiscriminatorValue("3")
public class InformationSpecialization implements Specialization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column (name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @Column (name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ManyToOne (fetch=FetchType.LAZY, cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    @JoinColumn(name = "spec_kind_id")
    private SpecKind specKind;



    public InformationSpecialization() {
    }

    public InformationSpecialization(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate specializationStartDate) {
        this.startDate = specializationStartDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate specializationEndDate) {
        this.endDate = specializationEndDate;
    }


    public SpecKind getSpecKind() {
        return specKind;
    }

    public void setSpecKind(SpecKind specKind){
        setSpecKind(specKind, true);
    }

    void setSpecKind(SpecKind specKind, boolean add) {
        this.specKind = specKind;
        if (specKind != null && add){
            specKind.addSpec(this, false);
        }
    }



    @Override
    public String toString() {
        return "InformationSpecialization{" +
                "id=" + id +
                ", specializationStartDate=" + startDate +
                ", specializationEndDate=" + endDate +
                '}';
    }
}
