package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.dto.NameDto;
import com.mycompany.myapp.domain.entity.TourCompany;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TourCompanyMapper {

    public NameDto toNameDto(TourCompany tourCompany) {
        if (Objects.isNull(tourCompany)) {
            return null;
        }
        return new NameDto(tourCompany.getId(), tourCompany.getTourCompanyName());
    }

}
