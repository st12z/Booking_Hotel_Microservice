package com.thuc.rooms.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, String field,String vale) {

        super(String.format("Resource %s not found for field %s with value %s", resource, field, vale));
    }
}
