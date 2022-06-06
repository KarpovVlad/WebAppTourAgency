package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.dto.NameDto;
import com.mycompany.myapp.domain.dto.TourDto;
import com.mycompany.myapp.domain.entity.Tour;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class TourMapper {

    private final TourCompanyMapper companyMapper;
    private final TourCategoryMapper categoryMapper;

    public TourMapper(TourCompanyMapper companyMapper, TourCategoryMapper categoryMapper) {
        this.companyMapper = companyMapper;
        this.categoryMapper = categoryMapper;
    }

    public NameDto toNameDto(Tour tour) {
        if (Objects.isNull(tour)) {
            return null;
        }
        return new NameDto(tour.getId(), tour.getName());
    }

    public TourDto toDto(Tour tour) {
        if (Objects.isNull(tour)) {
            return null;
        }
        TourDto tourDto = new TourDto();
        tourDto.setId(tour.getId());
        tourDto.setName(tour.getName());
        tourDto.setDescription(tour.getDescription());
        if (Objects.nonNull(tour.getImage())) {
            tourDto.setImage(Base64.toBase64String(tour.getImage()));
        }
        tourDto.setImageContentType(tour.getImageContentType());
        tourDto.setPrice(tour.getPrice());
        tourDto.setPersons(tour.getPersons());
        tourDto.setHot(tour.getHot());
        tourDto.setDiscoint(tour.getDiscoint());
        tourDto.setTourCompany(companyMapper.toNameDto(tour.getTourCompany()));
        tourDto.setCategories(mapCategoriesToCategoriesNameDto(tour));
        return tourDto;
    }

    private List<NameDto> mapCategoriesToCategoriesNameDto(Tour tour) {
        return tour.getCategories()
            .stream()
            .map(categoryMapper::toNameDto)
            .collect(Collectors.toList());
    }

}
