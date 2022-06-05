package com.mycompany.myapp.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TourCompany.
 */
@Entity
@Table(name = "tour_company")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TourCompany implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "tour_company_name")
    private String tourCompanyName;

    @OneToMany(mappedBy = "tourCompany")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "categories", "tourCompany" }, allowSetters = true)
    private Set<Tour> tours = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TourCompany id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTourCompanyName() {
        return this.tourCompanyName;
    }

    public TourCompany tourCompanyName(String tourCompanyName) {
        this.setTourCompanyName(tourCompanyName);
        return this;
    }

    public void setTourCompanyName(String tourCompanyName) {
        this.tourCompanyName = tourCompanyName;
    }

    public Set<Tour> getTours() {
        return this.tours;
    }

    public void setTours(Set<Tour> tours) {
        if (this.tours != null) {
            this.tours.forEach(i -> i.setTourCompany(null));
        }
        if (tours != null) {
            tours.forEach(i -> i.setTourCompany(this));
        }
        this.tours = tours;
    }

    public TourCompany tours(Set<Tour> tours) {
        this.setTours(tours);
        return this;
    }

    public TourCompany addTour(Tour tour) {
        this.tours.add(tour);
        tour.setTourCompany(this);
        return this;
    }

    public TourCompany removeTour(Tour tour) {
        this.tours.remove(tour);
        tour.setTourCompany(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TourCompany)) {
            return false;
        }
        return id != null && id.equals(((TourCompany) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TourCompany{" +
            "id=" + getId() +
            ", tourCompanyName='" + getTourCompanyName() + "'" +
            "}";
    }
}
