package com.thuc.rooms.service;

import com.thuc.rooms.dto.ReviewDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IReviewService {
    ReviewDto createReview(ReviewDto reviewDto, List<MultipartFile> images);

    String deleteReview(int id);

    List<ReviewDto> getReviewsByPropertyId(int propertyId);
}
