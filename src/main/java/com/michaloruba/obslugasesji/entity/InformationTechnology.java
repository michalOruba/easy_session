package com.michaloruba.obslugasesji.entity;

import javax.persistence.*;

@Entity
public class InformationTechnology extends FieldOfStudy {
    private boolean hasComputers;

    public boolean isHasComputers() {
        return hasComputers;
    }

    public void setHasComputers(boolean hasComputers) {
        this.hasComputers = hasComputers;
    }
}