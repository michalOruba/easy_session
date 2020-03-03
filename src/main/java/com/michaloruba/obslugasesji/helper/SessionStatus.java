package com.michaloruba.obslugasesji.helper;


public enum SessionStatus {
    PASSED("Passed"),
    NOT_PASSED("Not passed");

    private final String displayValue;

    SessionStatus(String status){
        displayValue = status;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
