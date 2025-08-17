package com.thuc.bookings.service;

import com.thuc.bookings.dto.requestDto.BookingCarsRequestDto;
import com.thuc.bookings.dto.requestDto.BookingRoomTypeDto;
import com.thuc.bookings.dto.requestDto.FilterBillsDto;
import com.thuc.bookings.dto.responseDto.BookingDto;
import com.thuc.bookings.dto.responseDto.PageResponseDto;
import com.thuc.bookings.dto.responseDto.PaymentResponseDto;
import com.thuc.bookings.dto.responseDto.SuccessResponseDto;
import com.thuc.bookings.entity.Bill;
import com.thuc.bookings.entity.BookingCars;
import com.thuc.bookings.entity.BookingRooms;
import com.thuc.bookings.entity.Vehicles;
import com.thuc.bookings.repository.BillRepository;
import com.thuc.bookings.repository.BookingCarsRepository;
import com.thuc.bookings.repository.BookingRoomsRepository;
import com.thuc.bookings.repository.VehiclesRepository;
import com.thuc.bookings.service.client.PaymentsFeignClient;
import com.thuc.bookings.service.client.RoomTypesFeignClient;
import com.thuc.bookings.service.impl.BookingServiceImpl;
import com.thuc.bookings.utils.BillStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    private RoomTypesFeignClient roomTypesFeignClient;

    @Mock
    private PaymentsFeignClient paymentsFeignClient;

    @Mock
    private BookingCarsRepository bookingCarsRepository;

    @Mock
    private BookingRoomsRepository bookingRoomsRepository;

    @Mock
    private BillRepository billRepository;

    @Mock
    private IRedisVehicleService redisVehicleService;

    @Mock
    private VehiclesRepository vehiclesRepository;

    @Mock
    private RedisTemplate<String,Object> redisTemplate;

    @Mock
    private IRedisBookingService redisBookingService;

    @Mock
    private StreamBridge streamBridge;

    @Mock
    @PersistenceContext
    private EntityManager entityManager;

    @InjectMocks
    private BookingServiceImpl bookingService;

    Bill billOne;
    Bill billTwo;
    BookingDto bookingDto;
    BookingCars bookingCars;
    Vehicles vehicle;
    BookingRooms bookingRoom;
    @BeforeEach
    void setUp() {
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

        billOne = Bill.builder()
                .id(1)
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
    @DisplayName("It should be return confirm booking")
    public void testConfirmBooking() {
        when(redisBookingService.getData(any())).thenReturn(bookingDto);
        ResponseEntity<SuccessResponseDto<PaymentResponseDto>> responsePaymentClient = ResponseEntity.ok(
                SuccessResponseDto.<PaymentResponseDto>builder()
                        .code(200)
                        .message("success")
                        .data(PaymentResponseDto.builder()
                                .billCode(billOne.getBillCode())
                                .paymentUrl("https://payment")
                                .build())
                        .build()
        );
        ResponseEntity<SuccessResponseDto<List<Integer>>> responseRoomTypesClient = ResponseEntity.ok(
                SuccessResponseDto.<List<Integer>>builder()
                        .code(200)
                        .message("success")
                        .data(List.of(101,102))
                        .build()
        );
        when(billRepository.save(any(Bill.class))).thenReturn(billOne);
        when(roomTypesFeignClient.availableRooms(any(BookingRoomTypeDto.class),anyInt())).thenReturn(responseRoomTypesClient);
        when(paymentsFeignClient.getUrl(any())).thenReturn(responsePaymentClient);
        when(streamBridge.send(anyString(),any())).thenReturn(true);
        //when
        String url = bookingService.confirm("123");

        //then
        assertEquals("https://payment",url);
        verify(billRepository,times(1)).save(any());
        verify(bookingRoomsRepository,times(1)).save(any());
        verify(bookingCarsRepository,times(1)).save(any());
    }
    @Test
    @DisplayName("It should remove in redis")
    public void testRemoveInRedis() {
        //given
        when(billRepository.findByBillCode(anyString())).thenReturn(billOne);
        when(bookingCarsRepository.findByBillId(anyInt())).thenReturn(List.of(bookingCars));
        when(vehiclesRepository.findById(anyInt())).thenReturn(Optional.of(vehicle));
        when(bookingRoomsRepository.findByBillId(anyInt())).thenReturn(List.of(bookingRoom));
        //when
        bookingService.removeHoldInRedis("123");
        //then
        verify(vehiclesRepository,times(1)).save(any());
        verify(redisVehicleService).deleteData(anyString());
        verify(redisTemplate).delete(anyString());
        verify(roomTypesFeignClient).confirmBookingRooms(any(),any(),any());
    }
}
