package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.CityConverter;
import com.thuc.rooms.dto.CityDto;
import com.thuc.rooms.entity.City;
import com.thuc.rooms.repository.CityRepository;
import com.thuc.rooms.service.ICityService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CityServiceImpl implements ICityService {
    private final CityRepository cityRepository;
    private final Logger log = LoggerFactory.getLogger(CityServiceImpl.class);
    @Override
    public List<CityDto> getAllCities() {
        log.debug("Request to get all Cities successfully");
        List<City> cities = cityRepository.findAll();
        return cities.stream().map(CityConverter::toCityDto).toList();
    }
}
