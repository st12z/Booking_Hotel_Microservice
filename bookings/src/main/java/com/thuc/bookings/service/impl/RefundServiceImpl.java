package com.thuc.bookings.service.impl;

import com.thuc.bookings.converter.BillConverter;
import com.thuc.bookings.converter.RefundBillConveter;
import com.thuc.bookings.dto.requestDto.FilterBillsDto;
import com.thuc.bookings.dto.requestDto.FilterRefundBillDto;
import com.thuc.bookings.dto.responseDto.BillDto;
import com.thuc.bookings.dto.responseDto.PageResponseDto;
import com.thuc.bookings.dto.responseDto.RefundBillDto;
import com.thuc.bookings.entity.Bill;
import com.thuc.bookings.entity.RefundBill;
import com.thuc.bookings.repository.BillRepository;
import com.thuc.bookings.repository.RefundBillRepository;
import com.thuc.bookings.service.IRefundBillService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
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
public class RefundServiceImpl implements IRefundBillService {
    private final RefundBillRepository refundBillRepository;
    private final BillRepository billRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public void createRefundBill(RefundBillDto refundBillDto) {
        RefundBill refundBill = RefundBillConveter.toRefundBill(refundBillDto);
        refundBillRepository.save(refundBill);
    }

    @Override
    public PageResponseDto<List<RefundBillDto>> getAllRefundBills(FilterRefundBillDto filterDto) throws ParseException {
        String selectClause = " SELECT rb.id, rb.created_at, rb.created_by, rb.updated_at, rb.updated_by, " +
                "rb.vnp_amount, rb.vnp_bank_code, rb.vnp_command, rb.vnp_message, rb.vnp_order_info, " +
                "rb.vnp_pay_date, rb.vnp_response_code, rb.vnp_response_id, rb.vnp_secure_hash, rb.vnp_tmn_code, " +
                "rb.vnp_transaction_no, rb.vnp_transaction_status, rb.vnp_transaction_type, rb.vnp_txn_ref, rb.email FROM refund_bills rb";
        StringBuilder builder = new StringBuilder(selectClause);
        Map<String, Object> params = new HashMap<>();
        int pageNo = filterDto.getPageNo();
        int pageSize = filterDto.getPageSize();
        if(filterDto.getPropertyId() != null && filterDto.getPropertyId()!=0) {
            builder.append(" join bill b on b.bill_code = rb.vnp_txn_ref AND b.property_id = :propertyId");
            params.put("propertyId", filterDto.getPropertyId());
        }
        builder.append(" WHERE 1=1 ");
        if(filterDto.getTransactionType() != null && !filterDto.getTransactionType().equals("0")) {
            builder.append(" AND rb.vnp_transaction_type = :transactionType");
            params.put("transactionType", filterDto.getTransactionType());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(!filterDto.getTimeOption().equals("0")){
            builder.append(" AND rb.created_at >=:beginDate AND rb.created_at <=:endDate");
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
        Query queryTotal = entityManager.createNativeQuery(builder.toString().replace(selectClause,"SELECT COUNT(*) FROM refund_bills rb"),Long.class);
        if(!filterDto.getSortOption().equals("0")){
            switch (filterDto.getSortOption()) {
                case "price_desc":
                    builder.append(" ORDER BY rb.vnp_amount DESC ");
                    break;
                case "price_asc":
                    builder.append(" ORDER BY rb.vnp_amount ASC ");
                    break;
                case "date_desc":
                    builder.append(" ORDER BY rb.created_at DESC ");
                    break;
                case "date_asc":
                    builder.append(" ORDER BY rb.created_at ASC ");
                    break;
                default:
                    break;
            }
        }
        int limit = pageSize;
        int offset= (pageNo-1)*pageSize;
        Query query = entityManager.createNativeQuery(builder.toString(), RefundBill.class);
        query.setFirstResult(offset).setMaxResults(limit);
        params.forEach(query::setParameter);
        params.forEach(queryTotal::setParameter);
        List<RefundBill> refundBills = query.getResultList();
        List<RefundBillDto> listRefundBillDto = new ArrayList<>();
        for(RefundBill refundBill : refundBills){
            String billCode = refundBill.getVnp_TxnRef();
            Bill bill = billRepository.findByBillCode(billCode);
            RefundBillDto refundBillDto = RefundBillConveter.toRefundBillDto(refundBill);
            refundBillDto.setOriginPayment(bill.getNewTotalPayment());
            listRefundBillDto.add(refundBillDto);
        }
        Long total = ((Number)queryTotal.getSingleResult()).longValue();
        return PageResponseDto.<List<RefundBillDto>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .total(total)
                .dataPage(listRefundBillDto)
                .build();

    }

    @Override
    public PageResponseDto<List<RefundBillDto>> getRefundBillsBySearch(String keyword, Integer pageNo, Integer pageSize) {
        StringBuilder builder = new StringBuilder("SELECT * FROM refund_bills  WHERE 1=1 ");
        if(keyword!=null && !keyword.isEmpty()){
            builder.append(" AND (vnp_txn_ref LIKE :keyword ");
            builder.append(" OR  vnp_transaction_no LIKE :keyword ");
            builder.append(" OR email LIKE :keyword)");
        }
        Query query = entityManager.createNativeQuery(builder.toString(), RefundBill.class);
        Query queryTotal = entityManager.createNativeQuery(builder.toString().replace("SELECT *","SELECT COUNT(*)"), Long.class);
        if(keyword!=null && !keyword.isEmpty()){
            query.setParameter("keyword", '%' + keyword + '%');
            queryTotal.setParameter("keyword", '%' + keyword + '%');
        }
        int limit = pageSize;
        int offset= (pageNo-1)*pageSize;
        query.setFirstResult(offset).setMaxResults(limit);
        Long total = ((Number)queryTotal.getSingleResult()).longValue();
        List<RefundBill> refundBills = query.getResultList();
        List<RefundBillDto> listRefundBillDto = new ArrayList<>();
        for(RefundBill refundBill : refundBills){
            String billCode = refundBill.getVnp_TxnRef();
            Bill bill = billRepository.findByBillCode(billCode);
            RefundBillDto refundBillDto= RefundBillConveter.toRefundBillDto(refundBill);
            refundBillDto.setOriginPayment(bill.getNewTotalPayment());
            listRefundBillDto.add(refundBillDto);
        }
        return PageResponseDto.<List<RefundBillDto>>builder()
                .dataPage(listRefundBillDto)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .total(total)
                .build();

    }
}
