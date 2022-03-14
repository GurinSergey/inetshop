package com.webstore.repository;

import com.webstore.domain.json.NovaCity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NovaCityRepo extends JpaRepository<NovaCity, Long> {
    List<NovaCity> getCurrentCity();
}
