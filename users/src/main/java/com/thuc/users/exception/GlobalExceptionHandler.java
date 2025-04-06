package com.thuc.users.exception;

import com.thuc.users.dto.responseDto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.MethodNotAllowedException;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler  {
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException e, WebRequest request) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setCode(HttpStatus.NOT_FOUND.value());
        errorResponseDto.setError(e.getMessage());
        errorResponseDto.setTime(LocalDateTime.now());
        errorResponseDto.setPath(request.getDescription(false).replace("uri=",""));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }
    @ExceptionHandler(ResourceAlreadyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponseDto> handleResourceAlreadyException(ResourceAlreadyException e, WebRequest request) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setCode(HttpStatus.CONFLICT.value());
        errorResponseDto.setError(e.getMessage());
        errorResponseDto.setTime(LocalDateTime.now());
        errorResponseDto.setPath(request.getDescription(false).replace("uri=",""));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDto);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException e, WebRequest request) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setCode(HttpStatus.BAD_REQUEST.value());
        errorResponseDto.setError(e.getMessage());
        errorResponseDto.setTime(LocalDateTime.now());
        errorResponseDto.setPath(request.getDescription(false).replace("uri=",""));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentTypeMismatchException e, WebRequest request) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setCode(HttpStatus.BAD_REQUEST.value());
        errorResponseDto.setError(e.getMessage());
        errorResponseDto.setTime(LocalDateTime.now());
        errorResponseDto.setPath(request.getDescription(false).replace("uri=",""));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponseDto> handleException(Exception e, WebRequest request) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponseDto.setError(e.getMessage());
        errorResponseDto.setTime(LocalDateTime.now());
        errorResponseDto.setPath(request.getDescription(false).replace("uri=",""));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto);
    }
    @ExceptionHandler(MethodNotAllowedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<ErrorResponseDto> handleMethodNotAllowedException(MethodNotAllowedException e, WebRequest request) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponseDto.setError(e.getMessage());
        errorResponseDto.setTime(LocalDateTime.now());
        errorResponseDto.setPath(request.getDescription(false).replace("uri=",""));
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponseDto);
    }
}
