package com.webstore.service;

import com.webstore.domain.Invoice;
import com.webstore.domain.InvoicePart;
import com.webstore.domain.Order;
import com.webstore.domain.Warehouse;
import com.webstore.repository.InvoicePartRepo;
import com.webstore.repository.InvoiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceRepo invoiceRepo;
    @Autowired
    private InvoicePartRepo invoicePartRepo;

    public List<Invoice> getAll() {
        return invoiceRepo.findAll();
    }

    public List<Invoice> findAllByWarehouse(Warehouse warehouse) {
        return invoiceRepo.findAllByWarehouseOrderByInvoiceDateDesc(warehouse);
    }

    public Invoice update(Invoice invoice) {
        return invoiceRepo.save(invoice);
    }

    public Invoice findById(Long id){
        return invoiceRepo.findOne(id);
    }

    public Invoice findInvoiceByOrder(Order order) {
        return invoiceRepo.findInvoiceByOrder(order);
    }

    @Transactional
    public void removeInvoice(Invoice invoice){

         invoiceRepo.deleteInvoicePartByInvoiceId(invoice.getId());
         invoiceRepo.deleteInvoiceById(invoice.getId());
         invoiceRepo.delete(invoice);
    }

    @Transactional
    public List<BigInteger> getActivePartByProductId(Long productId){
        return invoicePartRepo.getActivePartByProductId(productId);
    }

    @Transactional
    public InvoicePart findByIdAndLock(Long invoiceId){
        return invoicePartRepo.findById(invoiceId);
    }

    @Transactional
    public InvoicePart updateInvoicePart(InvoicePart invoicePart){
        return invoicePartRepo.save(invoicePart);
    }

    @Transactional
    public InvoicePart getInvoicePart(Long invoicePartId){
        return invoicePartRepo.getOne(invoicePartId);
    }

}
