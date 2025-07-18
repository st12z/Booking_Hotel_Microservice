package com.thuc.rooms.service.impl;


import com.thuc.rooms.converter.DiscountConverter;
import com.thuc.rooms.dto.*;
import com.thuc.rooms.entity.Discount;
import com.thuc.rooms.entity.UserDiscount;
import com.thuc.rooms.exception.BadRequestCustomException;
import com.thuc.rooms.exception.ResourceAlreadyExistsException;
import com.thuc.rooms.exception.ResourceNotFoundException;
import com.thuc.rooms.repository.DiscountRepository;
import com.thuc.rooms.repository.UserDiscountRepository;
import com.thuc.rooms.service.IDiscountService;
import com.thuc.rooms.utils.DiscountEnum;
import com.thuc.rooms.utils.UploadCloudinary;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
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
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements IDiscountService {
    private final DiscountRepository discountRepository;
    private final Logger log = LoggerFactory.getLogger(DiscountServiceImpl.class);
    private final StreamBridge streamBridge;
    private final RedissonClient redissonClient;
    private final UserDiscountRepository userDiscountRepository;
    private final UploadCloudinary uploadCloudinary;
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<DiscountDto> getAllDiscounts() {
        log.debug("Request to get all Discounts successfully");
        List<Discount> discounts = discountRepository.findActiveDiscount();
        return discounts.stream().map(DiscountConverter::toDiscountDto).toList();
    }



    @Override
    public UserDiscountDto saveDiscount(UserDiscountDto userDiscountDto) {
        log.debug("19-save discount with {}", userDiscountDto);
        handleSaveDiscount(userDiscountDto);
        return userDiscountDto;
    }

    @Override
    public List<DiscountDto> getMyDiscountsByEmail(String email) {
        List<UserDiscount> myDiscounts = userDiscountRepository.findByEmail(email);
        return myDiscounts.stream().map(userDiscount -> {
            int discountId= userDiscount.getDiscountId();
            Discount discount = getDiscountById(discountId);
            return DiscountConverter.toDiscountDto(discount);
        }).toList();
    }

    @Override
    public PageResponseDto<List<DiscountDto>> getMyDiscountsByEmailPage(String email, Integer pageNo, Integer pageSize) {
        StringBuilder builder = new StringBuilder(" SELECT * from user_discounts u where u.email=:email AND EXISTS (SELECT 1 FROM discount d WHERE u.discount_id = d.id AND d.start_date <= now() AND d.end_date>=now())");
        Query query = entityManager.createNativeQuery(builder.toString(),UserDiscount.class);
        Query queryTotal = entityManager.createNativeQuery(builder.toString().replace("SELECT *","SELECT COUNT(*)"),Long.class);
        query.setParameter("email",email);
        queryTotal.setParameter("email",email);
        query.setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize);
        List<UserDiscount> userDiscounts = query.getResultList();
        List<Discount> discounts = userDiscounts.stream().map(userDiscount -> {
            int discountId= userDiscount.getDiscountId();
            return getDiscountById(discountId);
        }).toList();
        long total = ((Number) queryTotal.getSingleResult()).longValue();
        return PageResponseDto.<List<DiscountDto>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .total(total)
                .dataPage(discounts.stream().map(DiscountConverter::toDiscountDto).toList())
                .build();
    }

    @Override
    public List<String> getAllDiscountTypes() {
        return DiscountEnum.getAllDiscountTypes();
    }

    @Override
    public PageResponseDto<List<DiscountDto>> getAllDiscountsByFilter(FilterDiscountDto filterDto) throws ParseException {
        StringBuilder builder = new StringBuilder(" SELECT * FROM discount WHERE 1=1 ");
        int pageNo = filterDto.getPageNo();
        int pageSize = filterDto.getPageSize();
        Map<String,Object> params = new HashMap<>();
        if(!filterDto.getDiscountType().equals("0")){
            builder.append(" AND discount_type=:discountType ");
            params.put("discountType",filterDto.getDiscountType());
        }
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

        Query query = entityManager.createNativeQuery(builder.toString(),Discount.class);
        int offset = pageSize * (pageNo - 1);
        int limit = pageSize;
        query.setFirstResult(offset).setMaxResults(limit);
        params.forEach(query::setParameter);
        params.forEach(queryTotal::setParameter);
        List<Discount> discounts = query.getResultList();
        List<DiscountDto> discountDtos=discounts.stream().map(DiscountConverter::toDiscountDto).toList();
        Long total = ((Number)queryTotal.getSingleResult()).longValue();
        return PageResponseDto.<List<DiscountDto>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .total(total)
                .dataPage(discountDtos)
                .build();

    }

    @Override
    public PageResponseDto<List<DiscountDto>> getSearchDiscounts(String keyword, Integer pageNo, Integer pageSize) {
        StringBuilder builder = new StringBuilder(" SELECT * FROM discount WHERE 1=1 ");
        int offset = pageSize * (pageNo - 1);
        int limit = pageSize;
        if(keyword!=null && !keyword.isEmpty()){
            builder.append(" AND unaccent(code) ILIKE unaccent(:keyword)");
        }
        Query queryTotal = entityManager.createNativeQuery(builder.toString().replace("SELECT *","SELECT COUNT(*)"),Long.class);
        builder.append(" ORDER BY id ASC");
        Query query = entityManager.createNativeQuery(builder.toString(),Discount.class);
        if(keyword!=null && !keyword.isEmpty()){
            query.setParameter("keyword",'%'+keyword+'%');
            queryTotal.setParameter("keyword",'%'+ keyword+'%');
        }
        query.setFirstResult(offset).setMaxResults(limit);
        List<Discount> discounts = query.getResultList();
        long total = ((Number)queryTotal.getSingleResult()).longValue();
        return PageResponseDto.<List<DiscountDto>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .total(total)
                .dataPage(discounts.stream().map(DiscountConverter::toDiscountDto).toList())
                .build();

    }

    @Override
    public DiscountDto getDiscountDtoById(Integer id) {
        return DiscountConverter.toDiscountDto(getDiscountById(id));
    }

    @Override
    public DiscountDto updateDiscount(Integer id, DiscountHotelRequestDto discountDto, MultipartFile file) {
        LocalDateTime startDate = LocalDateTime.parse(discountDto.getStartDate());
        LocalDateTime endDate = LocalDateTime.parse(discountDto.getEndDate());
        Discount discount = getDiscountById(id);
        discount.setDiscountType(discountDto.getDiscountType());
        discount.setDiscountValue(discountDto.getDiscountValue());
        discount.setQuantity(discountDto.getQuantity());
        discount.setImage(uploadCloudinary.uploadCloudinary(file));
        discount.setStartDate(startDate);
        discount.setEndDate(endDate);
        Discount savedDiscount = discountRepository.save(discount);
        return DiscountConverter.toDiscountDto(savedDiscount);
    }

    @Override
    public DiscountDto createDiscount(DiscountHotelRequestDto discountDto, MultipartFile file) {
        LocalDateTime startDate = LocalDateTime.parse(discountDto.getStartDate());
        LocalDateTime endDate = LocalDateTime.parse(discountDto.getEndDate());
        String image = uploadCloudinary.uploadCloudinary(file);
        Discount discount = Discount.builder()
                .code(discountDto.getCode())
                .discountType(discountDto.getDiscountType())
                .discountValue(discountDto.getDiscountValue())
                .quantity(discountDto.getQuantity())
                .image(image)
                .startDate(startDate)
                .endDate(endDate)
                .build();
        Discount savedDiscount = discountRepository.save(discount);
        return DiscountConverter.toDiscountDto(savedDiscount);
    }


    private void handleSaveDiscount(UserDiscountDto userDiscountDto) {
        int discountId=userDiscountDto.getDiscountId();
        String email = userDiscountDto.getEmail();
        RLock lock = redissonClient.getLock(String.format("save:%d", discountId));
        boolean isLocked = false;
        try{
            isLocked = lock.tryLock(10,10, TimeUnit.SECONDS);
            log.debug("Email :{}",email);
            log.debug("isLocked:{}", isLocked);
            log.debug("ThreadName:{}", Thread.currentThread().getName());
            if(isLocked){
                boolean check = handleCheckQuantity(userDiscountDto);
                if(check){
                    UserDiscount existDiscount = userDiscountRepository.
                            findByDiscountIdAndEmail(discountId,email);
                    if(existDiscount!=null){
                        throw new ResourceAlreadyExistsException("UserDiscount","discountId-email",
                                String.format("%d-%s",discountId,email));
                    }
                    UserDiscount userDiscount = UserDiscount.builder()
                            .email(email)
                            .discountId(discountId)
                            .build();
                    userDiscountRepository.save(userDiscount);
                }
                else{
                    throw new BadRequestCustomException("Discount is sold out");
                }
            }
        }catch (InterruptedException e){
            throw new RuntimeException("Failure acquire lock ",e);
        }finally {
            if(isLocked && lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }

    }

    private boolean handleCheckQuantity(UserDiscountDto userDiscountDto) {
        Discount discount = getDiscountById(userDiscountDto.getDiscountId());
        int quantity = discount.getQuantity();
        log.debug("current quantity:{}", quantity);
        if(quantity>0){
            discount.setQuantity(quantity-1);
            discountRepository.save(discount);
            return true;
        }
        return false;
    }

    private Discount getDiscountById( int discountId) {
        return discountRepository.findById(discountId)
                .orElseThrow(() -> new ResourceNotFoundException("Discount","id",String.valueOf(discountId) ));
    }
}
