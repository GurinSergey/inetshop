package com.webstore.repository;

import com.webstore.domain.Catalog;
import com.webstore.domain.Template;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemplateRepo extends JpaRepository<Template, Integer> {
    List<Template> findAllByCatalog(Catalog catalog_id);
}
