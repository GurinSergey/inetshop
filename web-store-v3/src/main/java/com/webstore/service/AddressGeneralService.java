package com.webstore.service;


import com.webstore.domain.AddressGeneral;
import com.webstore.domain.json.NovaCity;
import com.webstore.repository.AddressGeneralRepo;
import com.webstore.repository.NovaCityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AddressGeneralService {
    @Autowired
    AddressGeneralRepo addressGeneralRepo;
    @Autowired
    NovaCityRepo novaCityRepo;

    @Value("${nova.key:default_value}")
    private String novaKey;

    public void saveAll(List<AddressGeneral> addressGenerals){
        addressGeneralRepo.save(addressGenerals);
    }

    public List<NovaCity> getAllCity(){
        return novaCityRepo.getCurrentCity();
    }


    public List<AddressGeneral> getAllByCityRef(String cityRef){
        return addressGeneralRepo.getAllByCityRefOrderByDescriptionRu(cityRef);
    }

    public List<AddressGeneral> getAll(){
        return addressGeneralRepo.findAll();
    }

    public String getNovaKey() {
        return novaKey;
    }

    public void setNovaKey(String novaKey) {
        this.novaKey = novaKey;
    }




}
