package com.webstore.base;

import com.webstore.domain.Order;
import com.webstore.domain.User;
import com.webstore.domain.enums.OrderState;
import com.webstore.domain.enums.ResultCode;

public class ValidateState {
    private Order order;
    private OrderState orderState;
    private User user;
    private ResultCode resultCode;
    private boolean isReserv = false;

    private String response;

    public ValidateState(Order order, OrderState orderState, User user) {
        this.order = order;
        this.orderState = orderState;
        this.user = user;

    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public String getResponse() {
        return response;
    }

    /*LAO SUCCESSFULL - никто не принимал в работу,CONTINUE - в вашей компетенции,READ_ONLY - в чужой */
    public static ResultCode makeResultCode(User user, Order order) {
        if (order.getOper() == null) {
            return ResultCode.SUCCESSFULL;
        } else if (order.getOper().equals(user)) {
            return ResultCode.CONTINUE;
        }
        return ResultCode.READ_ONLY;
    }


    public boolean isReserv() {
        return isReserv;
    }

    public boolean validate() {

        if (order == null) {
            resultCode = ResultCode.NOT_FOUND;
            this.response = "Заказ не найден";
            return false;
        }
        if (order.getState() == OrderState.REJECTED) {
            resultCode = ResultCode.READ_ONLY;
            this.response = "Заказ отменен, операции запрещены";
            return false;
        }

        if ((orderState == OrderState.IN_PROCESS) && (ValidateState.makeResultCode(user, order) == ResultCode.READ_ONLY)) {
            resultCode = ResultCode.READ_ONLY;
            this.response = "Заказ взят другим менеджером";
            return false;
        }

        if ((orderState == OrderState.IN_PROCESS) || (orderState == OrderState.CHECK_REST)) {
            orderState = OrderState.IN_PROCESS;
            if ((order.getState() == OrderState.NEW) || (order.getState() == OrderState.WAIT_REST)) {
                isReserv = true;
            }
        }


        return true;
    }

}