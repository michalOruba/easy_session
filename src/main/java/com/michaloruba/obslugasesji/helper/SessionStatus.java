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

    public static Boolean isSessionStatus(String value){
        for (SessionStatus status : SessionStatus.values()){
            if (status.name().equalsIgnoreCase(value)) return true;
        }
        return false;
    }
}
