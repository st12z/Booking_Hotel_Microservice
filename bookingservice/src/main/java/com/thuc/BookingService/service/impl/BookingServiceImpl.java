package com.thuc.BookingService.service.impl;

import com.thuc.BookingService.service.IBookingService;
import com.thuc.BookingService.service.client.RoomsFeignClient;
import com.thuc.BookingService.service.client.UsersFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements IBookingService {
    private final RoomsFeignClient roomsFeignClient;
    private final UsersFeignClient usersFeignClient;

}
