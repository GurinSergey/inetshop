package com.webstore.base.stateStepBase;

import com.webstore.domain.*;
import com.webstore.domain.enums.InvoiceState;
import com.webstore.domain.enums.InvoiceType;
import com.webstore.domain.enums.OrderState;
import com.webstore.domain.enums.ResultCode;

public class StepConfirm extends OrderStateStep {


    @Override
    public boolean validate(String... arrParam) {

        if (super.validate(arrParam)) {
            if (order.getState() != OrderState.IN_PROCESS){
                resultCode = ResultCode.STATE_FORBIDDEN;
                this.responseMessage = "Сменить статус на CONFIRM возможно только из статуса IN_PROCESS";
                this.hasError = true;
                orderState = order.getState();
                user = order.getOper();

                return false;
            }
            else if (stepService.getInvoiceService().findInvoiceByOrder(order) != null){
                resultCode = ResultCode.STATE_FORBIDDEN;
                this.responseMessage = "Накладная по заказу уже присутствует в системе";
                this.hasError = true;
                orderState = order.getState();
                user = order.getOper();

                return false;
            }
        } else
            return false;

        return true;

    }

    @Override
    public boolean execute(String... arrParam) {

        Warehouse warehouse = stepService.getWarehouseService().getOne(1L);

        orderProtocol.addLineProtocol("Формирование накладной");
        Invoice invoice = new Invoice(InvoiceType.SALES, order, warehouse, user, InvoiceState.WAIT);
        //add to invoicePart
        for (OrderDetails orderDetail : order.getOrderDetails()) {
            //если уже зарезервирован или отмене тогда берем следующий
            if ((orderDetail.isCanceled()) || (!orderDetail.isLock())) {
                continue;
            }
            orderProtocol.addLineProtocol("Формирование деталей по накладной");
            InvoicePart invoicePart = new InvoicePart(invoice, orderDetail.getProduct(), orderDetail.getPrice(), orderDetail.getQuantity());
            invoicePart.setSalesPrice(orderDetail.getPrice());
            invoicePart.setState(InvoiceState.WAIT);
            invoice.setInvoicePart(invoicePart);
        }
        stepService.getInvoiceService().update(invoice);

        return true;
    }
}
