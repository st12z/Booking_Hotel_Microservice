package com.thuc.BookingService.service.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="rooms")
public interface RoomsFeignClient {
}
