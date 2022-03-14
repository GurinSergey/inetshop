package com.webstore.domain.enums;

import java.util.Calendar;

public enum PeriodType {
    SECOND(Calendar.SECOND),
    MINUTE(Calendar.MINUTE),
    MILLISECOND(Calendar.MILLISECOND),
    DATE(Calendar.DATE),
    HOUR(Calendar.HOUR);
    private int value;

    private PeriodType(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }


}

