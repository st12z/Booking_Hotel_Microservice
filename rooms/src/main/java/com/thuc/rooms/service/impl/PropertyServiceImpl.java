package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.PropertyConverter;
import com.thuc.rooms.dto.*;
import com.thuc.rooms.entity.*;
import com.thuc.rooms.exception.ResourceAlreadyExistsException;
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
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
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
    public PageResponseDto<List<PropertyDto>> getPropertiesByFilter(FilterPropertiesManageDto filterDto) {
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

    @Transactional
    @Override
    public PropertyDto updateProperty(PropertyDto propertyDto, List<MultipartFile> images) throws IOException {
        Property property = propertyRepository.findById(propertyDto.getId()).orElseThrow(()-> new ResourceNotFoundException("Property","id",String.valueOf(propertyDto.getId())));
        property.setName(propertyDto.getName());
        property.setAddress(propertyDto.getAddress());
        List<String> facilityNames = propertyDto.getFacilities();
        List<Facilities> oldFacilities = property.getFacilities();
        List<Facilities> facilitiesList = facilityNames.stream().map(name->{
           Facilities facilities = facilitiesRepository.findByName(name);
           if(facilities==null){
               throw new ResourceNotFoundException("Facilities","name",name);
           }
           return facilities;
        }).collect(Collectors.toList());
        property.getFacilities().clear();
        property.setSlug(propertyDto.getSlug());
        property.setFacilities(facilitiesList);
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
        for(Facilities facilities : oldFacilities){
            facilities.getProperties().remove(property);
            facilitiesRepository.save(facilities);
        }
        for(Facilities facilities: facilitiesList){
            facilities.getProperties().add(savedProperty);
            facilitiesRepository.save(facilities);
        }
        return PropertyConverter.toPropertyDto(savedProperty);
    }

    @Override
    public List<PropertyDto> getAllProperties() {
        List<Property> properties = propertyRepository.findAll();
        return properties.stream().map(PropertyConverter::toPropertyDto).toList();
    }

    public void updateDistanceFromCenter(Integer propertyId){
        StringBuilder builder = new StringBuilder("UPDATE properties SET distance_from_center= ST_Distance(properties.geog, cities.geog)" +
                "FROM cities WHERE properties.city_id = cities.id AND properties.id=:propertyId");
        entityManager.createNativeQuery(builder.toString()).setParameter("propertyId", propertyId).executeUpdate();
    }

    public void updateGeog(Integer propertyId, BigDecimal latitude, BigDecimal longitude){
        StringBuilder builder = new StringBuilder("UPDATE properties SET geog = ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)\n" +
                "    WHERE id = :propertyId");
        Map<String,Object> params = new HashMap<>();
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        params.put("propertyId", propertyId);
        Query query = entityManager.createNativeQuery(builder.toString());
        params.forEach(query::setParameter);
        query.executeUpdate();
    }
    @Transactional
    @Override
    public PropertyDto createProperty(PropertyRequestDto propertyDto, List<MultipartFile> images) {
        Property existProperty = propertyRepository.findByName(propertyDto.getName());
        if(existProperty!=null){
            throw new ResourceAlreadyExistsException("Property","name",propertyDto.getName());
        }
        List<String> imagesUpload = images.stream().map(uploadCloudinary::uploadCloudinary).toList();
        City city = cityRepository.findById(propertyDto.getCityId()).
                orElseThrow(()-> new ResourceNotFoundException("City","id",String.valueOf(propertyDto.getCityId())));
        List<Facilities> facilities = propertyDto.getFacilities().stream().map(id->{
            Facilities facility = facilitiesRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Facilities","id",String.valueOf(id)));
            return facility;
        }).toList();
        Property property = Property.builder()
                .name(propertyDto.getName())
                .address(propertyDto.getAddress())
                .city(city)
                .overview(propertyDto.getOverview())
                .propertyType(propertyDto.getPropertyType())
                .latitude(propertyDto.getLatitude())
                .longitude(propertyDto.getLongitude())
                .build();
        Property savedProperty = propertyRepository.save(property);
        propertyRepository.flush();
        for(Facilities facility: facilities){
            facility.getProperties().add(savedProperty);
            facilitiesRepository.save(facility);
        }
        List<PropertyImages> propertyImages = imagesUpload.stream().map(image->{
            PropertyImages propertyImage = PropertyImages.builder()
                    .image(image)
                    .property(property)
                    .build();
            PropertyImages savedPropertyImages= propertyImagesRepository.save(propertyImage);
            return  savedPropertyImages;
        }).toList();
        savedProperty.setPropertyImages(propertyImages);
        savedProperty.setFacilities(facilities);
        updateGeog(savedProperty.getId(),savedProperty.getLatitude(),savedProperty.getLongitude());
        updateDistanceFromCenter(savedProperty.getId());
        Property savedProperty2 = propertyRepository.findById(savedProperty.getId()).orElseThrow(()-> new ResourceNotFoundException("Property","id",String.valueOf(savedProperty.getId())));
        return PropertyConverter.toPropertyDto(savedProperty2);
    }


}