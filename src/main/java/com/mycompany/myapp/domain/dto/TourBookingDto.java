package com.mycompany.myapp.domain.dto;

public class TourBookingDto {

    private Long id;

    private NameDto tour;

    private NameDto user;

    private String status;

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
