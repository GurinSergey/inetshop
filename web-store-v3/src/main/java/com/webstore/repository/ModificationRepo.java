package com.webstore.repository;

import com.webstore.domain.Modification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModificationRepo extends JpaRepository<Modification, Integer> {
}
