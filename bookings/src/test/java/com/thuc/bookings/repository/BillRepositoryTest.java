package com.thuc.bookings.repository;

import com.thuc.bookings.entity.Bill;
import com.thuc.bookings.utils.BillStatus;
import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
public class BillRepositoryTest {
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

    Bill billOne;
    Bill billTwo;
    @BeforeEach
    public void setUp()  {
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
    @Autowired
    public BillRepository billRepository;
    @Test
    @DisplayName("It should be create create bill")
    public void testCreateBill(){
        Bill createBill = billRepository.save(billOne);
        assertThat(createBill).isNotNull();
        assertThat(createBill.getId()).isNotNull();
    }
    @Test
    @DisplayName("It should be return bill")
    public void testFindBillByBillCode(){
        Bill createBill = billRepository.save(billOne);
        Bill findBill = billRepository.findByBillCode(createBill.getBillCode());
        assertThat(findBill).isNotNull();
        assertThat(findBill.getId()).isNotNull();
    }
    @Test
    @DisplayName("It should be return count bill")
    public void testCountByCreatedAtAndBillStatus(){
        Bill createBill = billRepository.save(billOne);
        LocalDateTime start = createBill.getCreatedAt().minusSeconds(1);
        LocalDateTime end = createBill.getCreatedAt().plusSeconds(1);
        int count = billRepository.countByCreatedAtAndBillStatus(start, end,BillStatus.SUCCESS);
        assertThat(count).isEqualTo(1);
    }
    @Test
    @DisplayName("It should be return total ")
    public void testTotalPayment(){
        Bill createBill = billRepository.save(billOne);
        LocalDateTime start = createBill.getCreatedAt().minusSeconds(1);
        LocalDateTime end = createBill.getCreatedAt().plusSeconds(1);
        Integer total = billRepository.getTotalPaymentToday(start, end);
        assertThat(total).isNotNull();
        assertThat(total).isEqualTo(500);
    }
    @Test
    @DisplayName("It should be return page<bill> when find keyword of email")
    public void testFindBillByKeyword(){
        Pageable pageable = PageRequest.of(0,10);
        billRepository.save(billOne);
        billRepository.save(billTwo);
        Page<Bill> bills = billRepository.findByKeyword("thuc@gmail.com","%12%",pageable);
        assertThat(bills).isNotNull();
        assertThat(bills.getContent().size()).isEqualTo(2);
    }
    @Test
    @DisplayName("It should be return count bill by property id")
    public void testCountBillByPropertyId(){
        billRepository.save(billOne);
        billRepository.save(billTwo);
        Integer count = billRepository.countByPropertyId(1);
        assertThat(count).isEqualTo(2);
    }
    @Test
    @DisplayName("It should be return list bill by property id")
    public void testListBillByPropertyId(){
        billRepository.save(billOne);
        billRepository.save(billTwo);
        List<Bill> bills = billRepository.findByPropertyId(1);
        assertThat(bills).isNotNull();
        assertThat(bills.size()).isEqualTo(2);
    }
    @AfterEach
    public void tearDown() {
        billRepository.deleteAll();
    }
}
