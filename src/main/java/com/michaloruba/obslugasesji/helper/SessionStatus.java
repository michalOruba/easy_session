package com.michaloruba.obslugasesji.helper;


public enum SessionStatus {
    PASSED(false),
    NOT_PASSED(true);

    boolean isPassed;

    SessionStatus(boolean isPassed){
        this.isPassed = isPassed;
    }
}
