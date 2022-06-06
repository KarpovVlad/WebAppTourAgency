package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.dto.NameDto;
import com.mycompany.myapp.domain.entity.TourCompany;
import com.mycompany.myapp.exceptions.BadRequestAlertException;
import com.mycompany.myapp.repository.TourCompanyRepository;
import com.mycompany.myapp.service.mapper.TourCompanyMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TourCompanyService {

    private final TourCompanyRepository companyRepository;
    private final TourCompanyMapper companyMapper;

    public TourCompanyService(TourCompanyRepository companyRepository, TourCompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    public List<NameDto> getAllNames() {
        return companyRepository.findAll()
            .stream()
            .map(companyMapper::toNameDto)
            .collect(Collectors.toList());
    }

    public NameDto getNameById(Long id) {
        TourCompany tourCompany = getById(id);
        return companyMapper.toNameDto(tourCompany);
    }

    public NameDto create(NameDto request) {
        TourCompany tourCompany = new TourCompany();
        tourCompany.setTourCompanyName(request.getName());
        return companyMapper.toNameDto(companyRepository.save(tourCompany));
    }

    public NameDto update(Long id, NameDto request) {
        TourCompany tourCompany = getById(id);
        tourCompany.setTourCompanyName(request.getName());
        return companyMapper.toNameDto(companyRepository.save(tourCompany));
    }

    private TourCompany getById(Long id) {
        return companyRepository.findById(id)
            .orElseThrow(() -> new BadRequestAlertException("Tour company with id: " + id + " not found",
                TourCompany.class.getSimpleName(), "idinvalid"));
    }
}
