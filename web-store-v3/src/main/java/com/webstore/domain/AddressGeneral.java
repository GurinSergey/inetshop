package com.webstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "etc",name = "nova_poshta_adress")
//@JsonIgnoreProperties({ "Reception", "Delivery" ,"Schedule"})

//LAO в связи с тем что названия импортируются с заглавными буквами, к нам в вебинтерфейс они так же прилетают заглавными
@JsonIgnoreProperties(ignoreUnknown=true)
public class AddressGeneral {
  /*  @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;*/
    @Column(name = "ref")
    @JsonProperty("Ref")
    private String ref;
    @Id
    @Column(name = "site_key")
    @JsonProperty("SiteKey")
    private long siteKey;
    @Column(name = "description")
    @JsonProperty("Description")
    private String description;
    @Column(name = "description_ru")
    @JsonProperty("DescriptionRu")
    private String descriptionRu;
    @Column(name = "type_of_warehouse")
    @JsonProperty("TypeOfWarehouse")
    private String typeOfWarehouse;
    @Column(name = "number")
    @JsonProperty("Number")
    private int number;
    @Column(name = "city_ref")
    @JsonProperty("CityRef")
    private String cityRef;
    @Column(name = "city_description")
    @JsonProperty("CityDescription")
    private String   cityDescription;
    @Column(name = "city_description_ru")
    @JsonProperty("CityDescriptionRu")
    private String cityDescriptionRu;
    @Column(name = "longitude")
    @JsonProperty("Longitude")
    private double  longitude;
    @Column(name = "latitude")
    @JsonProperty("Latitude")
    private double  latitude;
    @Column(name = "post_finance")
    @JsonProperty("PostFinance")
    private int  postFinance;
    @Column(name = "pos_terminal")
    @JsonProperty("POSTerminal")
    private int posTerminal;
    @Column(name = "international_shipping")
    @JsonProperty("InternationalShipping")
    private int internationalShipping;
    @Column(name = "total_max_weight_allowed")
    @JsonProperty("TotalMaxWeightAllowed")
    private int  totalMaxWeightAllowed;
    @Column(name = "place_max_weight_allowed")
    @JsonProperty("PlaceMaxWeightAllowed")
    private int placeMaxWeightAllowed;
/*    private ReceptionNova reception ;
    private ReceptionNova delivery ;
    private ReceptionNova schedule ;
*/

 /*   @OneToMany(mappedBy = "addressGeneral", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JsonManagedReference
    private Set<ReceptionNova> receptionsNovaSet = new HashSet<ReceptionNova>();
*/
    public AddressGeneral() {
    }



    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public long getSiteKey() {
        return siteKey;
    }

    public void setSiteKey(long siteKey) {
        this.siteKey = siteKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionRu() {
        return descriptionRu;
    }

    public void setDescriptionRu(String descriptionRu) {
        this.descriptionRu = descriptionRu;
    }

    public String getTypeOfWarehouse() {
        return typeOfWarehouse;
    }

    public void setTypeOfWarehouse(String typeOfWarehouse) {
        this.typeOfWarehouse = typeOfWarehouse;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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

    public String getCityDescriptionRu() {
        return cityDescriptionRu;
    }

    public void setCityDescriptionRu(String cityDescriptionRu) {
        this.cityDescriptionRu = cityDescriptionRu;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getPostFinance() {
        return postFinance;
    }

    public void setPostFinance(int postFinance) {
        this.postFinance = postFinance;
    }

    public int getPosTerminal() {
        return posTerminal;
    }

    public void setPosTerminal(int posTerminal) {
        this.posTerminal = posTerminal;
    }

    public int getInternationalShipping() {
        return internationalShipping;
    }

    public void setInternationalShipping(int internationalShipping) {
        this.internationalShipping = internationalShipping;
    }

    public int getTotalMaxWeightAllowed() {
        return totalMaxWeightAllowed;
    }

    public void setTotalMaxWeightAllowed(int totalMaxWeightAllowed) {
        this.totalMaxWeightAllowed = totalMaxWeightAllowed;
    }

    public int getPlaceMaxWeightAllowed() {
        return placeMaxWeightAllowed;
    }

    public void setPlaceMaxWeightAllowed(int placeMaxWeightAllowed) {
        this.placeMaxWeightAllowed = placeMaxWeightAllowed;
    }

  /*  public Set<ReceptionNova> getReceptionsNovaSet() {
        return receptionsNovaSet;
    }

    public void setReceptionsNovaSet(Set<ReceptionNova> receptionsNovaSet) {
        this.receptionsNovaSet = receptionsNovaSet;
    }*/
}
