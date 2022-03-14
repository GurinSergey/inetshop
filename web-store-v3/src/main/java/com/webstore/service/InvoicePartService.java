package com.webstore.service;

import com.webstore.domain.InvoicePart;
import com.webstore.repository.InvoicePartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoicePartService {
    @Autowired
    private InvoicePartRepo invoicePartRepo;

    public List<InvoicePart> findInvoicePartByArrayId(Long[] invoiceId){
        return invoicePartRepo.findInvoicePartByArrayId(invoiceId);
    }
    public List<InvoicePart> findInvoicePartById(Long invoiceId){
        return invoicePartRepo.findAllByInvoiceId(invoiceId);
    }

    public double calcInitialSumById(Long invoiceId){
        return invoicePartRepo.calcInitialSumById(invoiceId).getCalc();
    }

    public InvoicePart update(InvoicePart invoicePart) {
        return invoicePartRepo.save(invoicePart);
    }

    public InvoicePart findOne(Long id){
        return invoicePartRepo.findOne(id);
    }
}
