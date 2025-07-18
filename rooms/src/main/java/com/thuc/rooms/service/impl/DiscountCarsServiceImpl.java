package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.DiscountCarsConverter;
import com.thuc.rooms.converter.DiscountConverter;
import com.thuc.rooms.dto.*;
import com.thuc.rooms.entity.Discount;
import com.thuc.rooms.entity.DiscountCars;
import com.thuc.rooms.entity.UserDiscount;
import com.thuc.rooms.entity.UserDiscountCars;
import com.thuc.rooms.exception.ResourceAlreadyExistsException;
import com.thuc.rooms.exception.ResourceNotFoundException;
import com.thuc.rooms.repository.DiscountCarsRepository;
import com.thuc.rooms.repository.UserDiscountCarsRepository;
import com.thuc.rooms.service.IDiscountCarsService;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DiscountCarsServiceImpl implements IDiscountCarsService {
    private final DiscountCarsRepository discountCarsRepository;
    private final UserDiscountCarsRepository userDiscountCarsRepository;
    private final UploadCloudinary uploadCloudinary;
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<DiscountCarsDto> getAllDiscountCars() {
        List<DiscountCars> discountCars = discountCarsRepository.findActiveDiscount();
        return discountCars.stream().map(DiscountCarsConverter::toDiscountCarsDto).toList();
    }

    @Override
    public List<DiscountCarsDto> getAllMyDiscounts(String email) {
        List<UserDiscountCars> userDiscountCars = userDiscountCarsRepository.findByEmail(email);
        List<DiscountCarsDto> result = userDiscountCars.stream().map(userDiscountCar ->{
           DiscountCars discountCars = discountCarsRepository.findById(userDiscountCar.getDiscountCarId())
                   .orElseThrow(()-> new ResourceNotFoundException("DiscountCars","id",String.valueOf(userDiscountCar.getDiscountCarId())));
           return DiscountCarsConverter.toDiscountCarsDto(discountCars);
        }).toList();
        return result;
    }

    @Override
    public UserDiscountCarDto saveDiscount(UserDiscountCarDto userDiscountCarDto) {
        UserDiscountCars existDiscountCars = userDiscountCarsRepository.findByDiscountCarIdAndEmail(userDiscountCarDto.getDiscountCarId(), userDiscountCarDto.getEmail());
        if(existDiscountCars != null) {
            throw new ResourceAlreadyExistsException("DiscountCars","discountCarId-Email",String.format("%d-%s",userDiscountCarDto.getDiscountCarId(),userDiscountCarDto.getEmail()));
        }
        UserDiscountCars userDiscountCars = UserDiscountCars.builder()
                .discountCarId(userDiscountCarDto.getDiscountCarId())
                .email(userDiscountCarDto.getEmail())
                .build();
        userDiscountCarsRepository.save(userDiscountCars);
        return userDiscountCarDto;
    }
    private DiscountCars getDiscountCarById(Integer discountCarId) {
        return discountCarsRepository.findById(discountCarId).orElseThrow(()-> new ResourceNotFoundException("Discount","id",String.valueOf(discountCarId)));
    }
    @Override
    public PageResponseDto<List<DiscountCarsDto>> getMyDiscountsByEmailPage(String email, Integer pageNo, Integer pageSize) {
        StringBuilder builder = new StringBuilder(" SELECT * from user_discount_cars u where u.email=:email AND EXISTS (SELECT 1 FROM discount_cars d WHERE u.discount_car_id = d.id AND d.start_date <= now() AND d.end_date>=now())");
        Query query = entityManager.createNativeQuery(builder.toString(), UserDiscountCars.class);
        Query queryTotal = entityManager.createNativeQuery(builder.toString().replace("SELECT *","SELECT COUNT(*)"),Long.class);
        query.setParameter("email",email);
        queryTotal.setParameter("email",email);
        query.setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize);
        List<UserDiscountCars> userDiscounts = query.getResultList();
        List<DiscountCars> discounts = userDiscounts.stream().map(userDiscount -> {
            int discountId= userDiscount.getDiscountCarId();
            return getDiscountCarById(discountId);
        }).toList();
        long total = ((Number) queryTotal.getSingleResult()).longValue();
        return PageResponseDto.<List<DiscountCarsDto>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .total(total)
                .dataPage(discounts.stream().map(DiscountCarsConverter::toDiscountCarsDto).toList())
                .build();
    }

    @Override
    public PageResponseDto<List<DiscountCarsDto>> getAllDiscountsByPage(FilterDiscountCarDto filterDto) throws ParseException {
        StringBuilder builder = new StringBuilder(" SELECT * FROM discount_cars WHERE 1=1 ");
        int pageNo = filterDto.getPageNo();
        int pageSize = filterDto.getPageSize();
        Map<String,Object> params = new HashMap<>();
        if(!filterDto.getDiscountStatus().equals("0")){
            String discountStatus = filterDto.getDiscountStatus();
            if(discountStatus.equals("active")){
                builder.append(" AND quantity>0 ");
            }
            else{
                builder.append(" AND quantity=0 ");
            }
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
                case "quantity_desc":
                    builder.append(" ORDER BY quantity DESC, id ASC ");
                    break;
                case "quantity_asc":
                    builder.append(" ORDER BY quantity ASC, id ASC ");
                    break;
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
        Query query = entityManager.createNativeQuery(builder.toString(),DiscountCars.class);
        int offset = pageSize * (pageNo - 1);
        int limit = pageSize;
        query.setFirstResult(offset).setMaxResults(limit);
        params.forEach(query::setParameter);
        params.forEach(queryTotal::setParameter);
        List<DiscountCars> discountCars = query.getResultList();
        List<DiscountCarsDto> discountDtos=discountCars.stream().map(DiscountCarsConverter::toDiscountCarsDto).toList();
        Long total = ((Number)queryTotal.getSingleResult()).longValue();
        return PageResponseDto.<List<DiscountCarsDto>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .total(total)
                .dataPage(discountDtos)
                .build();
    }

    @Override
    public PageResponseDto<List<DiscountCarsDto>> getSearchDiscountsByPage(String keyword, Integer pageNo, Integer pageSize) {
        StringBuilder builder = new StringBuilder(" SELECT * FROM discount_cars WHERE 1=1 ");
        int offset = pageSize * (pageNo - 1);
        int limit = pageSize;
        if(keyword!=null && !keyword.isEmpty()){
            builder.append(" AND unaccent(code) ILIKE unaccent(:keyword)");
        }
        Query queryTotal = entityManager.createNativeQuery(builder.toString().replace("SELECT *","SELECT COUNT(*)"),Long.class);
        builder.append(" ORDER BY id ASC");
        Query query = entityManager.createNativeQuery(builder.toString(),DiscountCars.class);
        if(keyword!=null && !keyword.isEmpty()){
            query.setParameter("keyword",'%'+keyword+'%');
            queryTotal.setParameter("keyword",'%'+ keyword+'%');
        }
        query.setFirstResult(offset).setMaxResults(limit);
        List<DiscountCars> discounts = query.getResultList();
        long total = ((Number)queryTotal.getSingleResult()).longValue();
        return PageResponseDto.<List<DiscountCarsDto>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .total(total)
                .dataPage(discounts.stream().map(DiscountCarsConverter::toDiscountCarsDto).toList())
                .build();
    }

    @Override
    public DiscountCarsDto getDiscountCarId(Integer id) {
        DiscountCars discount = discountCarsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("DiscountCar","id",String.valueOf(id)));
        return DiscountCarsConverter.toDiscountCarsDto(discount);
    }

    @Override
    public DiscountCarsDto updateDiscountCar(Integer id, DiscountCarRequestDto discountCarRequestDto, MultipartFile file) {
        LocalDateTime startDate = LocalDateTime.parse(discountCarRequestDto.getStartDate());
        LocalDateTime endDate = LocalDateTime.parse(discountCarRequestDto.getEndDate());
        DiscountCars discount = discountCarsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("DiscountCar","id",String.valueOf(id)));

        discount.setDiscountValue(discountCarRequestDto.getDiscountValue());
        discount.setCode(discountCarRequestDto.getCode());
        discount.setEndDate(startDate);
        discount.setStartDate(endDate);
        discount.setQuantity(discountCarRequestDto.getQuantity());
        discount.setDescription(discountCarRequestDto.getDescription());
        discount.setImages(uploadCloudinary.uploadCloudinary(file));
        DiscountCars savedDiscount = discountCarsRepository.save(discount);
        return DiscountCarsConverter.toDiscountCarsDto(savedDiscount);
    }

    @Override
    public DiscountCarsDto createDiscountCar(DiscountCarRequestDto discountCarRequestDto, MultipartFile file) {
        LocalDateTime startDate = LocalDateTime.parse(discountCarRequestDto.getStartDate());
        LocalDateTime endDate = LocalDateTime.parse(discountCarRequestDto.getEndDate());
        String image = uploadCloudinary.uploadCloudinary(file);
        DiscountCars discountCar = DiscountCars.builder()
                .code(discountCarRequestDto.getCode())
                .discountValue(discountCarRequestDto.getDiscountValue())
                .description(discountCarRequestDto.getDescription())
                .images(image)
                .startDate(startDate)
                .endDate(endDate)
                .build();
        DiscountCars savedDiscount = discountCarsRepository.save(discountCar);
        return DiscountCarsConverter.toDiscountCarsDto(savedDiscount);
    }
}
