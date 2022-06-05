package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Category.
 */
@Entity
@Table(name = "category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "excursion")
    private Boolean excursion;

    @Column(name = "relax")
    private Boolean relax;

    @Column(name = "shopping")
    private Boolean shopping;

    @ManyToMany(mappedBy = "categories")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "categories", "tourCompany" }, allowSetters = true)
    private Set<Tour> tours = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Category id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Category name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getExcursion() {
        return this.excursion;
    }

    public Category excursion(Boolean excursion) {
        this.setExcursion(excursion);
        return this;
    }

    public void setExcursion(Boolean excursion) {
        this.excursion = excursion;
    }

    public Boolean getRelax() {
        return this.relax;
    }

    public Category relax(Boolean relax) {
        this.setRelax(relax);
        return this;
    }

    public void setRelax(Boolean relax) {
        this.relax = relax;
    }

    public Boolean getShopping() {
        return this.shopping;
    }

    public Category shopping(Boolean shopping) {
        this.setShopping(shopping);
        return this;
    }

    public void setShopping(Boolean shopping) {
        this.shopping = shopping;
    }

    public Set<Tour> getTours() {
        return this.tours;
    }

    public void setTours(Set<Tour> tours) {
        if (this.tours != null) {
            this.tours.forEach(i -> i.removeCategory(this));
        }
        if (tours != null) {
            tours.forEach(i -> i.addCategory(this));
        }
        this.tours = tours;
    }

    public Category tours(Set<Tour> tours) {
        this.setTours(tours);
        return this;
    }

    public Category addTour(Tour tour) {
        this.tours.add(tour);
        tour.getCategories().add(this);
        return this;
    }

    public Category removeTour(Tour tour) {
        this.tours.remove(tour);
        tour.getCategories().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return id != null && id.equals(((Category) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Category{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", excursion='" + getExcursion() + "'" +
            ", relax='" + getRelax() + "'" +
            ", shopping='" + getShopping() + "'" +
            "}";
    }
}
