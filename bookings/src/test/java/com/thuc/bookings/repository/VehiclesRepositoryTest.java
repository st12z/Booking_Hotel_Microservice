package com.thuc.bookings.repository;

import com.thuc.bookings.entity.Vehicles;
import com.thuc.bookings.utils.CarStatus;
import com.thuc.bookings.utils.CarType;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@DataJpaTest
public class VehiclesRepositoryTest {
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
    private VehiclesRepository vehiclesRepository;
    Vehicles vehicle;
    @BeforeEach
    public void setUp() {
        vehicle = Vehicles.builder()
                .carType(CarType.BUS)
                .licensePlate("123")
                .price(1000)
                .images("image.jpg")
                .discount(3)
                .status(CarStatus.AVAILABLE)
                .star(2)
                .quantity(2)
                .build();
    }
    @Test
    @DisplayName("It should be return create vehicle")
    public void testCreateVehicle(){
        Vehicles newVehicle = vehiclesRepository.save(vehicle);
        assertThat(newVehicle).isNotNull();
    }
    @Test
    @DisplayName("It should be return vehicle by licensePlate")
    public void testFindByLicensePlate(){
        Vehicles newVehicle = vehiclesRepository.save(vehicle);
        Vehicles findVehicles = vehiclesRepository.findByLicensePlate(newVehicle.getLicensePlate());
        assertThat(findVehicles).isNotNull();
    }
}
