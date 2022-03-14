package com.webstore.base.stateStepBase;


import com.webstore.domain.Order;
import com.webstore.domain.OrderProtocol;
import com.webstore.domain.User;
import com.webstore.domain.enums.OrderState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class  OrderStateHandler extends StateMainHandler<OrderState>{
    private Order order;
    private OrderProtocol orderProtocol;
    private User user;

    @Override
    @Autowired
    public void setStepService(StepService stepService) {
        super.setStepService(stepService);
    }


    @Override
     protected  OrderStateStep createStateStep(OrderState state) {
        OrderStateStep stateStep = new BaseStep();
        if (state == OrderState.IN_PROCESS) {
            stateStep = new StepGetToWork();
        }
        else if (state == OrderState.CHECK_REST) {
            stateStep = new StepGetToWork();
        }
        else if (state == OrderState.CONFIRM){
            stateStep = new StepConfirm();
        }
        else if (state == OrderState.RECEIVED){
            stateStep = new StepReceived();
        }
        else if (state == OrderState.CLOSED){
            stateStep = new StepClose();
        }
        else if (state == OrderState.REJECTED){
            stateStep = new StepReject();
        }

        stateStep.init(order,state,user,orderProtocol);
        stateStep.setStepService(stepService);
        return stateStep;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderProtocol getOrderProtocol() {
        return orderProtocol;
    }

    public void setOrderProtocol(OrderProtocol orderProtocol) {
        this.orderProtocol = orderProtocol;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
