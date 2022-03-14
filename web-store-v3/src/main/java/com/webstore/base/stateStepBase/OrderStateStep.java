package com.webstore.base.stateStepBase;

import com.webstore.base.ValidateState;
import com.webstore.base.stateStepBase.baseInterface.IStateStep;
import com.webstore.domain.Order;
import com.webstore.domain.OrderProtocol;
import com.webstore.domain.User;
import com.webstore.domain.enums.OrderState;
import com.webstore.domain.enums.ResultCode;
import com.webstore.responses.Response;
import java.util.ArrayList;
import java.util.List;

public abstract class OrderStateStep implements IStateStep<Order,OrderState,OrderProtocol> {
    OrderProtocol orderProtocol;
    Order order;
    OrderState orderState;
    User user;
    ResultCode resultCode;
    String responseMessage;
    boolean isReserve = false;
    boolean hasError = false;
    List<Response> arrayResponse = new ArrayList<Response>();
    StepService stepService;


    OrderStateStep() {
    }

    @Override
    public boolean init(Order content, OrderState state, User user,OrderProtocol orderProtocol) {
        this.order = content;
        this.orderState = state;
        this.user = user;
        this.orderProtocol =  orderProtocol;
        return true;
    }

    @Override
    public boolean validate(String... arrParam) {
        if (order == null) {
            resultCode = ResultCode.NOT_FOUND;
            this.responseMessage = "Заказ не найден";
            this.hasError = true;
            return false;
        }
        if (order.getState() == OrderState.REJECTED) {
            resultCode = ResultCode.STATE_FORBIDDEN;
            orderState = OrderState.REJECTED;
            this.responseMessage = "Заказ отменен, операции запрещены";
            this.hasError = true;
            return false;
        }

        if ((orderState == OrderState.IN_PROCESS) && (ValidateState.makeResultCode(user, order) == ResultCode.READ_ONLY)) {
            resultCode = ResultCode.READ_ONLY;
            this.responseMessage = "Заказ взят другим менеджером";
            this.hasError = true;
            return false;
        }

        if (order.getState() == OrderState.CLOSED){
            resultCode = ResultCode.STATE_FORBIDDEN;
            orderState = OrderState.CLOSED;
            this.responseMessage = "Заказ закрыт, изменить статус нельзя";
            this.hasError = true;
            return false;

        }

        if ((orderState == OrderState.IN_PROCESS) || (orderState == OrderState.CHECK_REST)) {
            orderState = OrderState.IN_PROCESS;
            isReserve = true;
          /*  if ((order.getState() == OrderState.NEW) || (order.getState() == OrderState.WAIT_REST)) {
                isReserve = true;
            }*/

        }


        return true;
    }

    @Override
    public boolean before(String... arrParam) {
        return true;
    }

    @Override
    public boolean execute(String... arrParam) {
        return true;
    }

    @Override
    public boolean after(String... arrParam) {
        return true;
    }

    @Override
    public boolean rollback(String... arrParam) {
        return true;
    }


    public OrderState getOrderState() {
        return orderState;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public boolean isReserve() {
        return isReserve;
    }

    public boolean isHasError() {
        return hasError;
    }

    public List<Response> getArrayResponse() {
        return arrayResponse;
    }

    public OrderProtocol getOrderProtocol() {
        return orderProtocol;
    }

    public StepService getStepService() {
        return stepService;
    }

    public OrderStateStep setStepService(StepService stepService) {
        this.stepService = stepService;
        return this;
    }
}
