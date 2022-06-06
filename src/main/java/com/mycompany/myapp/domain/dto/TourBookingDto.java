package com.mycompany.myapp.domain.dto;

import java.time.Instant;

public class TourBookingDto {

    private Long id;

    private NameDto tour;

    private NameDto user;

    private String status;

    private Instant createdAt;

    private Instant lastModifiedAt;

    private String lastModifiedBy;

    public Instant getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Instant lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NameDto getTour() {
        return tour;
    }

    public void setTour(NameDto tour) {
        this.tour = tour;
    }

    public NameDto getUser() {
        return user;
    }

    public void setUser(NameDto user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
