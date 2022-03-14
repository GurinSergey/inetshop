package com.webstore.service;

import com.webstore.domain.Invoice;
import com.webstore.domain.InvoicePart;
import com.webstore.domain.Warehouse;
import com.webstore.domain.enums.InvoiceState;
import com.webstore.domain.json.ProductRest;
import com.webstore.repository.ProductRestDateRepo;
import com.webstore.repository.ProductRestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ProductRestService {
    @Autowired
    ProductRestDateRepo productRestDateRepo;

    @Autowired
    ProductRestRepo productRestRepo;

    public void addPurchase(Date rest_date, Warehouse warehouse, InvoicePart invoicePart) {
        productRestDateRepo.addPurchase(rest_date,
                invoicePart.getProduct().getId(),
                warehouse.getId(),
                invoicePart.getInitialAmount());
    }

    public void addSales(Date rest_date, Warehouse warehouse, InvoicePart invoicePart) {
        productRestDateRepo.addSales(rest_date,
                invoicePart.getProduct().getId(),
                warehouse.getId(),
                invoicePart.getInitialAmount());
    }

    //Закрываем расходную накладную, меняем фактические остатки
    @Transactional
    public void closeInvoice(List<InvoicePart> invoiceParts, Warehouse warehouse, Date rest_date) {
        for (InvoicePart invoicePart : invoiceParts) {
            productRestDateRepo.addSales(rest_date,
                    invoicePart.getProduct().getId(),
                    warehouse.getId(),
                    invoicePart.getInitialAmount());

            invoicePart.setState(InvoiceState.CLOSED);

        }



    }


    @Transactional
    public void addPlanSales(Date rest_date, long warehouseId, long productId, int amount) {
        productRestDateRepo.addPlanSales(rest_date,
                productId,
                warehouseId,
                amount);
    }

    @Transactional
    public void removePlanSales(Date rest_date, long warehouseId, long productId, int amount) {
        productRestDateRepo.removePlanSales(rest_date,
                productId,
                warehouseId,
                amount);
    }

    public List<ProductRest> getCurrentProductRestAll(Date rest_date, int warehouseId) {
        return productRestRepo.getCurrentProductRestAll(rest_date, warehouseId);
    }

    public int getAmountActPart(Long productId, Long orderId) {
        return productRestDateRepo.getAmountActPart(productId, orderId);
    }



}
