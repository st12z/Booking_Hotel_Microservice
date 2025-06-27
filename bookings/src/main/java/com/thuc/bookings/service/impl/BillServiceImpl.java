package com.thuc.bookings.service.impl;

import com.thuc.bookings.converter.BillConverter;
import com.thuc.bookings.dto.requestDto.FilterBillsDto;
import com.thuc.bookings.dto.responseDto.BillDto;
import com.thuc.bookings.dto.responseDto.PageResponseDto;
import com.thuc.bookings.dto.responseDto.StatisticBillByMonth;
import com.thuc.bookings.entity.Bill;
import com.thuc.bookings.entity.RefundBill;
import com.thuc.bookings.exception.ResourceNotFoundException;
import com.thuc.bookings.repository.BillRepository;
import com.thuc.bookings.repository.RefundBillRepository;
import com.thuc.bookings.service.IBillService;
import com.thuc.bookings.utils.BillStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements IBillService {
    private final BillRepository billRepository;
    private final RefundBillRepository refundBillRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public PageResponseDto<List<BillDto>> getMyBills(String email, Integer pageNo, Integer pageSize,String keyword) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<Bill> billPage = billRepository.findByKeyword(email, '%'+keyword+'%',pageable);
        List<BillDto> billDtos = billPage.getContent().stream().map(BillConverter::toBillDto).toList();
        return PageResponseDto.<List<BillDto>>builder()
                .total(billPage.getTotalElements())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .dataPage(billDtos)
                .build();
    }


    @Override
    public BillDto getBillByBillCode(String billCode) {
        Bill bill = billRepository.findByBillCode(billCode);
        if(bill==null){
            throw new ResourceNotFoundException("Bill","billCode",String.valueOf(billCode));
        }
        return BillConverter.toBillDto(bill);
    }

    @Override
    public Integer getAmountBillsToday() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        return (int) billRepository.countByCreatedAtAndBillStatus(startOfDay,endOfDay,BillStatus.SUCCESS);
    }

    @Override
    public Integer getAmountRevenueToday() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        int totalRevenue=  billRepository.getTotalPaymentToday(startOfDay,endOfDay) !=null ?
                billRepository.getTotalPaymentToday(startOfDay,endOfDay):0 ;
        int totalRefund = refundBillRepository.getTotalRefundToday(startOfDay,endOfDay) != null ?
                refundBillRepository.getTotalRefundToday(startOfDay,endOfDay) : 0;
        return totalRevenue-totalRefund;
    }

    @Override
    public List<StatisticBillByMonth> getAmountBillsByMonth(Integer month) {
        List<StatisticBillByMonth> result = new ArrayList<>();
        Year currentYear = Year.now();
        YearMonth currentMonth = currentYear.atMonth(month);
        int daysInMonth = currentMonth.lengthOfMonth();
        for(int i=1;i<=daysInMonth;i++){
            LocalDateTime startOfDay = LocalDateTime.of(currentYear.getValue(),month,i,0,0,0);
            LocalDateTime endOfDay = LocalDateTime.of(currentYear.getValue(),month,i,23,59,59,999_999_999);
            int total=billRepository.countByCreatedAtAndBillStatus(startOfDay,endOfDay,BillStatus.SUCCESS);
            result.add(new StatisticBillByMonth(i,total));
        }
        return result;

    }

    @Override
    public List<StatisticBillByMonth> getAmountRevenueByMonth(Integer month) {
        List<StatisticBillByMonth> result = new ArrayList<>();
        Year currentYear = Year.now();
        YearMonth currentMonth = currentYear.atMonth(month);
        int daysInMonth = currentMonth.lengthOfMonth();
        for(int i=1;i<=daysInMonth;i++){
            LocalDateTime startOfDay = LocalDateTime.of(currentYear.getValue(),month,i,0,0,0);
            LocalDateTime endOfDay = LocalDateTime.of(currentYear.getValue(),month,i,23,59,59,999_999_999);
            int totalRevenue=  billRepository.getTotalPaymentToday(startOfDay,endOfDay) !=null ?
                    billRepository.getTotalPaymentToday(startOfDay,endOfDay):0 ;
            int totalRefund = refundBillRepository.getTotalRefundToday(startOfDay,endOfDay) != null ?
                    refundBillRepository.getTotalRefundToday(startOfDay,endOfDay) : 0;
            result.add(new StatisticBillByMonth(i,totalRevenue-totalRefund));
        }
        return result;
    }

    @Override
    public List<BillDto> getAllBillsRecently() {
        return billRepository.findAll().stream().sorted(Comparator.comparing(Bill::getCreatedAt).reversed())
                .limit(6)
                .map(BillConverter::toBillDto).toList();
    }

    @Override
    public Map<Integer,Integer> getAmountBillsByPropertyIds(List<Integer> propertyIds) {
        Map<Integer,Integer> result = new HashMap<>();
        for(Integer propertyId:propertyIds){
            Integer amount = billRepository.countByPropertyId(propertyId)==null ? 0 : billRepository.countByPropertyId(propertyId);
            result.put(propertyId,amount);
        }
        return result;
    }

    @Override
    public Map<Integer,Integer> getAmountRevenueByPropertyIds(List<Integer> propertyIds) {
        Map<Integer,Integer> result = new HashMap<>();
        for(Integer propertyId:propertyIds){
            List<Bill> bills = billRepository.findByPropertyId(propertyId);
            Integer total = bills.stream().mapToInt(item->{
                int totalPayment = item.getNewTotalPayment();
                RefundBill refundBill = refundBillRepository.findByVnpTxnRef(item.getBillCode());
                int totalRefund = 0;
                if(refundBill!=null){
                    totalRefund=refundBill.getVnp_Amount();
                }
                return totalPayment-totalRefund;
            }).sum();
            result.put(propertyId,total);
        }
        return result;
    }

    @Override
    public PageResponseDto<List<BillDto>> getAllBills(FilterBillsDto filterBillsDto) throws ParseException {
        StringBuilder builder = new StringBuilder("SELECT * FROM bill b WHERE 1=1 ");
        Map<String,Object> params = new HashMap<>();
        int pageNo = filterBillsDto.getPageNo();
        int pageSize = filterBillsDto.getPageSize();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(!filterBillsDto.getTimeOption().equals("0")){
            builder.append(" AND b.created_at >=:beginDate AND b.created_at <=:endDate");
            LocalDate now = LocalDate.now();
            switch (filterBillsDto.getTimeOption()) {
                case "custom":{
                    Date beginDate = sdf.parse(filterBillsDto.getBeginDate());
                    Date endDate = sdf.parse(filterBillsDto.getEndDate());
                    params.put("beginDate",beginDate);
                    params.put("endDate",endDate);
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


        if(filterBillsDto.getPropertyId()!=0){
            builder.append(" AND b.property_id=:propertyId ");
            params.put("propertyId",filterBillsDto.getPropertyId());
        }
        if(!filterBillsDto.getBillTypeStatus().equals("0")){
            builder.append(" AND b.bill_status=:billType ");
            params.put("billType",filterBillsDto.getBillTypeStatus());
        }
        Query queryTotal = entityManager.createNativeQuery(builder.toString().replace("SELECT *","SELECT COUNT(*)"),Long.class);
        if(!filterBillsDto.getSortOption().equals("0")){
            switch (filterBillsDto.getSortOption()) {
                case "price_desc":
                    builder.append(" ORDER BY b.new_total_payment DESC ");
                    break;
                case "price_asc":
                    builder.append(" ORDER BY b.new_total_payment ASC ");
                    break;
                case "date_desc":
                    builder.append(" ORDER BY b.created_at DESC ");
                    break;
                case "date_asc":
                    builder.append(" ORDER BY b.created_at ASC ");
                    break;
                default:
                    break;
            }
        }
        int limit = pageSize;
        int offset= (pageNo-1)*pageSize;
        Query query = entityManager.createNativeQuery(builder.toString(),Bill.class);
        query.setFirstResult(offset).setMaxResults(limit);
        params.forEach(query::setParameter);
        params.forEach(queryTotal::setParameter);
        List<Bill> bills = query.getResultList();
        List<BillDto> billDtos=bills.stream().map(BillConverter::toBillDto).toList();
        Long total = ((Number)queryTotal.getSingleResult()).longValue();
        return PageResponseDto.<List<BillDto>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .total(total)
                .dataPage(billDtos)
                .build();
    }

    @Override
    public List<String> getAllTypeOfBillStatus() {
        return BillStatus.getAllValues();
    }

    @Override
    public PageResponseDto<List<BillDto>> getBillsByKeyword(String keyword, Integer pageNo, Integer pageSize) {
        StringBuilder builder = new StringBuilder("SELECT * FROM Bill b WHERE 1=1 ");
        if(!keyword.equals("")){
            builder.append(" AND (b.email ILIKE :keyword ");
            builder.append(" OR unaccent(lower(b.first_name)) ILIKE unaccent(:keyword) ");
            builder.append(" OR unaccent(lower(b.last_name)) ILIKE unaccent(:keyword) ");
            builder.append(" OR unaccent(b.bill_code) ILIKE unaccent(:keyword) ");
            builder.append(" OR b.phone_number ILIKE :keyword ) ");
        }
        Query query = entityManager.createNativeQuery(builder.toString(),Bill.class);
        Query queryTotal = entityManager.createNativeQuery(builder.toString().replace("SELECT *","SELECT COUNT(*)"),Long.class);
        if(!keyword.equals("")){
            query.setParameter("keyword",'%'+keyword+'%');
            queryTotal.setParameter("keyword",'%'+keyword+'%');
        }
        int limit = pageSize;
        int offset= (pageNo-1)*pageSize;
        query.setFirstResult(offset).setMaxResults(limit);
        List<Bill> bills = query.getResultList();
        List<BillDto> billDtos=bills.stream().map(BillConverter::toBillDto).toList();
        Long total = ((Number)queryTotal.getSingleResult()).longValue();
        return PageResponseDto.<List<BillDto>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .total(total)
                .dataPage(billDtos)
                .build();

    }

}