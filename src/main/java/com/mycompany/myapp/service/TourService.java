package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.dto.TourCriteria;
import com.mycompany.myapp.domain.dto.TourDto;
import com.mycompany.myapp.domain.entity.Tour;
import com.mycompany.myapp.domain.entity.User;
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
    private final UserService userService;
    private final TourMapper tourMapper;

    public TourService(TourCustomRepository tourRepository, UserService userService, TourMapper tourMapper) {
        this.tourRepository = tourRepository;
        this.userService = userService;
        this.tourMapper = tourMapper;
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
        User currentUser = userService.getCurrentUser();
        currentUser.getBookedTours().add(getById(id));
        userService.save(currentUser);
    }

    private Tour getById(Long id) {
        return tourRepository.findById(id)
            .orElseThrow(() -> new BadRequestAlertException("Tour with id: " + id + " not found",
                Tour.class.getSimpleName(), "idinvalid"));
    }

    private Sort getSorting(TourCriteria criteria) {
        return Objects.nonNull(criteria.getHotFirst()) && criteria.getHotFirst()
            ? Sort.by(Sort.Order.desc("hot"), Sort.Order.asc("name").ignoreCase())
            : Sort.by(Sort.Order.asc("name").ignoreCase());
    }

}
