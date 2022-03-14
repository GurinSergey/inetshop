package com.webstore.base.stateStepBase;

import com.webstore.base.ReserveHandler;
import com.webstore.domain.Invoice;
import com.webstore.domain.enums.InvoiceState;
import com.webstore.domain.enums.ResultCode;

public class StepReject extends OrderStateStep {
    Invoice invoice = null;
    @Override
    public boolean validate(String... arrParam) {
        boolean validate = false;
        validate =  super.validate(arrParam);

      if (validate) {
           invoice = stepService.getInvoiceService().findInvoiceByOrder(order);
          if (invoice != null) {
              orderProtocol.addLineProtocol("Обнаружена накладная, id: " + invoice.getId().toString())
                      .addLineProtocol("...производится анализ возможности удаления накладной");
              if (invoice.getState() == InvoiceState.CLOSED) {
                  hasError = true;
                  resultCode = ResultCode.STATE_FORBIDDEN;
                  responseMessage = "Накладная закрыта, старт операции 'отмены' из списка заказов запрещен";
                  orderProtocol.addLineProtocol(responseMessage);
                  return false;
              }
              orderProtocol.addLineProtocol("...отмена заказа возможна");

          }
      }
        return validate;
    }

    @Override
    public boolean execute(String... arrParam) {
      if (invoice != null){
          stepService.getInvoiceService().removeInvoice(invoice);
          orderProtocol.addLineProtocol("Связанная накладная удалена");

      }
        ReserveHandler reserveHandler = new ReserveHandler(orderState, order, orderProtocol, stepService)
                .removeReserve();
        hasError = reserveHandler.getHasError();
        arrayResponse = reserveHandler.getArrayResponse();

        return true;
    }
}
