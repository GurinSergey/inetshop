package com.webstore.repository;


import com.webstore.domain.Contractor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractorRepo extends JpaRepository<Contractor, Long> {

}
