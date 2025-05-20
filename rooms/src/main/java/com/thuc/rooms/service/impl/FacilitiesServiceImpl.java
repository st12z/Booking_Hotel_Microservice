package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.FacilitiesConverter;
import com.thuc.rooms.dto.FacilitiesDto;
import com.thuc.rooms.repository.FacilitiesRepository;
import com.thuc.rooms.service.IFacilitiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacilitiesServiceImpl implements IFacilitiesService {
    private final FacilitiesRepository facilitiesRepository;
    @Override
    public List<FacilitiesDto> geAllFacilities() {
        return facilitiesRepository.findAll().stream().map(FacilitiesConverter::toDto).toList();
    }
}
