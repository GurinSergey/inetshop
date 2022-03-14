package com.webstore.repository;

import com.webstore.domain.Model;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by SGurin on 19.07.2016.
 */
public interface ModelRepo extends JpaRepository<Model, Long> {
}
