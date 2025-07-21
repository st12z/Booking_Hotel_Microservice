package com.thuc.users.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource,String field,String value) {

        super(String.format("Resource %s with %s value %s not found", resource, field,value));
    }
}
