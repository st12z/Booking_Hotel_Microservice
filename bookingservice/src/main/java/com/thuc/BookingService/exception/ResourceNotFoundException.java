package com.thuc.BookingService.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, String field,String value) {

        super(String.format("Resource %s not found for field %s with value %s", resource, field, value));
    }
}
