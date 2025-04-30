package com.thuc.bookings.service.impl;

import com.thuc.bookings.converter.BillConverter;
import com.thuc.bookings.converter.BookingCarsConverter;
import com.thuc.bookings.converter.BookingRoomsConverter;
import com.thuc.bookings.dto.requestDto.BookingCarsRequestDto;
import com.thuc.bookings.dto.requestDto.BookingDto;
import com.thuc.bookings.dto.requestDto.BookingRoomTypeDto;
import com.thuc.bookings.dto.responseDto.PaymentResponseDto;
import com.thuc.bookings.dto.responseDto.SuccessResponseDto;
import com.thuc.bookings.entity.Bill;
import com.thuc.bookings.entity.BookingCars;
import com.thuc.bookings.entity.BookingRooms;
import com.thuc.bookings.entity.Vehicles;
import com.thuc.bookings.exception.ResourceNotFoundException;
import com.thuc.bookings.repository.BillRepository;
import com.thuc.bookings.repository.BookingCarsRepository;
import com.thuc.bookings.repository.BookingRoomsRepository;
import com.thuc.bookings.repository.VehiclesRepository;
import com.thuc.bookings.service.IBookingService;
import com.thuc.bookings.service.IRedisVehicleService;
import com.thuc.bookings.service.IVehiclesService;
import com.thuc.bookings.service.client.PaymentsFeignClient;
import com.thuc.bookings.service.client.RoomsFeignClient;
import com.thuc.bookings.service.client.UsersFeignClient;
import com.thuc.bookings.utils.BillStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements IBookingService {
    private final RoomsFeignClient roomsFeignClient;
    private final UsersFeignClient usersFeignClient;
    private final PaymentsFeignClient paymentsFeignClient;
    private final BookingCarsRepository bookingCarsRepository;
    private final BookingRoomsRepository bookingRoomsRepository;
    private final BillRepository billRepository;
    private final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);
    private final IRedisVehicleService redisVehicleService;
    private final VehiclesRepository vehiclesRepository;
    private final RedisTemplate<String,Object> redisTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public String confirm(BookingDto bookingDto) {
        ResponseEntity<SuccessResponseDto<PaymentResponseDto>> response = paymentsFeignClient.getUrl(bookingDto);
        PaymentResponseDto data =(PaymentResponseDto) Objects.requireNonNull(response.getBody().getData());
        Bill bill= billRepository.save(createBill(bookingDto,data.getBillCode()));
        for(BookingRoomTypeDto bookingRoomTypeDto: bookingDto.getRoomTypes()){
            List<Integer> roomNumbers = findRoomNumber(bookingRoomTypeDto,bookingDto.getPropertyId());
            BookingRooms bookingRooms= createBookingRooms(bookingRoomTypeDto,bill.getId(),roomNumbers);
            log.debug("----------------");
            log.debug("bookingRoomTypeDto : {}", bookingRoomTypeDto);
            log.debug("roomNumber : {}", roomNumbers);
            log.debug("bookingRooms : {}", bookingRooms);
            log.debug("---------------");
            bookingRoomsRepository.save(bookingRooms);
        }
        if(bookingDto.getBookingCars()!=null){
            for(BookingCarsRequestDto bookingCar : bookingDto.getBookingCars()){
                BookingCars bookingCars=createBookingCars(bookingCar,bill.getId());
                log.debug("bookingCars : {}", bookingCars);
                bookingCarsRepository.save(bookingCars);
            }
        }
        return data.getPaymentUrl();
    }

    @Override
    public void updateBillStatus(String billCode,BillStatus status) {
        Bill bill = billRepository.findByBillCode(billCode);
        bill.setBillStatus(status);
        billRepository.save(bill);
    }

    @Override
    public void removeHoldInRedis(String billCode) {
        Bill bill = billRepository.findByBillCode(billCode);
        if(bill==null){
            throw new ResourceNotFoundException("Bill","billCode",billCode);
        }
        String userEmail = bill.getUserEmail();
        List<BookingCars> vehicles = bookingCarsRepository.findByBillId(bill.getId());
        // xóa hold
        if(!vehicles.isEmpty()){
            for(BookingCars vehicle: vehicles){
                String key = String.format("%s:%d",userEmail,vehicle.getVehicleId());
                Vehicles existVehicle = vehiclesRepository.findById(vehicle.getVehicleId()).orElseThrow(
                        ()-> new ResourceNotFoundException("Vehicle","id",String.valueOf(vehicle.getVehicleId())));
                if(existVehicle.getQuantity()>1){
                    existVehicle.setQuantity(existVehicle.getQuantity()-1);
                    vehiclesRepository.save(existVehicle);
                }
                log.debug("100-key : {}", key);
                redisVehicleService.deleteData(key);
            }
        }
        List<BookingRooms> bookingRooms = bookingRoomsRepository.findByBillId(bill.getId());
        for(BookingRooms bookingRoom: bookingRooms){
            // xóa hold room
            redisTemplate.delete(String.format("hold|room|%d|%s|%s|%s",bookingRoom.getRoomTypeId()
                    ,userEmail,bookingRoom.getCheckIn(),bookingRoom.getCheckOut()));
            // khóa phòng đã đặt hiện tại

        }
    }

    private List<Integer> findRoomNumber(BookingRoomTypeDto bookingRoomTypeDto, int propertyId) {
        ResponseEntity<SuccessResponseDto<List<Integer>>> response = roomsFeignClient.availableRooms(bookingRoomTypeDto,propertyId);
        return (List<Integer> )response.getBody().getData();

    }


    private BookingRooms createBookingRooms(BookingRoomTypeDto bookingDto, int billId,List<Integer> roomNumbers) {
        return BookingRoomsConverter.toBookingRooms(bookingDto,billId,roomNumbers);
    }

    private BookingCars createBookingCars(BookingCarsRequestDto bookingDto,int billId) {
        return BookingCarsConverter.toBookingCars(bookingDto,billId);
    }

    private Bill createBill(BookingDto bookingDto,String billCode) {
        return BillConverter.toBill(bookingDto,billCode);
    }
}
