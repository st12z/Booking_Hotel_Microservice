package com.thuc.bookings.service;

import com.thuc.bookings.dto.requestDto.FilterBillsDto;
import com.thuc.bookings.dto.responseDto.BillDto;
import com.thuc.bookings.dto.responseDto.PageResponseDto;
import com.thuc.bookings.entity.Bill;
import com.thuc.bookings.repository.BillRepository;
import com.thuc.bookings.repository.RefundBillRepository;
import com.thuc.bookings.service.impl.BillServiceImpl;
import com.thuc.bookings.utils.BillStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BillServiceTest {
    @Mock
    private BillRepository billRepository;
    @Mock
    private RefundBillRepository refundBillRepository;
    @Mock
    private Query query;

    @Mock
    private Query queryTotal;

    private FilterBillsDto filterBillsDto;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private BillServiceImpl billService;
    Bill billOne;
    Bill billTwo;
    @BeforeEach
    void setUp() {
        filterBillsDto = new FilterBillsDto();
        filterBillsDto.setPageNo(1);
        filterBillsDto.setPageSize(10);
        filterBillsDto.setTimeOption("0");
        filterBillsDto.setPropertyId(0);
        filterBillsDto.setBillTypeStatus("0");
        filterBillsDto.setSortOption("0");

        billOne = Bill.builder()
                .firstName("nguyen tien")
                .lastName("thuc")
                .email("thuc@gmail.com")
                .phoneNumber("0123456790")
                .propertyId(1)
                .district("Quốc Oai")
                .city("Hà Nội")
                .country("Việt Nam")
                .addressDetail("Xóm 3")
                .originTotalPayment(1000)
                .newTotalPayment(500)
                .pricePromotion(200)
                .discountHotel(5)
                .discountCar(5)
                .billStatus(BillStatus.SUCCESS)
                .isBusinessTrip(1)
                .isShuttleService(1)
                .bookingForWho(1)
                .specialMessage("Yes")
                .userEmail("thuc@gmail.com")
                .billCode("123456")
                .discountHotelId(1)
                .discountCarId(1)
                .build();
        billTwo = Bill.builder()
                .firstName("nguyen tien")
                .lastName("thuc")
                .email("thuc@gmail.com")
                .phoneNumber("0123456790")
                .propertyId(1)
                .district("Quốc Oai")
                .city("Hà Nội")
                .country("Việt Nam")
                .addressDetail("Xóm 3")
                .originTotalPayment(2000)
                .newTotalPayment(1000)
                .pricePromotion(200)
                .discountHotel(5)
                .discountCar(5)
                .billStatus(BillStatus.SUCCESS)
                .isBusinessTrip(1)
                .isShuttleService(1)
                .bookingForWho(1)
                .specialMessage("Yes")
                .userEmail("thuc@gmail.com")
                .billCode("123455")
                .discountHotelId(1)
                .discountCarId(1)
                .build();
    }
    @Test
    @DisplayName("It should be return get my bills")
    public void testGetMyBills() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        List<Bill> bills = List.of(billOne,billTwo);
        Page<Bill> pageBill =new PageImpl<>(bills,pageable,2);
        when(billRepository.findByKeyword(
                any(String.class),
                any(String.class),
                any(Pageable.class))
        ).thenReturn(pageBill);
        //when
        PageResponseDto<List<BillDto>> result = billService.getMyBills("thuc@gmail.com",1,10,"12");
        //then
        verify(billRepository,times(1)).findByKeyword(any(String.class), any(String.class), any(Pageable.class));
        assertThat(result).isNotNull();
        assertThat(result.getDataPage().size()).isEqualTo(2);
        assertEquals(1,result.getPageNo());
        assertEquals(10,result.getPageSize());
        assertEquals(2,result.getTotal());
    }
    @Test
    @DisplayName("It should be return bill by billCode")
    public void testGetBillByBillCode() {
        //given
        when(billRepository.findByBillCode(any())).thenReturn(billOne);
        //when,then
        BillDto billDto = billService.getBillByBillCode(billOne.getBillCode());
        verify(billRepository,times(1)).findByBillCode(any());
        assertThat(billDto).isNotNull();
        assertThat(billDto.getBillCode()).isEqualTo(billOne.getBillCode());
    }
    @Test
    @DisplayName("It should be return amount bills today")
    public void testGetBillToday() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        when(billRepository.countByCreatedAtAndBillStatus(startOfDay,endOfDay,BillStatus.SUCCESS))
                .thenReturn(2);
        //when
        int count = billService.getAmountBillsToday();
        assertThat(count).isEqualTo(2);
        verify(billRepository,times(1))
                .countByCreatedAtAndBillStatus(any(LocalDateTime.class),any(LocalDateTime.class),any(BillStatus.class));
    }
    @Test
    @DisplayName("It should be return amount revenue today")
    public void testGetAmountRevenueToday() {
        //given
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        when(billRepository.getTotalPaymentToday(startOfDay,endOfDay)).thenReturn(1000);
        when(refundBillRepository.getTotalRefundToday(startOfDay,endOfDay)).thenReturn(500);
        //when
        int count = billService.getAmountRevenueToday();
        assertThat(count).isEqualTo(500);
    }
    @Test
    @DisplayName("Should return paged bills")
    void testGetAllBills() throws Exception {
        List<Bill> billList = List.of(billOne,billTwo);

        // Khi entityManager tạo query -> trả về query hoặc queryTotal mock
        when(entityManager.createNativeQuery(anyString(), eq(Bill.class))).thenReturn(query);
        when(entityManager.createNativeQuery(contains("COUNT(*)"), eq(Long.class))).thenReturn(queryTotal);

        // Mock setParameter để trả chính nó (fluent API)
        when(query.setFirstResult(anyInt())).thenReturn(query);
        when(query.setMaxResults(anyInt())).thenReturn(query);
        if (!filterBillsDto.getTimeOption().equals("0") ||
                filterBillsDto.getPropertyId() != 0 ||
                !filterBillsDto.getBillTypeStatus().equals("0")) {
            when(queryTotal.setParameter(anyString(), any())).thenReturn(queryTotal);
            when(query.setParameter(anyString(), any())).thenReturn(query);
        }

        // Mock kết quả
        when(query.getResultList()).thenReturn(billList);
        when(queryTotal.getSingleResult()).thenReturn(2);

        // Khi gọi service
        PageResponseDto<List<BillDto>> result = billService.getAllBills(filterBillsDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getDataPage().size()).isEqualTo(2);
        assertThat(result.getTotal()).isEqualTo(2);
        assertThat(result.getPageNo()).isEqualTo(filterBillsDto.getPageNo());
        assertThat(result.getPageSize()).isEqualTo(filterBillsDto.getPageSize());

        // Verify các method EntityManager/Query được gọi
        verify(entityManager).createNativeQuery(anyString(), eq(Bill.class));
        verify(entityManager).createNativeQuery(contains("COUNT(*)"), eq(Long.class));
        verify(query).setFirstResult(anyInt());
        verify(query).getResultList();
        verify(queryTotal).getSingleResult();
        if (!filterBillsDto.getTimeOption().equals("0") ||
                filterBillsDto.getPropertyId() != 0 ||
                !filterBillsDto.getBillTypeStatus().equals("0")) {
            verify(query).setParameter(anyString(), any());
            verify(queryTotal).setParameter(anyString(), any());
        }
    }
    @Test
    @DisplayName("It should return page bill by keyword")
    public void getBills_byKeyword_returnPageBills(){
        //given
        List<Bill> bills = List.of(billOne,billTwo);
        when(entityManager.createNativeQuery(anyString(),eq(Bill.class))).thenReturn(query);
        when(entityManager.createNativeQuery(contains("COUNT(*)"), eq(Long.class))).thenReturn(queryTotal);

        when(query.setFirstResult(anyInt())).thenReturn(query);
        when(query.setMaxResults(anyInt())).thenReturn(query);
        if (!filterBillsDto.getTimeOption().equals("0") ||
                filterBillsDto.getPropertyId() != 0 ||
                !filterBillsDto.getBillTypeStatus().equals("0")) {
            when(query.setParameter(anyString(), any())).thenReturn(query);
            when(queryTotal.setParameter(anyString(), any())).thenReturn(queryTotal);
        }


        when(query.getResultList()).thenReturn(bills);
        when(queryTotal.getSingleResult()).thenReturn(2);

        //when
        PageResponseDto<List<BillDto>> result = billService.getBillsByKeyword("12",1,10);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getDataPage().size()).isEqualTo(2);
        assertThat(result.getTotal()).isEqualTo(2);
        assertThat(result.getPageNo()).isEqualTo(filterBillsDto.getPageNo());
        assertThat(result.getPageSize()).isEqualTo(filterBillsDto.getPageSize());
        verify(entityManager).createNativeQuery(anyString(), eq(Bill.class));
        verify(entityManager).createNativeQuery(anyString(), eq(Long.class));
        if (!filterBillsDto.getTimeOption().equals("0") ||
                filterBillsDto.getPropertyId() != 0 ||
                !filterBillsDto.getBillTypeStatus().equals("0")) {
            verify(query).setParameter(anyString(), any());
            verify(queryTotal).setParameter(anyString(), any());
        }

    }

}
