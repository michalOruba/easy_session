package com.michaloruba.obslugasesji.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
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
public class InformationSpecialization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull(message = "this field is required")
    @Column (name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "this field is required")
    @FutureOrPresent(message = "date have to be equal or greater than today")
    @Column (name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ManyToOne (fetch=FetchType.LAZY, cascade = {
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
        this.specKind = specKind;
    }

    @Override
    public String toString() {
        return "InformationSpecialization{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", specKind=" + specKind +
                '}';
    }
}
