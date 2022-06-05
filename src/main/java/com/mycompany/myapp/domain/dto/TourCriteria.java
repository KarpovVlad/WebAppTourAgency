package com.mycompany.myapp.domain.dto;

import java.math.BigDecimal;

public class TourCriteria {

    private BigDecimal priceFrom;

    private BigDecimal priceTo;

    private Integer personsFrom;

    private Integer personsTo;

    // todo: replace booleans with Enum
    private Boolean isExcursion;

    private Boolean isRelax;

    private Boolean isShopping;

    public BigDecimal getPriceFrom() {
        return this.priceFrom;
    }

    public BigDecimal getPriceTo() {
        return this.priceTo;
    }

    public Integer getPersonsFrom() {
        return this.personsFrom;
    }

    public Integer getPersonsTo() {
        return this.personsTo;
    }

    public Boolean getIsExcursion() {
        return this.isExcursion;
    }

    public Boolean getIsRelax() {
        return this.isRelax;
    }

    public Boolean getIsShopping() {
        return this.isShopping;
    }

    public void setPriceFrom(BigDecimal priceFrom) {
        this.priceFrom = priceFrom;
    }

    public void setPriceTo(BigDecimal priceTo) {
        this.priceTo = priceTo;
    }

    public void setPersonsFrom(Integer personsFrom) {
        this.personsFrom = personsFrom;
    }

    public void setPersonsTo(Integer personsTo) {
        this.personsTo = personsTo;
    }

    public void setIsExcursion(Boolean isExcursion) {
        this.isExcursion = isExcursion;
    }

    public void setIsRelax(Boolean isRelax) {
        this.isRelax = isRelax;
    }

    public void setIsShopping(Boolean isShopping) {
        this.isShopping = isShopping;
    }
}
