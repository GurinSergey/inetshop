package com.webstore.repository;

import com.webstore.domain.ForgotList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotListRepo extends JpaRepository<ForgotList, Long>{
    ForgotList findByGuid(String guid);
}
