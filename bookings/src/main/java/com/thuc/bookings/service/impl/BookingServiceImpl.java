package com.thuc.bookings.service.impl;

import com.thuc.bookings.service.IBookingService;
import com.thuc.bookings.service.client.RoomsFeignClient;
import com.thuc.bookings.service.client.UsersFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements IBookingService {
    private final RoomsFeignClient roomsFeignClient;
    private final UsersFeignClient usersFeignClient;

}
