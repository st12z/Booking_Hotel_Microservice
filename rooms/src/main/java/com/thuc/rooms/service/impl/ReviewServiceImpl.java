package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.ReviewConverter;
import com.thuc.rooms.dto.RatingDto;
import com.thuc.rooms.dto.ReviewDto;
import com.thuc.rooms.entity.Property;
import com.thuc.rooms.entity.Review;
import com.thuc.rooms.exception.ResourceNotFoundException;
import com.thuc.rooms.repository.PropertyRepository;
import com.thuc.rooms.repository.ReviewRepository;
import com.thuc.rooms.service.IReviewService;
import com.thuc.rooms.utils.CaculateRating;
import com.thuc.rooms.utils.UploadCloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements IReviewService {
    private final ReviewRepository reviewRepository;
    private final UploadCloudinary uploadCloudinary;
    private final PropertyRepository propertyRepository;
    @Override
    public ReviewDto createReview(ReviewDto reviewDto, List<MultipartFile> images) {

        Property property =  propertyRepository.findById(reviewDto.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("Property","id",String.valueOf(reviewDto.getPropertyId())));
        Review review = ReviewConverter.toReview(reviewDto);
        review.setProperty(property);
        if(images!=null&& !images.isEmpty()) {
            List<CompletableFuture<String>> futures = images.stream()
                    .map(image -> CompletableFuture.supplyAsync(() -> uploadCloudinary.uploadCloudinary(image)))
                    .toList();

            // Đợi tất cả ảnh upload hoàn thành và lấy kết quả
            List<String> uploadImages = futures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());
            review.setImages(uploadImages);
        }
        review = reviewRepository.save(review);
        RatingDto ratingDto = CaculateRating.caculateRating(property);
        property.setAvgReviewScore(ratingDto.getAvgReviewScore());
        property.setRatingStar(ratingDto.getRatingStar());
        propertyRepository.save(property);
        return ReviewConverter.toReviewDto(review);
    }

    @Override
    public String deleteReview(int id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reviews","Id",String.valueOf(id)));
        reviewRepository.delete(review);
        return String.format("review deleted with id: %d", id);
    }

    @Override
    public List<ReviewDto> getReviewsByPropertyId(int propertyId) {
        List<Review> reviews = reviewRepository.findByPropertyId(propertyId);
        return reviews.stream().map(ReviewConverter::toReviewDto).sorted(Comparator.comparing(ReviewDto::getCreatedAt).reversed()).collect(Collectors.toList());
    }

    @Override
    public Integer getAmountReviews() {
        return (int) reviewRepository.count();
    }


}