package com.thuc.bookings.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thuc.bookings.dto.responseDto.BillDto;
import com.thuc.bookings.dto.responseDto.PageResponseDto;
import com.thuc.bookings.entity.Bill;
import com.thuc.bookings.service.IBillService;
import com.thuc.bookings.service.impl.BillServiceImpl;
import com.thuc.bookings.utils.BillStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BillControllers.class)
public class BillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IBillService billService; // <-- mock tự động

    @Autowired
    private ObjectMapper objectMapper;

    BillDto billOne;
    BillDto billTwo;

    @BeforeEach
    void setUp() {
        billOne = BillDto.builder()
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
        billTwo = BillDto.builder()
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
    @DisplayName("Test API GET /api/bills return code 200")
    public void testGetMyBills() throws Exception {
        //given
        List<BillDto> list = List.of(billOne, billTwo);
        PageResponseDto<List<BillDto>> pageResponseDto = PageResponseDto.<List<BillDto>>builder()
                .pageNo(1)
                .pageSize(10)
                .total(2L)
                .dataPage(list)
                .build();
        when(billService.getMyBills(any(),any(),any(),any())).thenReturn(pageResponseDto);
        //when
        this.mockMvc.perform(get("/api/bills")
                        .header("X-User-Email","thuc@gmail.com")
                        .param("pageNo","1")
                        .param("pageSize","10")
                        .param("keyword","12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.dataPage").isArray())
                .andExpect(jsonPath("$.data.total").value(2));
    }
    @Test
    @DisplayName(("Test API GET /api/bills/search return code 200"))
    public void testGetSearchBills() throws Exception {
        //given
        List<BillDto> list = List.of(billOne, billTwo);
        PageResponseDto<List<BillDto>> pageResponseDto = PageResponseDto.<List<BillDto>>builder()
                .pageNo(1)
                .pageSize(10)
                .total(2L)
                .dataPage(list)
                .build();
        when(billService.getBillsByKeyword(any(),any(),any())).thenReturn(pageResponseDto);
        //when
        this.mockMvc.perform(get("/api/bills/search")
                .param("pageNo","1")
                .param("pageSize","10")
                .param("keyword","12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.dataPage").isArray())
                .andExpect(jsonPath("$.data.total").value(2));
    }
}
