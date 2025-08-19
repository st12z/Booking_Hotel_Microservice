package com.thuc.bookings.integration;

import com.thuc.bookings.dto.requestDto.BookingCarsRequestDto;
import com.thuc.bookings.dto.requestDto.BookingRoomTypeDto;
import com.thuc.bookings.dto.responseDto.BillDto;
import com.thuc.bookings.dto.responseDto.BookingDto;
import com.thuc.bookings.entity.Bill;
import com.thuc.bookings.entity.BookingCars;
import com.thuc.bookings.entity.BookingRooms;
import com.thuc.bookings.entity.Vehicles;
import com.thuc.bookings.utils.BillStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BillIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(
                    DockerImageName.parse("postgis/postgis:15-3.3")
                            .asCompatibleSubstituteFor("postgres")
            )
                    .withDatabaseName("testdb")
                    .withUsername("postgres")
                    .withPassword("123456")
                    .withCommand("postgres", "-c", "timezone=Asia/Ho_Chi_Minh")
                    .withEnv("TZ", "Asia/Ho_Chi_Minh")
                    .withInitScript("init.sql");
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private String urlBooking = "http://localhost";

    private static RestTemplate restTemplate;

    @BeforeAll
    public static void init(){
        restTemplate = new RestTemplate();
    }
    BookingDto bookingDto;
    BookingCars bookingCars;
    Vehicles vehicle;
    BookingRooms bookingRoom;

    @BeforeEach
    public void setUp(){
        baseUrl= baseUrl + ":" + port +"/api/bills";
        urlBooking= baseUrl + ":" + port +"/api/bookings";

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
    public void testGetMyBill(){

    }
}

