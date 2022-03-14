package com.webstore.base.stateStepBase;

import com.webstore.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StepService {

    private ReserveOrderService reserveOrderService;
    private InvoiceService invoiceService;
    private ProductRestService productRestService;
    private OrderDetailsService orderDetailsService;
    private OrderService orderService;
    private WarehouseService warehouseService;

    //  @Autowired in setter down

    public StepService() {
    }

    public ReserveOrderService getReserveOrderService() {
        return reserveOrderService;
    }
    @Autowired
    public void setReserveOrderService(ReserveOrderService reserveOrderService) {
        this.reserveOrderService = reserveOrderService;
    }

    public InvoiceService getInvoiceService() {
        return invoiceService;
    }
    @Autowired
    public void setInvoiceService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    public ProductRestService getProductRestService() {
        return productRestService;
    }
    @Autowired
    public void setProductRestService(ProductRestService productRestService) {
        this.productRestService = productRestService;
    }

    public OrderDetailsService getOrderDetailsService() {
        return orderDetailsService;
    }
    @Autowired
    public void setOrderDetailsService(OrderDetailsService orderDetailsService) {
        this.orderDetailsService = orderDetailsService;
    }

    public OrderService getOrderService() {
        return orderService;
    }
    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public WarehouseService getWarehouseService() {
        return warehouseService;
    }
    @Autowired
    public void setWarehouseService(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }
}
