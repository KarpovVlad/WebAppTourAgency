package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.dto.TourCriteria;
import com.mycompany.myapp.domain.entity.Tour;
import com.mycompany.myapp.repository.TourCustomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourService {

    private final TourCustomRepository tourRepository;

    public TourService(TourCustomRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    public List<Tour> getAll(TourCriteria criteria) {
        return tourRepository.findByCriteria(criteria);
    }

}
