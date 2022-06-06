package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.dto.NameDto;
import com.mycompany.myapp.domain.entity.Category;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TourCategoryMapper {

    public NameDto toNameDto(Category category) {
        if (Objects.isNull(category)) {
            return null;
        }
        return new NameDto(category.getId(), category.getName());
    }

}
