package com.mycompany.myapp.controller;

import com.mycompany.myapp.domain.dto.TourBookingDto;
import com.mycompany.myapp.service.TourBookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class TourBookingController {
    private final Logger log = LoggerFactory.getLogger(TourBookingController.class);

    private final TourBookingService bookingService;

    public TourBookingController(TourBookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public List<TourBookingDto> getAll() {
        log.debug("Getting list of tour bookings");
        return bookingService.getAll();
    }

    @PatchMapping("/{id}/paid")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public void setAsPaid(@PathVariable Long id) {
        log.debug("Setting PAID status for tour booking with id: {}", id);
        bookingService.setAsPaid(id);
    }

    @PatchMapping("/{id}/decline")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public void decline(@PathVariable Long id) {
        log.debug("Declining tour booking with id: {}", id);
        bookingService.decline(id);
    }

    @GetMapping("/personal")
    public List<TourBookingDto> getPersonalBookings() {
        log.debug("Getting personal tour bookings");
        return bookingService.getPersonalBookings();
    }

}
