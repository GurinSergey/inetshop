package com.webstore.repository;


import com.webstore.domain.AddressGeneral;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressGeneralRepo extends JpaRepository<AddressGeneral, Integer> {

    public List<AddressGeneral> getAllByCityRefOrderByDescriptionRu(String cityRef);
}
