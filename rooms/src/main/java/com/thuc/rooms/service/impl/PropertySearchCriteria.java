package com.thuc.rooms.service.impl;

import com.thuc.rooms.dto.FilterCriteria;
import com.thuc.rooms.dto.PropertyDto;
import com.thuc.rooms.entity.Property;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@AllArgsConstructor
@ToString
public class PropertySearchCriteria implements Consumer<FilterCriteria> {
    private CriteriaBuilder builder;
    private List<Predicate> predicates;
    private Root<Property> root;
    private final Logger logger = LoggerFactory.getLogger(PropertySearchCriteria.class);
    @Override
    public void accept(FilterCriteria param) {
        if(param.getOperation().equalsIgnoreCase("<")) {
            logger.debug("FilterCriteria param  <:{}",param.toString());
            predicates.add(builder.lessThan(root.get(param.getKey()),(Integer)param.getValue()));
        }
        if(param.getOperation().equalsIgnoreCase(">=")) {
            logger.debug("FilterCriteria param  >=:{}",param.toString());
            predicates.add(builder.greaterThanOrEqualTo(root.get(param.getKey()),(Integer)param.getValue()));
        }

        if(param.getOperation().equalsIgnoreCase("CONTAINS")) {
            List<String> paramFacilities = (List<String>) param.getValue();
            List<Predicate> containsPredicates = new ArrayList<>();

            for (String facility : paramFacilities) {
                containsPredicates.add(builder.equal(builder.function("jsonb_contains", Boolean.class, root.get("facilities"),
                                        builder.literal("[\"" + facility + "\"]")), true)
                );
            }

            predicates.add(builder.and(containsPredicates.toArray(new Predicate[0])));

        }
        if(param.getOperation().equalsIgnoreCase("IN") ) {
            if(param.getKey().equalsIgnoreCase("ratingStar"))  predicates.add(root.get(param.getKey()).in((List<Integer>)param.getValue()));
            else predicates.add(root.get(param.getKey()).in((List<String>)param.getValue()));
        }
    }
}
