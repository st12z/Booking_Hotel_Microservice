package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.PropertyConverter;
import com.thuc.rooms.dto.*;
import com.thuc.rooms.entity.Facilities;
import com.thuc.rooms.entity.Property;
import com.thuc.rooms.entity.RoomType;
import com.thuc.rooms.exception.ResourceNotFoundException;
import com.thuc.rooms.service.IFilterService;
import com.thuc.rooms.service.IPropertyService;
import com.thuc.rooms.service.IRedisPropertyService;
import com.thuc.rooms.service.ISearchService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
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
    private final ISearchService searchService;
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public PageResponseDto<List<PropertyDto>> filterByCondition(SearchDto searchDto, FilterDto filter,int pageNo,int pageSize) {
        log.debug("filtering by condition with search:{} and filter:{}", searchDto, filter);
        List<Integer> ids = getIdsPropertyInRedis(searchDto) !=null ? getIdsPropertyInRedis(searchDto) : null;
        if(ids==null){
            PageResponseDto<List<PropertyDto>> response = searchService.getPropertiesBySearchV1(pageNo,pageSize,searchDto);
            ids = response.getDataPage().stream().map(PropertyDto::getId).toList();
        }
        log.debug("ids:{}", ids);
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


    // get list
    private List<PropertyDto> getPropertyByCriteria(List<FilterCriteria> filterCriterias,FilterDto filter,int pageNo,int pageSize) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createTupleQuery();
        Root<Property> root = query.from(Property.class);
        List<Predicate> predicates = new ArrayList<>();
        PropertySearchCriteria propertySearchCriteria = new PropertySearchCriteria(builder,predicates,root);
        filterCriterias.forEach(propertySearchCriteria);
        log.debug("get property by condition:{}", propertySearchCriteria);
        Predicate predicate = builder.and(predicates.toArray(new Predicate[0]));
        Join<Property,RoomType> roomTypePropertyJoin=root.join("roomTypes",JoinType.INNER);
        Join<Property, Facilities> propertyFacilitiesJoin = root.join("facilities");
        if(filter.getBudget()!=null && filter.getBudget()!=0){
            Predicate predicatePrice = builder.lessThanOrEqualTo(roomTypePropertyJoin.get("price"),filter.getBudget());
            predicate = builder.and(predicate,predicatePrice);
        }

        if(filter.getQuantityBeds()!=null && filter.getQuantityBeds()!=0){
            Predicate predicateNumbeds = builder.equal(roomTypePropertyJoin.get("numBeds"),filter.getQuantityBeds());
            predicate = builder.and(predicate,predicateNumbeds);
        }
        if(filter.getFacilities()!=null && !filter.getFacilities().isEmpty()){
            List<Predicate> predicateFacilities = new ArrayList<>();
            for(String facility:filter.getFacilities()){
                predicateFacilities.add(builder.equal(propertyFacilitiesJoin.get("id"),facility));
            }
            Predicate predicateOr = builder.or(predicateFacilities.toArray(new Predicate[0]));
            predicate = builder.and(predicate,predicateOr);
        }
        if(filter.getSortCondition()!=null && !filter.getSortCondition().isEmpty()){
            String split[]=filter.getSortCondition().split("-");
            if(split.length>=2){
                String key= split[0];
                String order=split[1];
                Subquery<Integer> maxPriceSubquery = query.subquery(Integer.class);
                Root<RoomType> roomTypeRoot = maxPriceSubquery.from(RoomType.class);
                maxPriceSubquery.select(builder.max(roomTypeRoot.get("price")))
                        .where(builder.equal(roomTypeRoot.get("property"), root));

                if (key.equals("price")) {
                    query.multiselect(root,maxPriceSubquery.getSelection());
                    if(order.equals("asc")){
                        query.orderBy(builder.asc(maxPriceSubquery.getSelection()));
                    }
                    else{
                        query.orderBy(builder.desc(maxPriceSubquery.getSelection()));
                    }
                }
                else{
                    Path<?>sortPath=root.get(key);
                    if(order.equals("asc")){
                        query.orderBy(builder.asc(sortPath));
                    }
                    else{
                        query.orderBy(builder.desc(sortPath));
                    }
                }
            }

        }
        query.where(predicate).distinct(true);
        query.groupBy(root.get("id"));


        List<Tuple> tuples = entityManager.createQuery(query)
                .setFirstResult((pageNo-1)*pageSize)
                .setMaxResults(pageSize)
                .getResultList();

        return tuples.stream().map(tuple->{
            Property property = tuple.get(0,Property.class);
            return PropertyConverter.toPropertyDto(property);
        }).toList();
    }

    // total
    private Long getTotalElements(List<FilterCriteria> filterCriterias, FilterDto filter, int pageNo, int pageSize) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Property> root = query.from(Property.class);
        List<Predicate> predicates = new ArrayList<>();
        PropertySearchCriteria propertySearchCriteria = new PropertySearchCriteria(builder,predicates,root);
        filterCriterias.forEach(propertySearchCriteria);
        Predicate predicate = builder.and(predicates.toArray(new Predicate[0]));
        Join<Property,RoomType> roomTypePropertyJoin=root.join("roomTypes",JoinType.INNER);
        Join<Property, Facilities> propertyFacilitiesJoin = root.join("facilities");
        if(filter.getBudget()!=null && filter.getBudget()!=0){
            Predicate predicatePrice = builder.lessThanOrEqualTo(roomTypePropertyJoin.get("price"),filter.getBudget());
            predicate = builder.and(predicate,predicatePrice);
        }

        if(filter.getFacilities()!=null && !filter.getFacilities().isEmpty()){
            List<Predicate> predicateFacilities = new ArrayList<>();
            for(String facility:filter.getFacilities()){
                predicateFacilities.add(builder.equal(propertyFacilitiesJoin.get("id"),facility));
            }
            Predicate predicateOr = builder.or(predicateFacilities.toArray(new Predicate[0]));
            predicate = builder.and(predicate,predicateOr);
        }
        if(filter.getQuantityBeds()!=null && filter.getQuantityBeds()!=0){
            Predicate predicateNumbeds = builder.equal(roomTypePropertyJoin.get("numBeds"),filter.getQuantityBeds());
            predicate = builder.and(predicate,predicateNumbeds);
        }
        query.select(builder.countDistinct(root));
        query.where(predicate);
        return entityManager.createQuery(query).getSingleResult();
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
        if(filterDto.getRating()!=null && !filterDto.getRating().isEmpty()) filterCriteriaList.add(new FilterCriteria("ratingStar","IN",filterDto.getRating()));
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

        return filterCriteriaList;
    }
}