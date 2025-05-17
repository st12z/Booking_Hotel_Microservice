package com.thuc.rooms.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thuc.rooms.constants.ReviewConstant;
import com.thuc.rooms.dto.ReviewDto;
import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.service.IReviewService;
import com.thuc.rooms.utils.UploadCloudinary;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@RequestMapping("/api/reviews")
@RestController
@RequiredArgsConstructor
public class ReviewsController {
    private final UploadCloudinary uploadCloudinary ;
    private final Logger log = LoggerFactory.getLogger(ReviewsController.class);
    private final IReviewService reviewService;
    @PostMapping(value = "",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponseDto<ReviewDto>> createReview(@RequestPart(name = "reviewDto") String reviewDto,
                                                                      @RequestPart(name = "images", required = false) List<MultipartFile> images) throws JsonProcessingException {
        log.debug("createReview with reviewDto: {}", reviewDto);
        ObjectMapper objectMapper = new ObjectMapper();
        ReviewDto review = objectMapper.readValue(reviewDto, ReviewDto.class);
        log.debug("createReview with reviewDto: {}", review);
        SuccessResponseDto<ReviewDto> response = SuccessResponseDto.<ReviewDto>builder()
                .code(ReviewConstant.STATUS_201)
                .message(ReviewConstant.MESSAGE_200)
                .data(reviewService.createReview(review,images))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/delete/{id}")
    public ResponseEntity<SuccessResponseDto<String>> deleteReview(@PathVariable int id) {
        log.debug("deleteReview with id: {}", id);
        SuccessResponseDto<String> response = SuccessResponseDto.<String>builder()
                .code(ReviewConstant.STATUS_200)
                .message(ReviewConstant.MESSAGE_200)
                .data(reviewService.deleteReview(id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("")
    public ResponseEntity<SuccessResponseDto<List<ReviewDto>>> getReviewsByPropertyId(@RequestParam int propertyId) {
        log.debug("getReviewsByPropertyId: {}", propertyId);
        SuccessResponseDto<List<ReviewDto>> response = SuccessResponseDto.<List<ReviewDto>>builder()
                .code(ReviewConstant.STATUS_200)
                .message(ReviewConstant.MESSAGE_200)
                .data(reviewService.getReviewsByPropertyId(propertyId))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/amount-reviews")
    public ResponseEntity<SuccessResponseDto<Integer>> amountReviews() {
        log.debug("amountReviews");
        SuccessResponseDto<Integer> response = SuccessResponseDto.<Integer>builder()
                .code(ReviewConstant.STATUS_200)
                .message(ReviewConstant.MESSAGE_200)
                .data(reviewService.getAmountReviews())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
