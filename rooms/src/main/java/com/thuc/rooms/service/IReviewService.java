package com.thuc.rooms.service;

import com.thuc.rooms.dto.FilterReviewDto;
import com.thuc.rooms.dto.PageResponseDto;
import com.thuc.rooms.dto.ReviewDto;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

public interface IReviewService {
    ReviewDto createReview(ReviewDto reviewDto, List<MultipartFile> images);

    String deleteReview(int id);

    List<ReviewDto> getReviewsByPropertyId(int propertyId);

    Integer getAmountReviews();

    PageResponseDto<List<ReviewDto>> getAllReviews(FilterReviewDto filterDto) throws ParseException;

    PageResponseDto<List<ReviewDto>> getSearchReviews(String keyword, Integer pageNo, Integer pageSize);
}
