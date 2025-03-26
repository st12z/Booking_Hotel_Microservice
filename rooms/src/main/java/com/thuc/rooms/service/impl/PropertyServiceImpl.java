package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.PropertyConverter;
import com.thuc.rooms.dto.PropertyDto;
import com.thuc.rooms.entity.Property;
import com.thuc.rooms.repository.PropertyRepository;
import com.thuc.rooms.service.IPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements IPropertyService {
    private final PropertyRepository propertyRepository;

    @Override
    public List<PropertyDto> getAllProperties() {
        List<Property> properties = propertyRepository.findAll(Sort.by("id"));
        return properties.stream().map(PropertyConverter::toPropertyDto).toList();
    }
}
