package com.webstore.domain.json;

import javax.persistence.*;
import java.io.Serializable;



@Entity
@SqlResultSetMapping(
        name = "cityMapping",
        classes = {
                @ConstructorResult(
                        targetClass = com.webstore.domain.json.NovaCity.class,
                        columns = {@ColumnResult(name = "city_ref", type = String.class),
                                @ColumnResult(name = "city_description_ru", type = String.class)
                                })})
@NamedNativeQuery(name = "NovaCity.getCurrentCity",
        query = "SELECT city_ref,city_description_ru from nova_poshta_adress group by city_ref,city_description_ru order by city_description_ru",
        resultSetMapping = "cityMapping")
public class NovaCity implements Serializable {
    @Id
    private String cityRef;
    private String cityDescription;

    public NovaCity() {
    }

    public NovaCity(String cityRef, String cityDescription) {
        this.cityRef = cityRef;
        this.cityDescription = cityDescription;
    }

    public String getCityRef() {
        return cityRef;
    }

    public void setCityRef(String cityRef) {
        this.cityRef = cityRef;
    }

    public String getCityDescription() {
        return cityDescription;
    }

    public void setCityDescription(String cityDescription) {
        this.cityDescription = cityDescription;
    }
}
