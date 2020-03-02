package com.michaloruba.obslugasesji.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *  Class is not used, but it will be in project, because of future development
 *  Difference between different specializations is realize in database Spec_type entity
 */

@Entity
@DiscriminatorValue("1")
public class DataBases extends InformationSpecialization {

}
