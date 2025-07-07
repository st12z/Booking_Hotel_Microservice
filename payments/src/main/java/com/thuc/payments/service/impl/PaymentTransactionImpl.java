package com.thuc.payments.service.impl;

import com.thuc.payments.converter.PaymentTransactionConverter;
import com.thuc.payments.dto.FilterTransactionDto;
import com.thuc.payments.dto.PageResponseDto;
import com.thuc.payments.dto.PaymentTransactionDto;
import com.thuc.payments.entity.PaymentTransaction;
import com.thuc.payments.repository.PaymentTransactionRepository;
import com.thuc.payments.service.IPaymentTransactionService;
import com.thuc.payments.utils.TransactionType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service

@RequiredArgsConstructor
public class PaymentTransactionImpl implements IPaymentTransactionService {
    private final PaymentTransactionRepository paymentTransactionRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public void createPayment(String vnpResponseCode, String vnpTxnRef, int vnpAmount,
                              String vnpTransactionNo, String vnpTransactionDate) {
        PaymentTransaction paymentTransaction =  PaymentTransaction.builder()
                .transactionType(TransactionType.PAYMENT)
                .vnpResponseCode(vnpResponseCode)
                .vnpTxnRef(vnpTxnRef)
                .vnpAmount(vnpAmount/100)
                .vnpTransactionNo(vnpTransactionNo)
                .vnpTransactionDate(vnpTransactionDate)
                .build();
        paymentTransactionRepository.save(paymentTransaction);
    }
    @Override
    public PageResponseDto<List<PaymentTransactionDto>> getAllTransactions(FilterTransactionDto filterDto) throws ParseException {
        String vnpResponseCode="00";
        String clauseFrom = "SELECT pt.id, pt.vnp_txn_ref, pt.vnp_amount, pt.vnp_transaction_no,pt.vnp_transaction_date, " +
                "pt.vnp_response_code, pt.created_at, pt.created_by, pt.updated_at, pt.updated_by, pt.transaction_type FROM payment_transaction pt";
        StringBuilder builder = new StringBuilder(clauseFrom);
        Map<String,Object> params = new HashMap<>();
        if(filterDto.getPropertyId()!=null && filterDto.getPropertyId()!=0){
            builder.append(" join bill b on b.bill_code = pt.vnp_txn_ref AND b.property_id = :propertyId");
            params.put("propertyId", filterDto.getPropertyId());
        }
        builder.append(" WHERE 1=1 ");
        if(!filterDto.getTransactionType().isEmpty() && !filterDto.getTransactionType().equals("0")){
            builder.append(" AND pt.transaction_type = :transactionType");
            params.put("transactionType", filterDto.getTransactionType());
        }
        if(!filterDto.getTransactionStatus().equals("0") ){
            params.put("vnpResponseCode",vnpResponseCode);
            if(filterDto.getTransactionStatus().equals("SUCCESS")){
                builder.append(" AND pt.vnp_response_code =:vnpResponseCode");
            }
            else{
                builder.append(" AND pt.vnp_response_code !=:vnpResponseCode");
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(!filterDto.getTimeOption().equals("0")){
            builder.append(" AND pt.created_at >=:beginDate AND pt.created_at <=:endDate");
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
        Query queryTotal = entityManager.createNativeQuery(builder.toString().replace(clauseFrom,"SELECT COUNT(*) FROM payment_transaction pt"),Long.class);
        if(!filterDto.getSortOption().equals("0")){
            switch (filterDto.getSortOption()) {
                case "price_desc":
                    builder.append(" ORDER BY pt.vnp_amount DESC ");
                    break;
                case "price_asc":
                    builder.append(" ORDER BY pt.vnp_amount ASC ");
                    break;
                case "date_desc":
                    builder.append(" ORDER BY pt.created_at DESC ");
                    break;
                case "date_asc":
                    builder.append(" ORDER BY pt.created_at ASC ");
                    break;
                default:
                    break;
            }
        }
        int limit = filterDto.getPageSize();
        int offset = (filterDto.getPageNo()-1)*filterDto.getPageSize();
        Query query = entityManager.createNativeQuery(builder.toString(), PaymentTransaction.class);
        query.setFirstResult(offset).setMaxResults(limit);
        params.forEach(query::setParameter);
        params.forEach(queryTotal::setParameter);
        long total = ((Number)queryTotal.getSingleResult()).longValue();
        List<PaymentTransaction> paymentTransactions = query.getResultList();
        List<PaymentTransactionDto> result = paymentTransactions.stream().map(PaymentTransactionConverter::toPaymentTransactionDto).toList();
        return PageResponseDto.<List<PaymentTransactionDto>>builder()
                .total(total)
                .pageNo(filterDto.getPageNo())
                .pageSize(filterDto.getPageSize())
                .dataPage(result)
                .build();
    }

    @Override
    public List<String> getALlTransactionTypes() {
        return TransactionType.getAllValues();
    }

    @Override
    public PageResponseDto<List<PaymentTransactionDto>> getSearchTransaction(String keyword, Integer pageNo, Integer pageSize) {
        StringBuilder builder = new StringBuilder("SELECT * FROM payment_transaction WHERE 1=1 ");
        Map<String, Object> params = new HashMap<>();
        if(keyword != null && !keyword.isEmpty()){
            builder.append(" AND ( vnp_txn_ref LIKE :keyword ");
            builder.append(" OR vnp_transaction_no LIKE :keyword) ");
            params.put("keyword",'%'+keyword+'%');
        }
        int limit = pageSize;
        int offset = (pageNo-1)*pageSize;
        Query query = entityManager.createNativeQuery(builder.toString(), PaymentTransaction.class);
        Query queryTotal = entityManager.createNativeQuery(builder.toString().replace("SELECT *","SELECT COUNT(*)"),Long.class);
        params.forEach(query::setParameter);
        params.forEach(queryTotal::setParameter);
        query.setFirstResult(offset).setMaxResults(limit);
        List<PaymentTransaction> paymentTransactions = query.getResultList();
        Long total = ((Number)queryTotal.getSingleResult()).longValue();
        return PageResponseDto.<List<PaymentTransactionDto>>builder()
                .total(total)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .dataPage(paymentTransactions.stream().map(PaymentTransactionConverter::toPaymentTransactionDto).toList())
                .build();
    }
}
