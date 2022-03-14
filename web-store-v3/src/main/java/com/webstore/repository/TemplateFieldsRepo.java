package com.webstore.repository;

import com.webstore.domain.InvoicePart;
import com.webstore.domain.Template;
import com.webstore.domain.TemplateFields;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TemplateFieldsRepo extends JpaRepository<TemplateFields, Integer> {

    @Query("select tf from TemplateFields tf where tf.template.id in (select p.template.id from Product p where p.id in ?1) order by tf.order asc")
    List<TemplateFields> getAllByTemplateOrderByOrderAsc(Long[] invoiceId);

}
