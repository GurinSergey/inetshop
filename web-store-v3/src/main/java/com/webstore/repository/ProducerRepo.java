package com.webstore.repository;

import com.webstore.domain.Producer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProducerRepo extends JpaRepository<Producer, Long>{
    Producer findByName(String name);
}
