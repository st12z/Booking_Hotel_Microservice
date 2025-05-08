package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.ReviewConverter;
import com.thuc.rooms.dto.ReviewDto;
import com.thuc.rooms.entity.Review;
import com.thuc.rooms.repository.ReviewRepository;
import com.thuc.rooms.service.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements IReviewService {
    private final ReviewRepository reviewRepository;
    @Override
    public ReviewDto createReview(ReviewDto reviewDto) {
        Review review = ReviewConverter.toReview(reviewDto);
        review = reviewRepository.save(review);
        return ReviewConverter.toReviewDto(review);
    }
}
