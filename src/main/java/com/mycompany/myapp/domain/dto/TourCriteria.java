package com.mycompany.myapp.domain.dto;

import java.math.BigDecimal;

public class TourCriteria {

    private BigDecimal priceFrom;

    private BigDecimal priceTo;

    private Integer personsFrom;

    private Integer personsTo;

    private Boolean isExcursion;

    private Boolean isRelax;

    private Boolean isShopping;

    public BigDecimal getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(BigDecimal priceFrom) {
        this.priceFrom = priceFrom;
    }

    public BigDecimal getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(BigDecimal priceTo) {
        this.priceTo = priceTo;
    }

    public Integer getPersonsFrom() {
        return personsFrom;
    }

    public void setPersonsFrom(Integer personsFrom) {
        this.personsFrom = personsFrom;
    }

    public Integer getPersonsTo() {
        return personsTo;
    }

    public void setPersonsTo(Integer personsTo) {
        this.personsTo = personsTo;
    }

    public Boolean getIsExcursion() {
        return isExcursion;
    }

    public void setIsExcursion(Boolean excursion) {
        isExcursion = excursion;
    }

    public Boolean getIsRelax() {
        return isRelax;
    }

    public void setIsRelax(Boolean relax) {
        isRelax = relax;
    }

    public Boolean getIsShopping() {
        return isShopping;
    }

    public void setIsShopping(Boolean shopping) {
        isShopping = shopping;
    }
}
