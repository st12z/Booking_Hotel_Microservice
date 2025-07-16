package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.TripTypeConverter;
import com.thuc.rooms.dto.PageResponseDto;
import com.thuc.rooms.dto.TripTypeDto;
import com.thuc.rooms.dto.TripTypeRequestDto;
import com.thuc.rooms.entity.TripType;
import com.thuc.rooms.exception.ResourceNotFoundException;
import com.thuc.rooms.repository.TripTypeRepository;
import com.thuc.rooms.service.ITripService;
import com.thuc.rooms.service.ITripTypeService;
import com.thuc.rooms.utils.UploadCloudinary;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TripTypeServiceImpl implements ITripTypeService {
    private final TripTypeRepository tripTypeRepository;
    private final UploadCloudinary uploadCloudinary;
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public PageResponseDto<List<TripTypeDto>> getSearchTripTypes(String keyword, Integer pageNo, Integer pageSize) {
        StringBuilder builder = new StringBuilder(" SELECT * FROM trip_type WHERE 1=1 ");
        if(keyword!=null && !keyword.isEmpty()){
            builder.append(" AND unaccent(name) ILIKE unaccent(:keyword)");
        }
        Query queryTotal = entityManager.createNativeQuery(builder.toString().replace("SELECT *","SELECT COUNT(*)"), Long.class);
        builder.append(" ORDER BY id ASC");
        Query query = entityManager.createNativeQuery(builder.toString(), TripType.class);
        if(keyword!=null && !keyword.isEmpty()){
            query.setParameter("keyword", '%'+keyword+'%');
            queryTotal.setParameter("keyword", '%'+keyword+'%');
        }
        query.setFirstResult((pageNo-1)*pageSize).setMaxResults(pageSize);
        List<TripType> tripTypes = query.getResultList();
        Long total = ((Number)queryTotal.getSingleResult()).longValue();
        return PageResponseDto.<List<TripTypeDto>>builder()
                .pageSize(pageSize)
                .pageNo(pageNo)
                .total(total)
                .dataPage(tripTypes.stream().map(TripTypeConverter::toTripTypeDto).toList())
                .build();
    }
    private TripType getTripTypeEntityById(Integer id){
        return tripTypeRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("TripType","id",String.valueOf(id)));
    }
    @Override
    public TripTypeDto getTripTypeById(Integer id) {
        TripType tripType = getTripTypeEntityById(id);
        return TripTypeConverter.toTripTypeDto(tripType);
    }
    @Transactional
    @Override
    public TripTypeDto updateTripType(Integer id, TripTypeRequestDto tripTypeDto, MultipartFile file) {
        String imageIcon = uploadCloudinary.uploadCloudinary(file);
        TripType tripType = getTripTypeEntityById(id);
        tripType.setIcon(imageIcon);
        tripType.setName(tripTypeDto.getTripType());
        TripType updatedTripType = tripTypeRepository.save(tripType);
        return TripTypeConverter.toTripTypeDto(updatedTripType);
    }

    @Override
    public TripTypeDto createTripType(TripTypeRequestDto tripTypeDto, MultipartFile file) {
        TripType tripType = TripType.builder()
                .name(tripTypeDto.getTripType())
                .icon(uploadCloudinary.uploadCloudinary(file))
                .build();
        TripType createdTripType = tripTypeRepository.save(tripType);
        return TripTypeConverter.toTripTypeDto(createdTripType);
    }

}
