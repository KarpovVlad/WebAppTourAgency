package com.mycompany.myapp.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tour")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tour implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Column(name = "price", precision = 21, scale = 2)
    private BigDecimal price;

    @Column(name = "persons")
    private Integer persons;

    @Column(name = "hot")
    private Boolean hot;

    @Column(name = "discoint")
    private Integer discoint;

    @ManyToMany
    @JoinTable(
        name = "rel_tour__category",
        joinColumns = @JoinColumn(name = "tour_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {"tours"}, allowSetters = true)
    private Set<Category> categories = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = {"tours"}, allowSetters = true)
    private TourCompany tourCompany;

    public Long getId() {
        return this.id;
    }

    public Tour id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Tour name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Tour description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Tour image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Tour imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Tour price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getPersons() {
        return this.persons;
    }

    public Tour persons(Integer persons) {
        this.setPersons(persons);
        return this;
    }

    public void setPersons(Integer persons) {
        this.persons = persons;
    }

    public Boolean getHot() {
        return this.hot;
    }

    public Tour hot(Boolean hot) {
        this.setHot(hot);
        return this;
    }

    public void setHot(Boolean hot) {
        this.hot = hot;
    }

    public Integer getDiscoint() {
        return this.discoint;
    }

    public Tour discoint(Integer discoint) {
        this.setDiscoint(discoint);
        return this;
    }

    public void setDiscoint(Integer discoint) {
        this.discoint = discoint;
    }

    public Set<Category> getCategories() {
        return this.categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Tour categories(Set<Category> categories) {
        this.setCategories(categories);
        return this;
    }

    public Tour addCategory(Category category) {
        this.categories.add(category);
        category.getTours().add(this);
        return this;
    }

    public Tour removeCategory(Category category) {
        this.categories.remove(category);
        category.getTours().remove(this);
        return this;
    }

    public TourCompany getTourCompany() {
        return this.tourCompany;
    }

    public void setTourCompany(TourCompany tourCompany) {
        this.tourCompany = tourCompany;
    }

    public Tour tourCompany(TourCompany tourCompany) {
        this.setTourCompany(tourCompany);
        return this;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Tour)) {
            return false;
        }
        Tour otherTour = (Tour) other;
        return Objects.equals(this.id, otherTour.id)
               && Objects.equals(this.name, otherTour.name)
               && Objects.equals(this.description, otherTour.description)
               && Objects.equals(this.persons, otherTour.persons);
    }

    @Override
    public String toString() {
        return "Tour{" +
               "id=" + getId() +
               ", name='" + getName() + "'" +
               ", description='" + getDescription() + "'" +
               ", image='" + getImage() + "'" +
               ", imageContentType='" + getImageContentType() + "'" +
               ", price=" + getPrice() +
               ", persons=" + getPersons() +
               ", hot='" + getHot() + "'" +
               ", discoint=" + getDiscoint() +
               "}";
    }
}
