package com.webstore.base.stateStepBase;

import com.webstore.base.ReserveHandler;
import com.webstore.domain.enums.ResultCode;


public class StepGetToWork extends OrderStateStep {

    @Override
    public boolean execute(String... arrParam) {
        if (isReserve) {
            ReserveHandler reserveHandler = new ReserveHandler(orderState, order, orderProtocol, stepService).makeReserve();
            orderState = reserveHandler.getOrderState();
            hasError = reserveHandler.getHasError();
            arrayResponse = reserveHandler.getArrayResponse();
            if (hasError) {
                resultCode = ResultCode.NO_REST;
            }
        }

        return true;
    }
}
