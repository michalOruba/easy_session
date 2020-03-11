package com.michaloruba.obslugasesji.helper;

public enum SubjectGradeTypes {
    NONE (Constants.NONE),
    TWO (Constants.TWO),
    THREE (Constants.THREE),
    THREE_PLUS (Constants.THREE_PLUS),
    FOUR (Constants.FOUR),
    FOUR_PLUS (Constants.FOUR_PLUS),
    FIVE (Constants.FIVE);

    private final double displayValue;

    SubjectGradeTypes(double grade) {
        this.displayValue = grade;
    }

    public double getDisplayValue() {
        return displayValue;
    }

    public static class Constants {
        public static final double NONE = 0.0;
        public static final double TWO = 2.0;
        public static final double THREE = 3.0;
        public static final double THREE_PLUS = 3.5;
        public static final double FOUR = 4.0;
        public static final double FOUR_PLUS = 4.5;
        public static final double FIVE = 5.0;
    }
}
