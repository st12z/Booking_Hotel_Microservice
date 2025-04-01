package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.PropertyConverter;
import com.thuc.rooms.dto.*;
import com.thuc.rooms.entity.Property;
import com.thuc.rooms.entity.RoomType;
import com.thuc.rooms.exception.ResourceNotFoundException;
import com.thuc.rooms.service.IFilterService;
import com.thuc.rooms.service.IRedisPropertyService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class FilterServiceImpl implements IFilterService {
    private final IRedisPropertyService redisService;
    private final Logger log = LoggerFactory.getLogger(FilterServiceImpl.class);
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public PageResponseDto<List<PropertyDto>> filterByCondition(SearchDto searchDto, FilterDto filter,int pageNo,int pageSize) {
        log.debug("filtering by condition with search:{} and filter:{}", searchDto, filter);
        List<Integer> ids = getIdsPropertyInRedis(searchDto) !=null ? getIdsPropertyInRedis(searchDto) : null;
        if(ids!=null){
            List<FilterCriteria> filterCriterias = getFilterCriteria(filter,ids);
            List<PropertyDto> propertyDtos=getPropertyByCriteria(filterCriterias,filter,pageNo,pageSize);
            Long total = getTotalElements(filterCriterias,filter,pageNo,pageSize);
            return PageResponseDto.<List<PropertyDto>>builder()
                    .dataPage(propertyDtos)
                    .total(total)
                    .pageSize(pageSize)
                    .pageNo(pageNo)
                    .build();
        }
        throw new RuntimeException("please search before filter");

    }


    // get list
    private List<PropertyDto> getPropertyByCriteria(List<FilterCriteria> filterCriterias,FilterDto filter,int pageNo,int pageSize) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Property> query = builder.createQuery(Property.class);
        Root<Property> root = query.from(Property.class);
        List<Predicate> predicates = new ArrayList<>();
        PropertySearchCriteria propertySearchCriteria = new PropertySearchCriteria(builder,predicates,root);
        filterCriterias.forEach(propertySearchCriteria);
        log.debug("get property by condition:{}", propertySearchCriteria);
        Predicate predicate = builder.and(predicates.toArray(new Predicate[0]));
        if(filter.getBudget()!=null){
            Join<Property, RoomType> propertyRoomTypeJoin = root.join("roomTypes", JoinType.INNER);
            Predicate predicateJoin = builder.lessThanOrEqualTo(propertyRoomTypeJoin.get("price"),filter.getBudget());
            predicate = builder.and(predicate,predicateJoin);
        }
        query.where(predicate);
        List<Property> properties = entityManager.createQuery(query)
                .setFirstResult((pageNo-1)*pageSize)
                .setMaxResults(pageSize)
                .getResultList();

        return properties.stream().map(PropertyConverter::toPropertyDto).toList();
    }

    // total
    private Long getTotalElements(List<FilterCriteria> filterCriterias, FilterDto filter, int pageNo, int pageSize) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Property> query = builder.createQuery(Property.class);
        Root<Property> root = query.from(Property.class);
        List<Predicate> predicates = new ArrayList<>();
        PropertySearchCriteria propertySearchCriteria = new PropertySearchCriteria(builder,predicates,root);
        filterCriterias.forEach(propertySearchCriteria);
        Predicate predicate = builder.and(predicates.toArray(new Predicate[0]));
        if(filter.getBudget()!=null){
            Join<RoomType,Property> roomTypePropertyJoin=root.join("roomTypes",JoinType.INNER);
            Predicate predicateJoin = builder.lessThanOrEqualTo(roomTypePropertyJoin.get("price"),filter.getBudget());
            predicate = builder.and(predicate,predicateJoin);
        }
        query.where(predicate);
        return (long) entityManager.createQuery(query)
                .getResultList()
                .size();
    }
    // get ids in redis
    private List<Integer> getIdsPropertyInRedis(SearchDto searchDto){
        log.debug("get ids property in redis:{}", searchDto);
        String key = String.format("search:%s", searchDto);
        if(redisService.getData(key)!=null) {
            List<PropertyDto> propertyDtosInRedis = redisService.getData(key);
            List<Integer> ids = propertyDtosInRedis.stream().map(PropertyDto::getId).toList();

            return ids;
        }
        return null;
    }

    // create filtercriteria
    private List<FilterCriteria> getFilterCriteria(FilterDto filterDto,List<Integer> ids){
        List<FilterCriteria> filterCriteriaList = new ArrayList<>();
        if(ids!=null && ids.size()>0){
            filterCriteriaList.add(new FilterCriteria("id","IN",ids));
        }
        if(filterDto.getRating()!=null) filterCriteriaList.add(new FilterCriteria("ratingStar","IN",filterDto.getRating()));
        if(filterDto.getPropertyType()!=null && !filterDto.getPropertyType().isEmpty())filterCriteriaList.add(new FilterCriteria("propertyType","IN",filterDto.getPropertyType()));
        if(filterDto.getDistance() !=null && !filterDto.getDistance().isEmpty()){
            for(String distance:filterDto.getDistance()){
                filterCriteriaList.add(new FilterCriteria("distanceFromCenter","<",Integer.valueOf(distance.charAt(1)+"")));
            }
        }
        if(filterDto.getReviewScore()!=null && !filterDto.getReviewScore().isEmpty()){
            for(String reviewScore:filterDto.getReviewScore()){
                if(reviewScore.length()>2){
                    String[] split = reviewScore.split("-");
                    filterCriteriaList.add(new FilterCriteria("avgReviewScore",">=",Integer.valueOf(split[0])));
                    filterCriteriaList.add(new FilterCriteria("avgReviewScore","<",Integer.valueOf(split[1])));
                }
                else{
                    filterCriteriaList.add(new FilterCriteria("avgReviewScore","<",Integer.valueOf(reviewScore.charAt(1)+"")));
                }
            }
        }
        if(filterDto.getFacilities() !=null && !filterDto.getFacilities().isEmpty()){
            filterCriteriaList.add(new FilterCriteria("facilities","CONTAINS",filterDto.getFacilities()));
        }
        return filterCriteriaList;
    }
}
