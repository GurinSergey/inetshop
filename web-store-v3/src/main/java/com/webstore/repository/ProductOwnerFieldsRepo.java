package com.webstore.repository;

import com.webstore.domain.ProductOwnerFields;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductOwnerFieldsRepo extends JpaRepository<ProductOwnerFields, Integer> {

    @Query("select coalesce(max(po.fieldsValue.value), '-') from ProductOwnerFields po where po.product.id = ?1 and po.templateFields.id = ?2")
    String getAllByProductAndTemplateFields(Long productId, int templateFieldId);

    @Query(nativeQuery = true,
            value =
            "select count(*) from product p where p.template_id = ?1\n" +
            "and exists (select 1 from product_owner_fields f where f.product_id = p.id and f.fields_value_id = ?2)")
    int getCntByValueIdAndTemplateId(int templateId, Long valueId);

}
