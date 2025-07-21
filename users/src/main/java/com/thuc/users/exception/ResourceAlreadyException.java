package com.thuc.users.exception;

public class ResourceAlreadyException extends RuntimeException {
    public ResourceAlreadyException(String resource,String field,String value) {
        super(String.format("Resource %s already exists with value %s", resource, field));
    }
}
