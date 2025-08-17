package com.thuc.bookings.repository;

import com.thuc.bookings.entity.RefundBill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Testcontainers
@DataJpaTest
public class RefundBillRepositoryTest {
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
    @Autowired
    private BillRepository billRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    @Autowired
    private RefundBillRepository refundBillRepository;
    private RefundBill refundBillOne;
    @BeforeEach
    public void setUp() {
        refundBillOne = RefundBill.builder()
                .vnp_BankCode("123")
                .vnp_Command("123")
                .vnp_Message("ok")
                .vnp_OrderInfo("ok")
                .vnp_PayDate(LocalDateTime.now().toString())
                .vnp_ResponseCode("00")
                .vnp_ResponseId("00")
                .vnp_SecureHash("1234")
                .vnp_TmnCode("123")
                .vnp_TxnRef("123")
                .vnp_Amount(1000)
                .vnp_TransactionStatus("00")
                .vnp_TransactionType("")
                .vnp_TransactionNo("123")
                .email("thuc@gmail.com")
                .build();
    }
    @Test
    @DisplayName("It should be test create refund bill")
    public void testCreateRefundBill() {
        //when
        RefundBill savedRefundBill = refundBillRepository.save(refundBillOne);
        //then
        assertThat(savedRefundBill.getId()).isNotNull();
    }
    @Test
    @DisplayName("It should be test getTotalRefundToday")
    public void testGetTotalRefundToday() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        //given
        RefundBill savedRefundBill = refundBillRepository.save(refundBillOne);
        //when
        Integer total = refundBillRepository.getTotalRefundToday(startOfDay,endOfDay);
        //then
        assertThat(total).isEqualTo(1000);
    }
    @Test
    @DisplayName("It should be test findByVnpTxnRef")
    public void testFindByVnpTxnRef() {
        //given
        RefundBill savedRefundBill = refundBillRepository.save(refundBillOne);
        //when
        RefundBill refundBill = refundBillRepository.findByVnpTxnRef(savedRefundBill.getVnp_TxnRef());
        //then
        assertThat(refundBill).isNotNull();
        assertThat(refundBill.getVnp_TxnRef()).isEqualTo("123");
    }
    @Test
    @DisplayName("It should be test findByCreatedAtBetween")
    public void testFindByCreatedAtBetween() {
        RefundBill savedRefundBill = refundBillRepository.save(refundBillOne);
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay().minusDays(1);
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX).plusDays(1);
        //when
        List<RefundBill> refundBills = refundBillRepository.findByCreatedAtBetween(startOfDay,endOfDay);
        //then
        assertThat(refundBills.size()).isEqualTo(1);
    }
}
