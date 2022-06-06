package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.dto.TourBookingDto;
import com.mycompany.myapp.domain.entity.TourBooking;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TourBookingMapper {

    private final UserMapper userMapper;
    private final TourMapper tourMapper;

    public TourBookingMapper(UserMapper userMapper, TourMapper tourMapper) {
        this.userMapper = userMapper;
        this.tourMapper = tourMapper;
    }

    public TourBookingDto toDto(TourBooking booking) {
        if (Objects.isNull(booking)) {
            return null;
        }
        TourBookingDto bookingDto = new TourBookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setStatus(booking.getStatus().name());
        bookingDto.setTour(tourMapper.toNameDto(booking.getTour()));
        bookingDto.setUser(userMapper.toNameDto(booking.getUser()));
        return bookingDto;
    }

}
