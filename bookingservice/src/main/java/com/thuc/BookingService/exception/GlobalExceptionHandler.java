package com.thuc.rooms.exception;

import com.thuc.rooms.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponseDto> handleException(ResourceNotFoundException e, WebRequest request) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setCode(HttpStatus.NOT_FOUND.value());
        errorResponseDto.setError(e.getMessage());
        errorResponseDto.setPath(request.getDescription(false).replace("uri=",""));
        errorResponseDto.setTime(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException e, WebRequest request) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setCode(HttpStatus.NOT_FOUND.value());
        errorResponseDto.setError(e.getMessage());
        errorResponseDto.setPath(request.getDescription(false).replace("uri=",""));
        errorResponseDto.setTime(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponseDto> handleResouseAlreadyExistException(ResourceNotFoundException e, WebRequest request) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setCode(HttpStatus.NOT_FOUND.value());
        errorResponseDto.setError(e.getMessage());
        errorResponseDto.setPath(request.getDescription(false).replace("uri=",""));
        errorResponseDto.setTime(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }
}
