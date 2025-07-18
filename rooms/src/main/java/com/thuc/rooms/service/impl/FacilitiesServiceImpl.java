package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.FacilitiesConverter;
import com.thuc.rooms.dto.FacilitiesDto;
import com.thuc.rooms.dto.FacilityDto;
import com.thuc.rooms.dto.PageResponseDto;
import com.thuc.rooms.entity.Facilities;
import com.thuc.rooms.exception.ResourceNotFoundException;
import com.thuc.rooms.repository.FacilitiesRepository;
import com.thuc.rooms.service.IFacilitiesService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacilitiesServiceImpl implements IFacilitiesService {
    private final FacilitiesRepository facilitiesRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<FacilitiesDto> geAllFacilities() {
        return facilitiesRepository.findAll().stream().map(FacilitiesConverter::toDto).toList();
    }

    @Override
    public PageResponseDto<List<FacilitiesDto>> getAllFacilitiesPage(String keyword, Integer pageNo, Integer pageSize) {
        StringBuilder builder = new StringBuilder(" SELECT * FROM facilities WHERE 1=1 ");
        if(keyword != null && !keyword.isEmpty()) {
            builder.append(" AND unaccent(name) ILIKE unaccent(:keyword) ");
        }
        int limit = pageSize;
        int offset = (pageNo - 1) * pageSize;
        Query queryTotal = entityManager.createNativeQuery(builder.toString().replace("SELECT *","SELECT COUNT(*)"),Long.class);
        builder.append(" ORDER BY id");
        Query query = entityManager.createNativeQuery(builder.toString(), Facilities.class);
        if(keyword != null && !keyword.isEmpty()) {
            query.setParameter("keyword",'%'+ keyword +'%');
            queryTotal.setParameter("keyword", '%'+keyword+'%');
        }
        query.setFirstResult(offset).setMaxResults(limit);
        List<Facilities> facilities = query.getResultList();
        Long total = ((Number) queryTotal.getSingleResult()).longValue();
        return PageResponseDto.<List<FacilitiesDto>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .total(total)
                .dataPage(facilities.stream().map(FacilitiesConverter::toDto).toList())
                .build();

    }

    @Override
    public FacilitiesDto getFacilityById(Integer id) {
        Facilities facility = facilitiesRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Facility","id",String.valueOf(id)));
        return FacilitiesConverter.toDto(facility);
    }

    @Override
    public FacilitiesDto update(Integer id, FacilityDto facilityDto) {
        Facilities facility = facilitiesRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Facility","id",String.valueOf(id)));
        facility.setName(facilityDto.getName());
        Facilities updatedFacility = facilitiesRepository.save(facility);
        return FacilitiesConverter.toDto(updatedFacility);
    }

    @Override
    public FacilitiesDto create(FacilityDto facilityDto) {
        Facilities facilities = Facilities.builder()
                .name(facilityDto.getName())
                .build();
        Facilities createdFacility = facilitiesRepository.save(facilities);
        return FacilitiesConverter.toDto(createdFacility);
    }
}
