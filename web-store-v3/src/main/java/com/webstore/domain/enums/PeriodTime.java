package com.webstore.domain.enums;

public enum PeriodTime {
    P09_12(0,12),
    P12_15(1,15),
    P15_18(2,18),
    P18_20(3,20),
    P09_20(4,20);

    private int value;
    private int hours;

    private PeriodTime(int value,int hours) {
        this.value = value;
        this.hours = hours;
    }

    public int getValue() {
        return value;
    }


    public int getHours() {
        return hours;
    }

}
