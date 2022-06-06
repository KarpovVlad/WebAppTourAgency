package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.entity.TourBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourBookingRepository extends JpaRepository<TourBooking, Long> {

    @Query(value = "select case when (count (booking) > 0) then true else false end " +
                   "from TourBooking booking " +
                   "where booking.user.id = :userId " +
                   "and booking.tour.id = :tourId " +
                   "and booking.status = 'PENDING'")
    boolean isPendingBookingPresent(@Param("userId") Long userId, @Param("tourId") Long tourId);

    List<TourBooking> findByUserId(Long userId);
}
