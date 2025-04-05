package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.PropertyConverter;
import com.thuc.rooms.dto.PropertyDto;
import com.thuc.rooms.dto.SearchDto;
import com.thuc.rooms.entity.City;
import com.thuc.rooms.entity.Property;
import com.thuc.rooms.exception.ResourceNotFoundException;
import com.thuc.rooms.repository.CityRepository;
import com.thuc.rooms.repository.PropertyRepository;
import com.thuc.rooms.service.IPropertyService;
import com.thuc.rooms.service.IRedisPropertyService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements IPropertyService {
    private final PropertyRepository propertyRepository;
    private final CityRepository cityRepository;
    private final Logger log = LoggerFactory.getLogger(PropertyServiceImpl.class);
    private final IRedisPropertyService redisPropertyService;
    @Override
    public List<PropertyDto> getAllProperties(String slugCity) {
        Optional<City> cityOptional = cityRepository.findBySlug(slugCity);
        if(!cityOptional.isPresent()){
            log.debug("City with Slug {} not found", slugCity);
            throw new ResourceNotFoundException("City","Slug",slugCity);
        }
        log.debug("Requested to getAllProperties with {} successfully",slugCity);
        List<Property> properties = propertyRepository.findByCityId(cityOptional.get().getId());
        return properties.stream().map(PropertyConverter::toPropertyDto).toList();
    }

    @Override
    public PropertyDto getPropertyBySlug(String slug) {
        Property property = propertyRepository.findBySlug(slug);
        if(property == null){
            throw new ResourceNotFoundException("Property","Slug",slug);
        }
        return PropertyConverter.toPropertyDto(property);
    }

    @Override
    public List<PropertyDto> getPropertiesBySlugs(List<String> slugs) {
        List<PropertyDto> propertyDtos = new ArrayList<>();
        String key = String.join(",", slugs);
        if(redisPropertyService.getData(key) !=null && !redisPropertyService.getData(key).isEmpty()){
            return redisPropertyService.getData(key);
        }
        if(slugs != null && !slugs.isEmpty()){
            for(String slug : slugs){
                propertyDtos.add(getPropertyBySlug(slug));
            }
            redisPropertyService.saveData(key, propertyDtos);
        }
        return propertyDtos;
    }


}
