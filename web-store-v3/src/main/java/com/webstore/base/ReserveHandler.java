package com.webstore.base;

import com.webstore.base.stateStepBase.StepService;
import com.webstore.domain.*;
import com.webstore.domain.enums.MySQLException;
import com.webstore.domain.enums.OrderState;
import com.webstore.domain.enums.ResultCode;
import com.webstore.responses.Response;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.orm.jpa.JpaSystemException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReserveHandler {
    private OrderState orderState;
    private Boolean hasError = false;
    private List<Response> arrayResponse = new ArrayList<Response>();

    private Order order;
    private OrderProtocol orderProtocol;


    private StepService stepService;


    public ReserveHandler(OrderState orderState,
                          Order order,
                          OrderProtocol orderProtocol,
                          StepService stepService) {
        this.orderState = orderState;
        this.order = order;
        this.orderProtocol = orderProtocol;
        this.stepService = stepService;


    }

    public OrderState getOrderState() {
        return orderState;
    }

    public Boolean getHasError() {
        return hasError;
    }

    public List<Response> getArrayResponse() {
        return arrayResponse;
    }

    public ReserveHandler makeReserve() {
        for (OrderDetails orderDetails : order.getOrderDetails()) {
            try {

                //если уже зарезервирован или отменен тогда берем следующий
                if ((orderDetails.isCanceled()) || (orderDetails.isLock())) {
                    continue;
                }

                //Сохраним кол-во товара в заказе и накладной
                //проверим остаток по накладным, хватает ли его для резервирования
                int orderQuantity = orderDetails.getQuantity();
                int allCount = stepService.getProductRestService().getAmountActPart(orderDetails.getProduct().getId(), order.getOrderId());

                if (orderQuantity > allCount) {
                    hasError = true;
                    arrayResponse.add(Response.ok(ResultCode.NO_REST, orderDetails.getId()));
                    orderProtocol.addLineProtocol("Товар id:" + orderDetails.getProduct().getId() + " недостаточное кол-во товара в активных накладных");
                    orderState = OrderState.WAIT_REST;
                    continue;
                }


                //Берем все активные накладные(invoice_part) с таким товаром
                for (BigInteger aLong : stepService.getInvoiceService().getActivePartByProductId(orderDetails.getProduct().getId())) {
                    // System.out.println(aLong);
                    // Блокируем запись в накладной во время изменения остатка и установки резерва
                    InvoicePart invoicePart = stepService.getInvoiceService().findByIdAndLock(aLong.longValue());
                    int invoicePartCount = invoicePart.getRestAmount();
                    int reserveCount_ = 0;

                    if (invoicePartCount <= orderQuantity) {
                        orderQuantity = orderQuantity - invoicePartCount;
                        reserveCount_ = invoicePartCount;
                        invoicePartCount = 0;

                    } else {
                        invoicePartCount = invoicePartCount - orderQuantity;
                        reserveCount_ = orderQuantity;
                        orderQuantity = 0;
                    }


                    // при чекине сразу добавляем претензию резервирования для учета планируемых остатков в разрезе активных накладных
                    ReservOrderDetail reservOrderDetail = new ReservOrderDetail(orderDetails.getId(),
                            order.getOrderId(),
                            orderDetails.getProduct().getId(),
                            reserveCount_,
                            new Date(),
                            1/*пока так...еще не придумал как сделать независимые склады*/,
                            invoicePart.getId());
                    stepService.getReserveOrderService().saveReserve(reservOrderDetail);

                    //меняем остаток по накладной
                    invoicePart.setRestAmount(invoicePartCount);
                    invoicePart.setActive(invoicePartCount > 0);

                    stepService.getInvoiceService().updateInvoicePart(invoicePart);
                    System.out.println("quantity:");
                    System.out.println("invoicePart.getRestAmount()");
                    System.out.println(invoicePart.getRestAmount());

                    if (orderQuantity == 0) {
                        break;
                    }

                }


                orderDetails.setLock(true);

                orderProtocol.addLineProtocol("Товар id:" + orderDetails.getProduct().getId() + " учтен на остатках");
                arrayResponse.add(Response.ok(ResultCode.SUCCESSFULL, orderDetails.getId()));


            } catch (JpaSystemException e) {
                hasError = true;
                if (e.getCause() instanceof GenericJDBCException) {
                    GenericJDBCException e_ = (GenericJDBCException) e.getCause();
                    if (e_.getErrorCode() == MySQLException.ER_VIEW_CHECK_FAILED.toValue()) {
                        arrayResponse.add(Response.ok(ResultCode.NO_REST, orderDetails.getId()));
                        orderProtocol.addLineProtocol("Товар id:" + orderDetails.getProduct().getId() + " недостаточное кол-во на остатках");
                        orderState = OrderState.WAIT_REST;
                    } else {
                        orderProtocol.addLineProtocol("Товар id:" + orderDetails.getId() + " ошибка:")
                                .addLineProtocol(e_.getCause().getMessage());
                        arrayResponse.add(Response.ok(ResultCode.BAD_DATA, orderDetails.getProduct().getId()));
                        orderState = OrderState.MANUAL_PROCESSING;
                    }
                } else {
                    orderProtocol.addLineProtocol("Товар id:" + orderDetails.getId() + "Неизвестная Jpa ошибка:").addLineProtocol(e.getMessage());
                    arrayResponse.add(Response.ok(ResultCode.BAD_DATA, orderDetails.getProduct().getId()));
                    orderState = OrderState.MANUAL_PROCESSING;
                }
            } catch (Exception e) {
                hasError = true;
                orderProtocol.addLineProtocol("Товар id:" + orderDetails.getProduct().getId() + "Неизвестная ошибка:").addLineProtocol(e.getMessage());
                arrayResponse.add(Response.ok(ResultCode.BAD_DATA, orderDetails.getId()));
                orderState = OrderState.MANUAL_PROCESSING;
            }

        }
        return this;
    }

    public ReserveHandler removeReserve() {

        List<ReservOrderDetail> reservOrderDetails = stepService.getReserveOrderService().findAllReservByOrderId(order.getOrderId());

        for (ReservOrderDetail reservOrderDetail : reservOrderDetails) {
            InvoicePart invoicePart = stepService.getInvoiceService().findByIdAndLock(reservOrderDetail.getInvoicePartId());  // stepService.getInvoiceService().getInvoicePart(reservOrderDetail.getInvoicePartId());
            invoicePart.setRestAmount(invoicePart.getRestAmount() + reservOrderDetail.getQuantity());
            invoicePart.setActive(true);
            stepService.getInvoiceService().updateInvoicePart(invoicePart);
        }

        stepService.getReserveOrderService().removeReserve(reservOrderDetails);

        for (OrderDetails orderDetails : order.getOrderDetails()) {
            orderDetails.setLock(false);
            orderDetails.setCanceled(true);
            orderProtocol.addLineProtocol("Товар id:" + orderDetails.getProduct().getId() + " отменен, планируемые остатки пересчитаны");
            arrayResponse.add(Response.ok(ResultCode.SUCCESSFULL, orderDetails.getId()));
        }
        return this;
    }


}