package com.webstore.repository;

import com.webstore.domain.Invoice;
import com.webstore.domain.Order;
import com.webstore.domain.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvoiceRepo extends JpaRepository<Invoice, Long> {

    public List<Invoice> findAllByWarehouseOrderByInvoiceDateDesc(Warehouse warehouse);
    public List<Invoice> findAllByOrderByInvoiceDate();

    public Invoice findInvoiceByOrder(Order order);

    @Modifying
    @Query( nativeQuery = true, value = "Delete from invoice_part where invoice_id = :invoice_id")
    public void deleteInvoicePartByInvoiceId(@Param("invoice_id") Long invoiceId);

    @Modifying
    @Query( nativeQuery = true, value = "Delete from invoice where id = :invoice_id")
    public void deleteInvoiceById(@Param("invoice_id") Long invoiceId);


}
