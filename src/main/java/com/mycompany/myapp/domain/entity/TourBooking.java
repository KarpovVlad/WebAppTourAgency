package com.mycompany.myapp.domain.entity;

import com.mycompany.myapp.domain.enums.TourBookingStatus;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = { "user_id", "tour_id" })
})
public class TourBooking extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Tour tour;

    @Enumerated(EnumType.STRING)
    private TourBookingStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public TourBookingStatus getStatus() {
        return status;
    }

    public void setStatus(TourBookingStatus status) {
        this.status = status;
    }
}
