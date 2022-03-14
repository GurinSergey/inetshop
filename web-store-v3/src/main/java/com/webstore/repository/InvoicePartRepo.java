package com.webstore.repository;

import com.webstore.domain.InvoicePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import javax.persistence.LockModeType;
import java.math.BigInteger;
import java.util.List;

public interface InvoicePartRepo extends JpaRepository<InvoicePart, Long> {

    @Query("Select inPart from InvoicePart inPart where inPart.invoice.id in ?1")
    List<InvoicePart> findInvoicePartByArrayId(Long[] invoiceId);

    @Query("Select inPart from InvoicePart inPart where inPart.invoice.id = ?1")
    List<InvoicePart> findAllByInvoiceId(Long invoiceId);

    @Query("select  coalesce(sum(p.initialPrice * p.initialAmount),0) as calc  from InvoicePart p where p.invoice.id  = ?1")
    InvoiceDetailsCust calcInitialSumById(Long invoiceId);


    @Query( nativeQuery = true,value = "select ip.id" +
            "    from invoice_part ip" +
            "      inner join invoice i on (i.id = ip.invoice_id and i.invoice_type =0)" +
            "    where ip.product_id = :p_product_id and  ip.active = 1 order by ip.id")
    List<BigInteger> getActivePartByProductId(@Param("p_product_id") Long productId);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("Select inPart from InvoicePart inPart where inPart.id = ?1")
    InvoicePart findById(Long invoiceId);

    interface InvoiceDetailsCust{
         double getCalc();
    }

}
