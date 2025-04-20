package com.thuc.BookingService.service.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="users")
public interface UsersFeignClient {
}
