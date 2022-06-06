package com.mycompany.myapp.domain.dto;

import java.math.BigDecimal;
import java.util.List;

public class TourDto {

    private Long id;

    private String name;

    private String description;

    private String image;

    private String imageContentType;

    private BigDecimal price;

    private Integer persons;

    private Boolean hot;

    private Integer discoint; // todo: fix typo

    private NameDto tourCompany;

    private List<NameDto> categories;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getPersons() {
        return persons;
    }

    public void setPersons(Integer persons) {
        this.persons = persons;
    }

    public Boolean getHot() {
        return hot;
    }

    public void setHot(Boolean hot) {
        this.hot = hot;
    }

    public Integer getDiscoint() {
        return discoint;
    }

    public void setDiscoint(Integer discount) {
        this.discoint = discount;
    }

    public NameDto getTourCompany() {
        return tourCompany;
    }

    public void setTourCompany(NameDto tourCompany) {
        this.tourCompany = tourCompany;
    }

    public List<NameDto> getCategories() {
        return categories;
    }

    public void setCategories(List<NameDto> categories) {
        this.categories = categories;
    }
}
