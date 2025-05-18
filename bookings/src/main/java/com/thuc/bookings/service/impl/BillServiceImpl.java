package com.thuc.bookings.service.impl;

import com.thuc.bookings.converter.BillConverter;
import com.thuc.bookings.dto.responseDto.BillDto;
import com.thuc.bookings.dto.responseDto.PageResponseDto;
import com.thuc.bookings.dto.responseDto.StatisticBillByMonth;
import com.thuc.bookings.entity.Bill;
import com.thuc.bookings.repository.BillRepository;
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

import java.time.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements IBillService {
    private final BillRepository billRepository;

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public PageResponseDto<List<BillDto>> getMyBills(String email, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("createdAt").descending());
        Page<Bill> billPage = billRepository.findByUserEmailAndBillStatus(email, BillStatus.SUCCESS,pageable);
        List<BillDto> billDtos = billPage.getContent().stream().map(BillConverter::toBillDto).toList();
        return PageResponseDto.<List<BillDto>>builder()
                .total(billPage.getTotalElements())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .dataPage(billDtos)
                .build();
    }

    @Override
    public PageResponseDto<List<BillDto>> getBillsByKeyword(String email,String keyword,Integer pageNo, Integer pageSize) {
        StringBuilder builder = new StringBuilder(" SELECT * FROM bill WHERE 1=1 ");
        builder.append(" AND user_email = :email ");
        builder.append(" AND ( bill_code LIKE :keyword ");
        builder.append(" OR first_name LIKE :keyword ");
        builder.append(" OR last_name LIKE :keyword ");
        builder.append(" OR phone_number LIKE :keyword)");

        String count = builder.toString().replace("SELECT *"," SELECT COUNT(*) ");
        Query queryCount = entityManager.createNativeQuery(count, Integer.class);
        queryCount.setParameter("keyword", "%"+keyword+"%");
        queryCount.setParameter("email",email);
        int total =(Integer) queryCount.getSingleResult();

        builder.append(" ORDER BY created_at desc ");
        int offset = (pageNo - 1) * pageSize;
        Query query = entityManager.createNativeQuery(builder.toString(), Bill.class);
        query.setParameter("keyword", "%"+keyword+"%");
        query.setParameter("email",email);
        query.setFirstResult(offset);
        query.setMaxResults(pageSize);
        List<BillDto> billDtos = query.getResultList();
        return PageResponseDto.<List<BillDto>>builder()
                .total((long)total)
                .dataPage(billDtos)
                .pageSize(pageSize)
                .pageNo(pageNo)
                .build();
    }

    @Override
    public BillDto getBillByBillCode(String billCode) {
        Bill bill = billRepository.findByBillCode(billCode);
        return BillConverter.toBillDto(bill);
    }

    @Override
    public Integer getAmountBills() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        return (int) billRepository.countByCreatedAt(startOfDay,endOfDay);
    }

    @Override
    public Integer getAmountRevenueToday() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        return  billRepository.getTotalPaymentToday(startOfDay,endOfDay) !=null ? billRepository.getTotalPaymentToday(startOfDay,endOfDay):0 ;
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
            int total=billRepository.countByCreatedAt(startOfDay,endOfDay);
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
            int total=billRepository.getTotalPaymentToday(startOfDay,endOfDay) !=null ? billRepository.getTotalPaymentToday(startOfDay,endOfDay):0 ;
            result.add(new StatisticBillByMonth(i,total));
        }
        return result;
    }

    @Override
    public List<BillDto> getAllBills() {
        return billRepository.findAll().stream().sorted(Comparator.comparing(Bill::getCreatedAt).reversed())
                .limit(6)
                .map(BillConverter::toBillDto).toList();
    }

}