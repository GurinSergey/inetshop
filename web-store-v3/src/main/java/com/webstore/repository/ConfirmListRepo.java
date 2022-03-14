package com.webstore.repository;

import com.webstore.domain.ConfirmList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmListRepo extends JpaRepository<ConfirmList, Long>{
    ConfirmList findByGuid(String guid);
}
