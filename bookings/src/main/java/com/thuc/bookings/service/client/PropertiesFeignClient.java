package com.thuc.bookings.service.client;

import com.thuc.bookings.dto.responseDto.PropertyDto;
import com.thuc.bookings.dto.responseDto.SuccessResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="rooms", contextId = "propertiesClient",path = "/api/properties")
public interface PropertiesFeignClient {
    @GetMapping("/id/{id}")
    public ResponseEntity<SuccessResponseDto<PropertyDto>> getPropertyId(@PathVariable Integer id) ;
}
