package com.thuc.bookings.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thuc.bookings.dto.requestDto.BookingCarsRequestDto;
import com.thuc.bookings.dto.requestDto.BookingRoomTypeDto;
import com.thuc.bookings.dto.responseDto.BookingDto;
import com.thuc.bookings.dto.responseDto.PaymentResponseDto;
import com.thuc.bookings.dto.responseDto.SuccessResponseDto;
import com.thuc.bookings.entity.BookingCars;
import com.thuc.bookings.entity.BookingRooms;
import com.thuc.bookings.entity.Vehicles;
import com.thuc.bookings.repository.VehiclesRepository;
import com.thuc.bookings.service.IRedisBookingService;
import com.thuc.bookings.service.client.PaymentsFeignClient;
import com.thuc.bookings.service.client.RoomTypesFeignClient;
import com.thuc.bookings.service.client.UsersFeignClient;
import com.thuc.bookings.utils.UploadCloudinary;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.containers.GenericContainer;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,properties = {
        "eureka.client.enabled=false",
        "spring.cloud.config.enabled=false"
})
@Testcontainers
public class BookingIntegrationTest {
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
    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:7.0.11-alpine")
            .withExposedPorts(6379);
    @Container
    static GenericContainer<?> rabbitmq = new GenericContainer<>("rabbitmq:3.11-management-alpine")
            .withExposedPorts(5672, 15672)
            .withEnv("RABBITMQ_DEFAULT_USER", "guest")
            .withEnv("RABBITMQ_DEFAULT_PASS", "guest");
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
        registry.add("spring.rabbitmq.host", rabbitmq::getHost);
        registry.add("spring.rabbitmq.port", () -> rabbitmq.getMappedPort(5672));
        registry.add("spring.rabbitmq.username", () -> "guest");
        registry.add("spring.rabbitmq.password", () -> "guest");
    }

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;

    @MockBean
    private PaymentsFeignClient paymentsFeignClient;

    @MockBean
    private RoomTypesFeignClient roomsFeignClient;

    @MockBean
    private UsersFeignClient usersFeignClient;

    @MockBean
    private UploadCloudinary uploadCloudinary;

    @Autowired
    private IRedisBookingService redisBookingService;

    @MockBean
    private StreamBridge streamBridge;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VehiclesRepository vehiclesRepository;

    @BeforeAll
    public static void init(){

        restTemplate = new RestTemplate();
    }
    BookingDto bookingDto;
    @BeforeEach
    public void setUp(){

        baseUrl= baseUrl + ":" + port +"/api/bookings";

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


    }
    @Test
    void testConfirmBooking() throws JsonProcessingException {
        // 1. Save booking to Redis
        redisBookingService.saveData("bookings-b123", bookingDto);
        BookingDto bookingDtoRedis = redisBookingService.getData("bookings-b123");
        // mock payment feign client
        PaymentResponseDto paymentResponseDto = PaymentResponseDto.builder()
                .billCode("123456")
                .paymentUrl("https://payment/123")
                .build();
        SuccessResponseDto<PaymentResponseDto> paymentResponse = SuccessResponseDto.<PaymentResponseDto>builder()
                .code(200)
                .message("success")
                .data(paymentResponseDto)
                .build();
        ResponseEntity<SuccessResponseDto<PaymentResponseDto>> paymentResponseEntity =
                ResponseEntity.ok(paymentResponse);
        Mockito.when(paymentsFeignClient.getUrl(ArgumentMatchers.any(BookingDto.class)))
                .thenReturn(paymentResponseEntity);
        doReturn(true).when(streamBridge).send(anyString(), ArgumentMatchers.any());

        SuccessResponseDto<List<Integer>> roomTypesResponse = SuccessResponseDto.<List<Integer>>builder()
                .code(200)
                .message("success")
                .data(List.of(101,102))
                .build();
        ResponseEntity<SuccessResponseDto<List<Integer>>> roomTypesResponseEntity =
                ResponseEntity.ok(roomTypesResponse);
        when(roomsFeignClient.availableRooms(ArgumentMatchers.any(BookingRoomTypeDto.class),ArgumentMatchers.anyInt()))
                .thenReturn(roomTypesResponseEntity);

        // 3. Gọi API
        ResponseEntity<SuccessResponseDto> response = restTemplate.getForEntity(
                baseUrl + "/confirm?uniqueCheck=b123",
                SuccessResponseDto.class
        );

        // 4. Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getData()).isEqualTo("https://payment/123");
    }

}
