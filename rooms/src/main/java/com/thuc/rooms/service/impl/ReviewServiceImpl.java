package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.ReviewConverter;
import com.thuc.rooms.dto.FilterReviewDto;
import com.thuc.rooms.dto.PageResponseDto;
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
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements IReviewService {
    private final ReviewRepository reviewRepository;
    private final UploadCloudinary uploadCloudinary;
    private final PropertyRepository propertyRepository;
    @PersistenceContext
    private EntityManager entityManager;
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

    @Override
    public PageResponseDto<List<ReviewDto>> getAllReviews(FilterReviewDto filterDto) throws ParseException {
        StringBuilder builder = new StringBuilder(" SELECT * FROM reviews WHERE 1=1");
        Map<String,Object> params = new HashMap<>();
        int pageNo = filterDto.getPageNo();
        int pageSize = filterDto.getPageSize();
        if(filterDto.getPropertyId()!=null && filterDto.getPropertyId()!=0) {
            builder.append(" AND property_id=:propertyId");
            params.put("propertyId",filterDto.getPropertyId());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(!filterDto.getTimeOption().equals("0")){
            builder.append(" AND created_at >=:beginDate AND created_at <=:endDate");
            LocalDate now = LocalDate.now();
            switch (filterDto.getTimeOption()) {
                case "custom":{
                    Date beginDate = sdf.parse(filterDto.getBeginDate());
                    Date endDate = sdf.parse(filterDto.getEndDate());
                    params.put("beginDate",beginDate);
                    params.put("endDate",endDate);
                    break;
                }
                case "today": {
                    LocalDate today = LocalDate.now();
                    params.put("beginDate", today.atStartOfDay());
                    params.put("endDate", today.plusDays(1).atStartOfDay());
                    break;
                }
                case "yesterday": {
                    LocalDate yesterday = LocalDate.now().minusDays(1);
                    params.put("beginDate", yesterday.atStartOfDay());
                    params.put("endDate", yesterday.plusDays(1).atStartOfDay());
                    break;
                }
                case "last_7_days": {
                    params.put("beginDate", now.minusDays(7).atTime(LocalTime.MIN));
                    params.put("endDate", now.atTime(LocalTime.MAX));
                    break;
                }
                case "last_30_days": {
                    params.put("beginDate", now.minusDays(30).atTime(LocalTime.MIN));
                    params.put("endDate", now.atTime(LocalTime.MAX));
                    break;
                }
                case "this_week": {
                    LocalDate today = LocalDate.now();
                    LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
                    LocalDate endOfWeek = startOfWeek.plusDays(7);
                    params.put("beginDate", startOfWeek.atStartOfDay());
                    params.put("endDate", endOfWeek.atStartOfDay());
                    break;
                }
                case "this_month": {
                    LocalDate today = LocalDate.now();
                    LocalDate firstDay = today.withDayOfMonth(1);
                    LocalDate firstOfNextMonth = firstDay.plusMonths(1);
                    params.put("beginDate", firstDay.atStartOfDay());
                    params.put("endDate", firstOfNextMonth.atStartOfDay());
                    break;
                }
                case "this_year": {
                    LocalDate today = LocalDate.now();
                    LocalDate firstDay = today.withDayOfYear(1);
                    LocalDate firstOfNextYear = firstDay.plusYears(1);
                    params.put("beginDate", firstDay.atStartOfDay());
                    params.put("endDate", firstOfNextYear.atStartOfDay());
                    break;
                }
            }
        }
        Query queryTotal = entityManager.createNativeQuery(builder.toString().replace("SELECT *","SELECT COUNT(*)"),Long.class);
        if(!filterDto.getSortOption().equals("0")){
            switch (filterDto.getSortOption()) {
                case "date_desc":
                    builder.append(" ORDER BY created_at DESC, id ASC ");
                    break;
                case "date_asc":
                    builder.append(" ORDER BY created_at ASC, id ASC ");
                    break;
                default:
                    break;
            }
        }
        else{
            builder.append(" ORDER BY id ASC ");
        }
        Query query = entityManager.createNativeQuery(builder.toString(),Review.class);
        params.forEach(query::setParameter);
        params.forEach(queryTotal::setParameter);
        query.setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize);
        List<Review> reviews = query.getResultList();
        long total = ((Number) queryTotal.getSingleResult()).longValue();
        return PageResponseDto.<List<ReviewDto>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .total(total)
                .dataPage(reviews.stream().map(ReviewConverter::toReviewDto).toList())
                .build();
    }

    @Override
    public PageResponseDto<List<ReviewDto>> getSearchReviews(String keyword, Integer pageNo, Integer pageSize) {
        StringBuilder builder = new StringBuilder(" SELECT * FROM reviews WHERE 1=1 ");
        if (keyword != null && !keyword.isEmpty()) {
            builder.append(" AND (unaccent(content) ILIKE unaccent(:keyword)");
            builder.append(" OR unaccent(CAST(id AS TEXT)) ILIKE unaccent(:keyword)");
            builder.append(" OR unaccent(CAST(user_id AS TEXT)) ILIKE unaccent(:keyword)");
            builder.append(" OR unaccent(CAST(property_id AS TEXT)) ILIKE unaccent(:keyword))");
        }
        Query queryTotal = entityManager.createNativeQuery(builder.toString().replace("SELECT *","SELECT COUNT(*)"),Long.class);
        builder.append(" ORDER BY id ASC");
        Query query = entityManager.createNativeQuery(builder.toString(),Review.class);
        if(keyword != null && !keyword.isEmpty()){
            query.setParameter("keyword",'%'+ keyword+'%');
            queryTotal.setParameter("keyword", '%'+keyword+'%');
        }
        List<Review> reviews = query.getResultList();
        long total = ((Number) queryTotal.getSingleResult()).longValue();
        return PageResponseDto.<List<ReviewDto>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .total(total)
                .dataPage(reviews.stream().map(ReviewConverter::toReviewDto).toList())
                .build();
    }


}