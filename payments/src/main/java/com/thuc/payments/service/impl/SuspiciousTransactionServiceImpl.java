package com.thuc.payments.service.impl;

import com.thuc.payments.converter.SuspiciousTransactionConverter;
import com.thuc.payments.dto.FilterTranLogsDto;
import com.thuc.payments.dto.PageResponseDto;
import com.thuc.payments.dto.SuspiciousTransactionDto;
import com.thuc.payments.entity.SuspiciousPaymentLog;
import com.thuc.payments.exception.ResourceNotFoundException;
import com.thuc.payments.repository.SuspiciousPaymentLogRepository;
import com.thuc.payments.service.ISuspiciousTransactionService;
import com.thuc.payments.utils.SuspiciousTypeEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SuspiciousTransactionServiceImpl implements ISuspiciousTransactionService {
    private final SuspiciousPaymentLogRepository suspiciousPaymentLogRepository;
    private final RedisTemplate<String,Object> redisTemplate;
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public SuspiciousTransactionDto getSuspiciousTransactionById(Integer id) {
        SuspiciousPaymentLog suspiciousPaymentLog = suspiciousPaymentLogRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("SuspiciousPaymentLog", "id", String.valueOf(id)));
        return SuspiciousTransactionConverter.toSuspiciousTransactionDto(suspiciousPaymentLog);
    }

    @Override
    public List<String> getAllSuspiciousTranTypes() {
        return SuspiciousTypeEnum.getAllValues();
    }

    @Override
    public PageResponseDto<List<SuspiciousTransactionDto>> getSuspiciousTransByFilter(FilterTranLogsDto filterDto) throws ParseException {
        StringBuilder builder = new StringBuilder("SELECT * FROM suspicious_payment_log WHERE 1=1 ");
        Map<String, Object> params = new HashMap<>();
        if(!filterDto.getSuspiciousTranType().equals("0")){
            builder.append(" AND suspicious_type=:suspiciousTranType ");
            params.put("suspiciousTranType", filterDto.getSuspiciousTranType());
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
                case "price_desc":
                    builder.append(" ORDER BY amount DESC ");
                    break;
                case "price_asc":
                    builder.append(" ORDER BY amount ASC ");
                    break;
                case "date_desc":
                    builder.append(" ORDER BY created_at DESC ");
                    break;
                case "date_asc":
                    builder.append(" ORDER BY created_at ASC ");
                    break;
                default:
                    break;
            }
        }
        int pageNo = filterDto.getPageNo();
        int pageSize = filterDto.getPageSize();
        int limit = pageSize;
        int offset = (pageNo-1) * pageSize;
        Query query = entityManager.createNativeQuery(builder.toString(), SuspiciousPaymentLog.class);
        query.setFirstResult(offset).setMaxResults(limit);
        params.forEach(query::setParameter);
        params.forEach(queryTotal::setParameter);
        Long total = ((Number)queryTotal.getSingleResult()).longValue();
        List<SuspiciousPaymentLog> suspiciousPaymentLogs =  query.getResultList();
        List<SuspiciousTransactionDto> result = suspiciousPaymentLogs.stream()
                .map(SuspiciousTransactionConverter::toSuspiciousTransactionDto).toList();
        return PageResponseDto.<List<SuspiciousTransactionDto>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .total(total)
                .dataPage(result)
                .build();
    }

    @Override
    public PageResponseDto<List<SuspiciousTransactionDto>> getSuspiciousTransByKeyword(String keyword, Integer pageNo, Integer pageSize) {
        StringBuilder builder = new StringBuilder("SELECT * FROM suspicious_payment_log WHERE 1=1 ");
        if(keyword != null && !keyword.isEmpty()){
            builder.append(" AND ( cast(id as TEXT)  LIKE  :keyword ");
            builder.append(" OR cast(user_id as TEXT) LIKE :keyword ");
            builder.append(" OR ip_address LIKE :keyword) ");
        }
        Query query = entityManager.createNativeQuery(builder.toString(),SuspiciousPaymentLog.class);
        Query queryTotal = entityManager.createNativeQuery(builder.toString().replace("SELECT *","SELECT COUNT(*)"),Long.class);
        if(keyword != null && !keyword.isEmpty()){
            query.setParameter("keyword",'%'+ keyword + '%');
            queryTotal.setParameter("keyword",'%'+ keyword + '%');
        }
        long total = ((Number)queryTotal.getSingleResult()).longValue();
        int limit = pageSize;
        int offset = (pageNo-1) * pageSize;
        query.setFirstResult(offset).setMaxResults(limit);
        List<SuspiciousPaymentLog> suspiciousPaymentLogs =  query.getResultList();
        List<SuspiciousTransactionDto> result = suspiciousPaymentLogs.stream().map(SuspiciousTransactionConverter::toSuspiciousTransactionDto).toList();
        return PageResponseDto.<List<SuspiciousTransactionDto>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .total(total)
                .dataPage(result)
                .build();
    }
    private String getKeyLockedUserIdTransaction(Integer userId){
        return String.format("payment-block-%d",userId);
    }
    @Override
    public PageResponseDto<List<SuspiciousTransactionDto>> getSuspiciousTransLocked(String keyword, Integer pageNo, Integer pageSize) {
        StringBuilder builder = new StringBuilder(" SELECT * FROM suspicious_payment_log WHERE 1=1 ");
        Map<Integer,Object> map = new HashMap<>();
        if(keyword != null && !keyword.isEmpty()){
            builder.append(" AND ( cast(id as TEXT)  LIKE  :keyword ");
            builder.append(" OR cast(user_id as TEXT) LIKE :keyword ");
            builder.append(" OR ip_address LIKE :keyword) ");
        }
        builder.append(" ORDER BY created_at DESC ");
        Query query = entityManager.createNativeQuery(builder.toString(),SuspiciousPaymentLog.class);
        if(keyword != null && !keyword.isEmpty()){
            query.setParameter("keyword",'%'+ keyword + '%');
        }
        List<SuspiciousPaymentLog> suspiciousPaymentLogs =  query.getResultList();
        List<SuspiciousTransactionDto> suspiciousTransactionDtos = new ArrayList<>();
        for(SuspiciousPaymentLog item : suspiciousPaymentLogs){
            Integer userId = item.getUserId();
            Boolean locked = redisTemplate.hasKey(getKeyLockedUserIdTransaction(userId));
            if(locked){
                if(!map.containsKey(userId)){
                    SuspiciousTransactionDto suspiciousTransactionDto = SuspiciousTransactionConverter.toSuspiciousTransactionDto(item);
                    suspiciousTransactionDtos.add(suspiciousTransactionDto);
                    map.put(userId, Boolean.TRUE);
                }
            }
        }
        int limit = pageSize;
        int offset = (pageNo-1) * pageSize;
        List<SuspiciousTransactionDto> result = suspiciousTransactionDtos.subList(offset, Math.min(offset+limit, suspiciousTransactionDtos.size()));
        return PageResponseDto.<List<SuspiciousTransactionDto>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .dataPage(result)
                .total((long)suspiciousTransactionDtos.size())
                .build();
    }
    @Override
    public List<Integer> unClock(List<Integer> userIds) {
        for(Integer userId : userIds){
            String key = getKeyLockedUserIdTransaction(userId);
            if(redisTemplate.hasKey(key)){
                userIds.remove(userId);
                redisTemplate.delete(key);
            }
        }
        return userIds;
    }
}
