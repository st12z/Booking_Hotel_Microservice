package com.thuc.rooms.service;



import com.thuc.rooms.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IPropertyService {
    List<PropertyDto> getAllPropertiesBySlugCity(String slugCity);

    PropertyDto getPropertyBySlug(String slug);

    List<PropertyDto> getPropertiesBySlugs(List<String> slugs);

    PropertyDto getPropertyById(Integer id);

    Integer getAmountProperties();



    PageResponseDto<List<PropertyDto>> getPropertiesByFilter(FilterDtoManage filterDto);

    PropertyDto updateProperty(PropertyDto propertyDto, List<MultipartFile> images) throws IOException;
}
