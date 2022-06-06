package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.entity.Tour;
import com.mycompany.myapp.domain.entity.TourBooking;
import com.mycompany.myapp.domain.entity.User;
import com.mycompany.myapp.domain.enums.TourBookingStatus;
import com.mycompany.myapp.exceptions.BadRequestAlertException;
import com.mycompany.myapp.repository.TourBookingRepository;
import com.mycompany.myapp.service.mapper.TourBookingMapper;
import org.springframework.stereotype.Service;

@Service
public class TourBookingService {

    private final TourBookingRepository bookingRepository;
    private final UserService userService;
    private final TourBookingMapper bookingMapper;

    public TourBookingService(TourBookingRepository bookingRepository,
                              UserService userService, TourBookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.bookingMapper = bookingMapper;
    }

    public void createBooking(Tour tour) {
        User currentUser = userService.getCurrentUser();
        validateBookingCreation(currentUser, tour);
        TourBooking tourBooking = new TourBooking();
        tourBooking.setUser(currentUser);
        tourBooking.setTour(tour);
        tourBooking.setStatus(TourBookingStatus.PENDING);
        bookingRepository.save(tourBooking);
    }

    private void validateBookingCreation(User initiator, Tour tour) {
        if (bookingRepository.isPendingBookingPresent(initiator.getId(), tour.getId())) {
            throw new BadRequestAlertException("You already have a pending booking for this tour",
                TourBooking.class.getSimpleName(), "idinvalid");
        }
    }
}
