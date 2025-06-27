package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.PropertyConverter;
import com.thuc.rooms.dto.*;
import com.thuc.rooms.entity.*;
import com.thuc.rooms.exception.ResourceNotFoundException;
import com.thuc.rooms.repository.*;
import com.thuc.rooms.service.IPropertyService;
import com.thuc.rooms.service.IRedisPropertyService;
import com.thuc.rooms.service.client.BillsFeignClient;
import com.thuc.rooms.utils.UploadCloudinary;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements IPropertyService {
    private final PropertyRepository propertyRepository;
    private final CityRepository cityRepository;
    private final Logger log = LoggerFactory.getLogger(PropertyServiceImpl.class);
    private final IRedisPropertyService redisPropertyService;
    private final RoomTypeRepository roomTypeRepository;
    private final BillsFeignClient billsFeignClient;
    private final FacilitiesRepository facilitiesRepository;
    private final UploadCloudinary uploadCloudinary;
    private final PropertyImagesRepository propertyImagesRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<PropertyDto> getAllPropertiesBySlugCity(String slugCity) {
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

    @Override
    public PropertyDto getPropertyById(Integer id) {
        Property property = propertyRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Property","id",String.valueOf(id)));
        return PropertyConverter.toPropertyDto(property);
    }

    @Override
    public Integer getAmountProperties() {
        return (int)propertyRepository.count();
    }


    @Override
    public PageResponseDto<List<PropertyDto>> getPropertiesByFilter(FilterDtoManage filterDto) {
        int pageNo = filterDto.getPageNo();
        int pageSize = filterDto.getPageSize();
        String keyword =filterDto.getKeyword();
        int topBill = filterDto.getTopBill();
        int topRevenue = filterDto.getTopRevenue();
        int rateStar = filterDto.getRateStar();
        String propertyType = filterDto.getPropertyType();
        StringBuilder builder = new StringBuilder("SELECT * FROM properties p WHERE 1=1 ");
        if(rateStar!=0){
            builder.append(" AND rating_star =:rateStar ");
        }
        if(propertyType!=null && !propertyType.isEmpty()){
            builder.append(" AND p.property_type = :propertyType ");
        }
        if(keyword != null && !keyword.isEmpty()){
            builder.append(" AND (unaccent(lower(p.property_type)) ILIKE unaccent(:keyword) " +
                    " OR unaccent(lower(p.name)) ILIKE unaccent(:keyword) " +
                    " OR EXISTS(SELECT 1 FROM cities c WHERE p.city_id=c.id AND unaccent(c.name) ILIKE unaccent(:keyword))) ");

        }
        Query queryTotal = entityManager.createNativeQuery(builder.toString().replace("SELECT *", "SELECT COUNT(*)"),Long.class);
        builder.append(" ORDER BY p.id ");
        builder.append(" LIMIT :limit OFFSET :offset");
        Query query = entityManager.createNativeQuery(builder.toString(), Property.class);
        if(rateStar!=0){
            query.setParameter("rateStar", rateStar);
            queryTotal.setParameter("rateStar", rateStar);
        }
        if(propertyType!=null && !propertyType.isEmpty()){
            query.setParameter("propertyType", propertyType);
            queryTotal.setParameter("propertyType", propertyType);
        }
        if(keyword != null && !keyword.isEmpty()){
            query.setParameter("keyword",'%'+ keyword+'%');
            queryTotal.setParameter("keyword",'%'+ keyword+'%');
        }
        long total = ((Number) queryTotal.getSingleResult()).longValue();
        int limit = pageSize;
        int offset = (pageNo-1)*pageSize;
        query.setParameter("limit",limit);
        query.setParameter("offset",offset);
        List <Property> properties = query.getResultList();
        List<Integer> propertyIds = properties.stream().map(Property::getId).toList();
        Map<Integer,Integer> billMap = billsFeignClient.getBillByPropertyIds(propertyIds).getBody().getData();
        Map<Integer,Integer> revenueMap = billsFeignClient.getRevenueByPropertyIds(propertyIds).getBody().getData();
        List<PropertyDto> propertiesDtos = properties.stream().map(item->{
            int propertyId = item.getId();
            PropertyDto propertyDto = PropertyConverter.toPropertyDto(item);
            propertyDto.setTotalBills(billMap.get(propertyId));
            propertyDto.setTotalPayments(revenueMap.get(propertyId));
            return propertyDto;
        }).collect(Collectors.toList());
        if(topBill!=0 ){
            propertiesDtos.sort(Comparator.comparing(PropertyDto::getTotalBills).reversed());
            if(topBill<propertiesDtos.size()){
                propertiesDtos = propertiesDtos.subList(0, topBill);
            }
            if(topBill<=pageSize) total = (long)topBill;
        }
        if(topRevenue!=0 ){
            propertiesDtos.sort(Comparator.comparing(PropertyDto::getTotalPayments).reversed());
            if(topRevenue<propertiesDtos.size()){
                propertiesDtos = propertiesDtos.subList(0, topRevenue);
            }
            if(topRevenue<=pageSize) total = (long)topRevenue;
        }
        return PageResponseDto.<List<PropertyDto>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .dataPage(propertiesDtos)
                .total(total)
                .build();
    }

    @Override
    public PropertyDto updateProperty(PropertyDto propertyDto, List<MultipartFile> images) throws IOException {
        Property property = propertyRepository.findById(propertyDto.getId()).orElseThrow(()-> new ResourceNotFoundException("Property","id",String.valueOf(propertyDto.getId())));
        property.setName(propertyDto.getName());
        property.setAddress(propertyDto.getAddress());
        List<String> facilityNames = propertyDto.getFacilities();
        List<Facilities> facilitiesList = facilityNames.stream().map(name->{
           Facilities facilities = facilitiesRepository.findByName(name);
           if(facilities==null){
               throw new ResourceNotFoundException("Facilities","name",name);
           }
           return facilities;
        }).collect(Collectors.toList());
        property.setFacilities(facilitiesList);
        property.setSlug(propertyDto.getSlug());
        property.setOverview(propertyDto.getOverview());
        for (MultipartFile file : images) {
            System.out.println("File name: " + file.getOriginalFilename());
            System.out.println("Size: " + file.getSize());
            System.out.println("Hash: " + Arrays.hashCode(file.getBytes()));
        }
        List<String> imagesUpload = images.stream()
                .map(f -> {
                    String url = uploadCloudinary.uploadCloudinary(f);
                    System.out.println("Uploaded: " + f.getOriginalFilename() + " => " + url);
                    return url;
                })
                .toList();
        propertyImagesRepository.deleteByPropertyId(propertyDto.getId());
        imagesUpload.forEach(image->{
            PropertyImages propertyImages = PropertyImages.builder()
                    .image(image)
                    .property(property)
                    .build();
            propertyImagesRepository.save(propertyImages);
        });
        Property savedProperty = propertyRepository.save(property);
        return PropertyConverter.toPropertyDto(savedProperty);
    }

    @Override
    public List<PropertyDto> getAllProperties() {
        List<Property> properties = propertyRepository.findAll();
        return properties.stream().map(PropertyConverter::toPropertyDto).toList();
    }


}