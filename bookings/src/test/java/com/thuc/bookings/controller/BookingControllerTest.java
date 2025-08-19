package com.thuc.bookings.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thuc.bookings.dto.requestDto.BookingCarsRequestDto;
import com.thuc.bookings.dto.requestDto.BookingRoomTypeDto;
import com.thuc.bookings.dto.responseDto.BookingDto;
import com.thuc.bookings.dto.responseDto.PageResponseDto;
import com.thuc.bookings.dto.responseDto.PaymentResponseDto;
import com.thuc.bookings.dto.responseDto.SuccessResponseDto;
import com.thuc.bookings.entity.Bill;
import com.thuc.bookings.entity.BookingCars;
import com.thuc.bookings.entity.BookingRooms;
import com.thuc.bookings.entity.Vehicles;
import com.thuc.bookings.service.IBillService;
import com.thuc.bookings.service.IBookingService;
import com.thuc.bookings.service.IRedisBookingService;
import com.thuc.bookings.service.client.PaymentsFeignClient;
import com.thuc.bookings.service.client.RoomTypesFeignClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import org.springframework.test.web.servlet.MockMvc.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static reactor.core.publisher.Mono.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(BookingControllers.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IBookingService bookingService; // <-- mock tự động

    @MockBean
    private PaymentsFeignClient paymentsFeignClient;

    @MockBean
    private IRedisBookingService redisBookingService;

    @MockBean
    private RoomTypesFeignClient roomTypesFeignClient;

    @MockBean
    private StreamBridge streamBridge;

    @Autowired
    private ObjectMapper objectMapper;
    BookingDto bookingDto;
    BookingCars bookingCars;
    Vehicles vehicle;
    BookingRooms bookingRoom;
    @BeforeEach
    public void setUp(){

        bookingDto = new BookingDto();
        bookingDto.setAddressDetail("123 Le Loi");
        bookingDto.setBookingForWho(1);
        bookingDto.setCity("Ho Chi Minh");
        bookingDto.setCountry("Vietnam");
        bookingDto.setDistrict("District 1");
        bookingDto.setEmail("thuc@example.com");
        bookingDto.setFirstName("Nguyen");
        bookingDto.setLastName("Tien");
        bookingDto.setPhoneNumber("0123456789");
        bookingDto.setNewTotalPayment(1000000);
        bookingDto.setOriginTotalPayment(1200000);
        bookingDto.setPropertyId(1);
        bookingDto.setPriceCar(200000);
        bookingDto.setPricePromotion(100000);
        bookingDto.setDiscountCar(0);
        bookingDto.setDiscountHotel(0);
        bookingDto.setDiscountCarId(0);
        bookingDto.setDiscountHotelId(0);
        bookingDto.setUserEmail("thuc@example.com");
        bookingDto.setIsBusinessTrip(0);
        bookingDto.setIsShuttleService(0);
        bookingDto.setSpecialMessage("No special request");
        BookingRoomTypeDto roomTypeDto = new BookingRoomTypeDto();
        roomTypeDto.setCheckIn(LocalDateTime.of(2025, 8, 17, 14, 0));
        roomTypeDto.setCheckOut(LocalDateTime.of(2025, 8, 20, 12, 0));
        roomTypeDto.setDayStays(3);
        roomTypeDto.setOriginPayment(300000);
        roomTypeDto.setNewPayment(270000);
        roomTypeDto.setPromotion(30000);
        roomTypeDto.setQuantityRooms(2);
        roomTypeDto.setRoomTypeId(1);
        roomTypeDto.setPropertyId(1);

        bookingDto.setRoomTypes(List.of(roomTypeDto));
        bookingDto.setBookingCars(List.of(new BookingCarsRequestDto(1,1000)));

        bookingCars = BookingCars.builder()
                .id(1)
                .billId(1)
                .priceBooking(1000)
                .build();

        vehicle = Vehicles.builder()
                .id(1)
                .quantity(5)
                .build();

        bookingRoom = BookingRooms.builder()
                .id(1)
                .billId(1)
                .roomTypeId(1)
                .numRooms(List.of(101))
                .checkIn(LocalDateTime.now())
                .checkOut(LocalDateTime.now().plusDays(1))
                .build();
    }
    @Test
    public void testConfirm() throws Exception {

        Mockito.when(bookingService.confirm(anyString())).thenReturn("https://payment");
        //then
        mockMvc.perform(get("/api/bookings/confirm")
                        .param("uniqueCheck", "abc123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("https://payment"));
    }
}

