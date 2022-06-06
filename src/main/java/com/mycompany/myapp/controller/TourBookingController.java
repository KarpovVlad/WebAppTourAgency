package com.mycompany.myapp.controller;

import com.mycompany.myapp.service.TourBookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
public class TourBookingController {
    private final Logger log = LoggerFactory.getLogger(TourBookingController.class);

    private final TourBookingService bookingService;

    public TourBookingController(TourBookingService bookingService) {
        this.bookingService = bookingService;
    }

}
