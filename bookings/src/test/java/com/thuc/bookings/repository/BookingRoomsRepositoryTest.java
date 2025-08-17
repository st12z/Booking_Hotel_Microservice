package com.thuc.bookings.repository;

import com.thuc.bookings.entity.Bill;
import com.thuc.bookings.entity.BookingRooms;
import com.thuc.bookings.utils.BillStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@Testcontainers
@DataJpaTest   
public class BookingRoomsRepositoryTest {
    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(
                    DockerImageName.parse("postgis/postgis:15-3.3-alpine")
                            .asCompatibleSubstituteFor("postgres")
            )
                    .withDatabaseName("testdb")
                    .withUsername("postgres")
                    .withPassword("123456")
                    .withCommand("postgres", "-c", "timezone=Asia/Ho_Chi_Minh")
                    .withEnv("TZ", "Asia/Ho_Chi_Minh")
                    .withInitScript("init.sql");
    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BookingRoomsRepository bookingRoomsRepository;

    Integer billId;
    BookingRooms bookingRoomsOne;
    BookingRooms bookingRoomsTwo;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    public void setUp() {

        Bill bill = Bill.builder()
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

        billId =billRepository.save(bill).getId();

        bookingRoomsOne = BookingRooms.builder()
                .roomTypeId(1)
                .quantityRooms(1)
                .billId(billId)
                .checkIn(LocalDateTime.now().minusDays(1))
                .checkOut(LocalDateTime.now().plusDays(1))
                .dayStays(1)
                .originPayment(1000)
                .promotion(500)
                .newPayment(500)
                .propertyId(1)
                .build();
    }
    @Test
    @DisplayName("It should be create booking rooms")
    public void testCreateBookingRooms() {
        BookingRooms bookingRoomsCreated = bookingRoomsRepository.save(bookingRoomsOne);
        assertThat(bookingRoomsCreated).isNotNull();
    }
    @Test
    @DisplayName("It should be return list booking rooms by roomTypeId and propertyId")
    public void testListBookingRoomsByRoomTypeId() {
        BookingRooms bookingRoomsCreated = bookingRoomsRepository.save(bookingRoomsOne);
        List<BookingRooms> list = bookingRoomsRepository.findByRoomTypeIdAndPropertyId(1,1);
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(1);
    }
    @Test
    @DisplayName("It should be return booking rooms by property id")
    public void testReturnBookingRoomsByPropertyId() {
        bookingRoomsRepository.save(bookingRoomsOne);
        List<BookingRooms> list = bookingRoomsRepository.findByPropertyId(1);
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(1);
    }
}
