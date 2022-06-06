package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.dto.TourBookingDto;
import com.mycompany.myapp.domain.entity.Tour;
import com.mycompany.myapp.domain.entity.TourBooking;
import com.mycompany.myapp.domain.entity.User;
import com.mycompany.myapp.domain.enums.TourBookingStatus;
import com.mycompany.myapp.exceptions.BadRequestAlertException;
import com.mycompany.myapp.repository.TourBookingRepository;
import com.mycompany.myapp.service.mapper.TourBookingMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<TourBookingDto> getPersonalBookings() {
        return bookingRepository.findByUserId(userService.getCurrentUser().getId())
            .stream()
            .map(bookingMapper::toDto)
            .collect(Collectors.toList());
    }

    public List<TourBookingDto> getAll() {
        return bookingRepository.findAll()
            .stream()
            .map(bookingMapper::toDto)
            .collect(Collectors.toList());
    }

    public void setAsPaid(Long id) {
        TourBooking booking = getById(id);
        validatePendingStatus(booking);
        booking.setStatus(TourBookingStatus.PAID);
        bookingRepository.save(booking);
    }

    public void decline(Long id) {
        TourBooking booking = getById(id);
        validatePendingStatus(booking);
        booking.setStatus(TourBookingStatus.DECLINED);
        bookingRepository.save(booking);
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

    private TourBooking getById(Long id) {
        return bookingRepository.findById(id)
            .orElseThrow(() -> new BadRequestAlertException("Tour booking with id: " + id + " not found",
                TourBooking.class.getSimpleName(), "idnotfound"));
    }

    private void validatePendingStatus(TourBooking booking) {
        if (booking.getStatus() != TourBookingStatus.PENDING) {
            throw new BadRequestAlertException("Booking is not in pending status",
                TourBooking.class.getSimpleName(), "idinvalid");
        }
    }

    private void validateBookingCreation(User initiator, Tour tour) {
        if (bookingRepository.isPendingBookingPresent(initiator.getId(), tour.getId())) {
            throw new BadRequestAlertException("You already have a pending booking for this tour",
                TourBooking.class.getSimpleName(), "idinvalid");
        }
    }
}
