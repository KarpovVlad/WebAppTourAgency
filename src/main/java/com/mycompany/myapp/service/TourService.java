package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.dto.TourCriteria;
import com.mycompany.myapp.domain.entity.Tour;
import com.mycompany.myapp.domain.entity.User;
import com.mycompany.myapp.repository.TourCustomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourService {

    private final TourCustomRepository tourRepository;
    private final UserService userService;

    public TourService(TourCustomRepository tourRepository, UserService userService) {
        this.tourRepository = tourRepository;
        this.userService = userService;
    }

    public List<Tour> getAll(TourCriteria criteria) {
        return tourRepository.findByCriteria(criteria);
    }

    public void book(Long id) {
        User currentUser = userService.getCurrentUser();
        currentUser.getBookedTours().add(getById(id));
        userService.save(currentUser);
    }

    private Tour getById(Long id) {
        return tourRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tour with id: " + id + " not found"));
    }

}
