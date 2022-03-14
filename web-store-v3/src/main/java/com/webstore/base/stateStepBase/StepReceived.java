package com.webstore.base.stateStepBase;

import com.webstore.domain.enums.PeriodTime;

import java.util.Date;

public class StepReceived extends OrderStateStep {

    @Override
    public boolean execute(String... arrParam) {

       order.setDeliveryDate(new Date());
       if (order.getPeriodTime() == null){
           order.setPeriodTimeOnly(PeriodTime.P18_20);
       }
       return true;
    }
}
