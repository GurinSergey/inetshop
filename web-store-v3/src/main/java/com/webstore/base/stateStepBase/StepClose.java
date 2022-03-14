package com.webstore.base.stateStepBase;

import com.webstore.domain.Invoice;
import com.webstore.domain.OrderProtocol;
import com.webstore.domain.enums.InvoiceState;
import com.webstore.domain.enums.InvoiceType;
import com.webstore.domain.enums.OrderState;
import com.webstore.domain.enums.ResultCode;
import com.webstore.responses.Response;

import java.util.Date;

public class StepClose extends OrderStateStep {
    @Override
    public boolean execute(String... arrParam) {

        //закрываем заказ, меняем статус в накладной, после чего по накладной идет расчет остатков.
        orderProtocol.addLineProtocol("Закрытие накладной");
        Invoice invoice = stepService.getInvoiceService().findInvoiceByOrder(order);

        if (!isCorrectInvoice(orderProtocol, invoice)) {
            hasError = true;
            responseMessage = "Накладная не найдена или закрыта, невозможно изменить остатки";
            orderProtocol.addLineProtocol(responseMessage);
            resultCode = ResultCode.STATE_FORBIDDEN;
            orderState = OrderState.MANUAL_PROCESSING;
            //arrayResponse.add(Response.ok(ResultCode.READ_ONLY, order.getOrderId()));
            return false;
        };

        //закрываем резерв
        stepService.getReserveOrderService().closeReserveByInvoiceId(order.getOrderId());

        stepService.getProductRestService().closeInvoice(invoice.getInvoicePart(), invoice.getWarehouse(), new Date());
        invoice.setState(InvoiceState.CLOSED);
        invoice.setOper(user);
        stepService.getInvoiceService().update(invoice);
        return true;
    }

    private boolean isCorrectInvoice(OrderProtocol orderProtocol, Invoice invoice) {
        return (invoice != null) && (invoice.getState() != InvoiceState.CLOSED) && (invoice.getInvoiceType() != InvoiceType.PURCHASE);
    }
}