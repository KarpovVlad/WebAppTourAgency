package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.dto.TourCriteria;
import com.mycompany.myapp.domain.dto.TourDto;
import com.mycompany.myapp.domain.entity.Tour;
import com.mycompany.myapp.exceptions.BadRequestAlertException;
import com.mycompany.myapp.repository.TourCustomRepository;
import com.mycompany.myapp.service.mapper.TourMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TourService {

    private final TourCustomRepository tourRepository;
    private final TourMapper tourMapper;
    private final TourBookingService tourBookingService;

    public TourService(TourCustomRepository tourRepository, TourMapper tourMapper,
                       TourBookingService tourBookingService) {
        this.tourRepository = tourRepository;
        this.tourMapper = tourMapper;
        this.tourBookingService = tourBookingService;
    }

    public TourDto getDtoById(Long id) {
        Tour tour = getById(id);
        return tourMapper.toDto(tour);
    }

    public List<TourDto> getAll(TourCriteria criteria) {
        Sort sorting = getSorting(criteria);
        return tourRepository.findByCriteria(criteria, sorting)
            .stream()
            .map(tourMapper::toDto)
            .collect(Collectors.toList());
    }

    public void book(Long id) {
        Tour tour = getById(id);
        tourBookingService.createBooking(tour);
    }

    private Tour getById(Long id) {
        return tourRepository.findById(id)
            .orElseThrow(() -> new BadRequestAlertException("Tour with id: " + id + " not found",
                Tour.class.getSimpleName(), "idnotfound"));
    }

    private Sort getSorting(TourCriteria criteria) {
        return Objects.nonNull(criteria.getHotFirst()) && criteria.getHotFirst()
            ? Sort.by(Sort.Order.desc("hot"), Sort.Order.asc("name").ignoreCase())
            : Sort.by(Sort.Order.asc("name").ignoreCase());
    }

}
