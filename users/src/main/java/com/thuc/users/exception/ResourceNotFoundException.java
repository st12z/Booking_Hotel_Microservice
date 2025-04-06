package com.thuc.users.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource,String field) {

        super(String.format("Resource %s with %s not found", resource, field));
    }
}
