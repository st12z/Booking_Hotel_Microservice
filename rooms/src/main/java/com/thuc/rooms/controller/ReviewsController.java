package com.thuc.rooms.controller;

import com.thuc.rooms.constants.ReviewConstant;
import com.thuc.rooms.dto.ReviewDto;
import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.service.IReviewService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/api/reviews")
@RestController
@RequiredArgsConstructor
public class ReviewsController {
    private final Logger log = LoggerFactory.getLogger(ReviewsController.class);
    private final IReviewService reviewService;
    @PostMapping("")
    public ResponseEntity<SuccessResponseDto<ReviewDto>> createReview(@RequestBody ReviewDto reviewDto) {
        log.debug("createReview with reviewDto: {}", reviewDto);
        SuccessResponseDto<ReviewDto> responseDto = SuccessResponseDto.<ReviewDto>builder()
                .code(ReviewConstant.STATUS_200)
                .message(ReviewConstant.MESSAGE_200)
                .data(reviewService.createReview(reviewDto))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
