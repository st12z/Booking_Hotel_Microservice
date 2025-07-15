package com.thuc.rooms.service;


import com.thuc.rooms.dto.CityDto;
import com.thuc.rooms.dto.CityRequestDto;
import com.thuc.rooms.dto.PageResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ICityService {
    public List<CityDto> getAllCities();

    PageResponseDto<List<CityDto>> getAllCitiesPageResponse(Integer pageNo, Integer pageSize);

    PageResponseDto<List<CityDto>> getAllCitiesByKeyword(String keyword, Integer pageNo, Integer pageSize);

    CityDto getCityById(Integer id);

    CityDto updateCity(Integer id, String name, MultipartFile image);

    CityDto createCity(CityRequestDto cityDto, MultipartFile image);
}
