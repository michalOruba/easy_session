package com.michaloruba.obslugasesji.entity;

import javax.persistence.*;

@Entity
@DiscriminatorValue("1")
public class InformationTechnology extends FieldOfStudy {
    private boolean hasComputers;

    public boolean hasComputers() {
        return hasComputers;
    }

    public void setHasComputers(boolean hasComputers) {
        this.hasComputers = hasComputers;
    }
}
