package com.thuc.bookings.repository;

import com.thuc.bookings.entity.Bill;
import com.thuc.bookings.entity.BookingCars;
import com.thuc.bookings.entity.Vehicles;
import com.thuc.bookings.utils.BillStatus;
import com.thuc.bookings.utils.CarStatus;
import com.thuc.bookings.utils.CarType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@Testcontainers
@DataJpaTest
public class BookingCarsRepositoryTest {
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

    @Autowired
    private BillRepository billRepository;
    @Autowired
    private BookingCarsRepository bookingCarsRepository;

    @Autowired
    private VehiclesRepository vehiclesRepository;

    BookingCars bookingCarsOne;
    BookingCars bookingCarsTwo;
    Integer vehicleId;
    Integer billId;

    @BeforeEach
    void setUp() {

        Vehicles vehicle = Vehicles.builder()
                .carType(CarType.BUS)
                .licensePlate("123")
                .price(1000)
                .images("image.jpg")
                .discount(3)
                .status(CarStatus.AVAILABLE)
                .star(2)
                .quantity(2)
                .build();

        vehicleId = vehiclesRepository.save(vehicle).getId();

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

        bookingCarsOne = BookingCars.builder()
                .billId(billId)
                .vehicleId(vehicleId)
                .priceBooking(1000)
                .build();
        bookingCarsTwo = BookingCars.builder()
                .billId(billId)
                .vehicleId(vehicleId)
                .priceBooking(2000)
                .build();
    }
    @Test
    @DisplayName("It should be return create booking car")
    public void testCreateBookingCar(){
        BookingCars newBookingCar = bookingCarsRepository.save(bookingCarsOne);
        assertThat(newBookingCar).isNotNull();
    }
    @Test
    @DisplayName("It should be return list booking cars by property id")
    public void testGetListBookingCarsByPropertyId() {
        bookingCarsRepository.save(bookingCarsOne);
        bookingCarsRepository.save(bookingCarsTwo);
        List<BookingCars> list= bookingCarsRepository.findByBillId(billId);
        assertThat(list.size()).isEqualTo(2);
    }

}
