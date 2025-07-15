package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.CityConverter;
import com.thuc.rooms.dto.CityDto;
import com.thuc.rooms.dto.CityRequestDto;
import com.thuc.rooms.dto.PageResponseDto;
import com.thuc.rooms.entity.City;
import com.thuc.rooms.exception.ResourceNotFoundException;
import com.thuc.rooms.repository.CityRepository;
import com.thuc.rooms.service.ICityService;
import com.thuc.rooms.utils.UploadCloudinary;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements ICityService {
    private final CityRepository cityRepository;
    private final Logger log = LoggerFactory.getLogger(CityServiceImpl.class);
    private final UploadCloudinary uploadCloudinary;
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<CityDto> getAllCities() {
        log.debug("Request to get all Cities successfully");
        List<City> cities = cityRepository.findAll();
        return cities.stream().map(CityConverter::toCityDto).toList();
    }

    @Override
    public PageResponseDto<List<CityDto>> getAllCitiesPageResponse(Integer pageNo, Integer pageSize) {
        log.debug("Request to get all Cities successfully");
        Pageable pageable = PageRequest.of(pageNo-1, pageSize, Sort.by(Sort.Direction.ASC,"id"));
        Page<City> pageResult = cityRepository.findAll(pageable);
        List<City> cities = cityRepository.findAll(pageable).getContent();
        List<CityDto> cityDtos = cities.stream().map(CityConverter::toCityDto).toList();
        return PageResponseDto.<List<CityDto>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .total(pageResult.getTotalElements())
                .dataPage(cityDtos)
                .build();
    }

    @Override
    public PageResponseDto<List<CityDto>> getAllCitiesByKeyword(String keyword, Integer pageNo, Integer pageSize) {
        StringBuilder builder = new StringBuilder(" SELECT * FROM cities WHERE 1=1 ");
        if(keyword!= null && !keyword.isEmpty()){
            builder.append(" AND ( cast(id as TEXT) ILIKE :keyword");
            builder.append(" OR (unaccent(name) ILIKE :keyword) )");
        }
        Query query = entityManager.createNativeQuery(builder.toString(), City.class);
        Query queryTotal = entityManager.createNativeQuery(builder.toString().replace("SELECT *", "SELECT COUNT(*)"), Long.class);
        if(keyword!= null && !keyword.isEmpty()){
            query.setParameter("keyword",'%'+ keyword+'%');
            queryTotal.setParameter("keyword", '%'+ keyword + '%');
        }
        query.setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize);
        long total = ((Number) queryTotal.getSingleResult()).longValue();
        List<City> cities = (List<City>) query.getResultList();
        List<CityDto> cityDtos = cities.stream().map(CityConverter::toCityDto).toList();
        return PageResponseDto.<List<CityDto>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .total(total)
                .dataPage(cityDtos)
                .build();
    }

    @Override
    public CityDto getCityById(Integer id) {
        City city = cityRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("City","id",String.valueOf(id)));
        return CityConverter.toCityDto(city);
    }

    @Override
    public CityDto updateCity(Integer id, String name, MultipartFile image) {
        String imageCloudinary = uploadCloudinary.uploadCloudinary(image);
        City city = cityRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("City","id",String.valueOf(id)));
        city.setName(name);
        city.setImage(imageCloudinary);
        City savedCity = cityRepository.save(city);
        return CityConverter.toCityDto(savedCity);
    }
    public void updateGeog(Integer cityId, BigDecimal latitudeCenter, BigDecimal longitudeCenter){
        StringBuilder builder = new StringBuilder("UPDATE cities SET geog = ST_SetSRID(ST_MakePoint(:longitude_center, :latitude_center), 4326)\n" +
                "    WHERE id = :cityId");
        Map<String,Object> params = new HashMap<>();
        params.put("longitude_center", latitudeCenter);
        params.put("latitude_center", longitudeCenter);
        params.put("cityId", cityId);
        Query query = entityManager.createNativeQuery(builder.toString());
        params.forEach(query::setParameter);
        query.executeUpdate();
    }
    @Transactional
    @Override
    public CityDto createCity(CityRequestDto cityDto, MultipartFile image) {
        String imageCloudinary = uploadCloudinary.uploadCloudinary(image);
        City city = City.builder()
                .name(cityDto.getName())
                .image(imageCloudinary)
                .latitudeCenter(cityDto.getLatitudeCenter())
                .longitudeCenter(cityDto.getLongitudeCenter())
                .build();
        City savedCity = cityRepository.save(city);
        updateGeog(savedCity.getId(), savedCity.getLatitudeCenter(), savedCity.getLongitudeCenter());
        return CityConverter.toCityDto(savedCity);
    }
}
