package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.CityConverter;
import com.thuc.rooms.dto.CityDto;
import com.thuc.rooms.entity.City;
import com.thuc.rooms.repository.CityRepository;
import com.thuc.rooms.service.ICityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CityServiceImpl implements ICityService {
    private final CityRepository cityRepository;
    @Override
    public List<CityDto> getAllCities() {
        List<City> cities = cityRepository.findAll();
        return cities.stream().map(CityConverter::toCityDto).toList();
    }
}
