package com.thuc.BookingService.exception;

public class ResourceAlreadyExistsException extends RuntimeException{
    public ResourceAlreadyExistsException(String resource,String field,String value) {
        super(String.format("Resource %s already exists with value %s", resource, field));
    }
}
