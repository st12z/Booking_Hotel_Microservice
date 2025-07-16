package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.PropertyTypeConverter;
import com.thuc.rooms.converter.TripTypeConverter;
import com.thuc.rooms.dto.PageResponseDto;
import com.thuc.rooms.dto.PropertyTypeDto;
import com.thuc.rooms.dto.PropertyTypeRequestDto;
import com.thuc.rooms.dto.TripTypeDto;
import com.thuc.rooms.entity.PropertyType;
import com.thuc.rooms.entity.TripType;
import com.thuc.rooms.exception.ResourceAlreadyExistsException;
import com.thuc.rooms.exception.ResourceNotFoundException;
import com.thuc.rooms.repository.PropertyTypeRepository;
import com.thuc.rooms.service.IPropertyTypeService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyTypeServiceImpl implements IPropertyTypeService {
    private final PropertyTypeRepository propertyTypeRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<String> getAllPropertyTypes() {
        return propertyTypeRepository.findAll().stream().map(PropertyType::getName).collect(Collectors.toList());
    }

    @Override
    public PageResponseDto<List<PropertyTypeDto>> getAllPropertyTypesByPage(String keyword, Integer pageNo, Integer pageSize) {
        StringBuilder builder = new StringBuilder(" SELECT * FROM property_type WHERE 1=1 ");
        if(keyword!=null && !keyword.isEmpty()){
            builder.append(" AND unaccent(name) ILIKE unaccent(:keyword)");
        }
        Query queryTotal = entityManager.createNativeQuery(builder.toString().replace("SELECT *","SELECT COUNT(*)"), Long.class);
        builder.append(" ORDER BY id ASC");
        Query query = entityManager.createNativeQuery(builder.toString(), PropertyType.class);
        if(keyword!=null && !keyword.isEmpty()){
            query.setParameter("keyword", '%'+keyword+'%');
            queryTotal.setParameter("keyword", '%'+keyword+'%');
        }
        query.setFirstResult((pageNo-1)*pageSize).setMaxResults(pageSize);
        List<PropertyType> propertyTypes = query.getResultList();
        Long total = ((Number)queryTotal.getSingleResult()).longValue();
        return PageResponseDto.<List<PropertyTypeDto>>builder()
                .pageSize(pageSize)
                .pageNo(pageNo)
                .total(total)
                .dataPage(propertyTypes.stream().map(PropertyTypeConverter::toPropertyTypeDto).toList())
                .build();
    }
    private PropertyType getPropertyTypeEntityById(Integer id){
        return propertyTypeRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("PropertyType","id",String.valueOf(id)));
    }
    @Override
    public PropertyTypeDto getPropertyTypeById(Integer id) {
        PropertyType propertyType = getPropertyTypeEntityById(id);
        return PropertyTypeConverter.toPropertyTypeDto(propertyType);
    }
    @Transactional
    @Override
    public PropertyTypeDto updatePropertyType(Integer id, PropertyTypeRequestDto propertyTypeDto) {
        PropertyType propertyType = getPropertyTypeEntityById(id);
        propertyType.setName(propertyTypeDto.getName());
        PropertyType updatedPropertyType = propertyTypeRepository.save(propertyType);
        return PropertyTypeConverter.toPropertyTypeDto(updatedPropertyType);
    }

    @Override
    public PropertyTypeDto createPropertyType(PropertyTypeRequestDto propertyTypeDto) {
        PropertyType existProperty = propertyTypeRepository.findByName(propertyTypeDto.getName());
        if(existProperty != null){
            throw new ResourceAlreadyExistsException("PropertyType","name",propertyTypeDto.getName());
        }
        PropertyType propertyType = PropertyType.builder()
                .name(propertyTypeDto.getName())
                .build();
        PropertyType createPropertyType = propertyTypeRepository.save(propertyType);
        return PropertyTypeConverter.toPropertyTypeDto(createPropertyType);
    }
}
