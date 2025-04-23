package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.DiscountCarsConverter;
import com.thuc.rooms.dto.DiscountCarsDto;
import com.thuc.rooms.dto.UserDiscountCarDto;
import com.thuc.rooms.entity.DiscountCars;
import com.thuc.rooms.entity.UserDiscountCars;
import com.thuc.rooms.exception.ResourceNotFoundException;
import com.thuc.rooms.repository.DiscountCarsRepository;
import com.thuc.rooms.repository.UserDiscountCarsRepository;
import com.thuc.rooms.service.IDiscountCarsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class DiscountCarsServiceImpl implements IDiscountCarsService {
    private final DiscountCarsRepository discountCarsRepository;
    private final UserDiscountCarsRepository userDiscountCarsRepository;
    @Override
    public List<DiscountCarsDto> getAllDiscountCars() {
        List<DiscountCars> discountCars = discountCarsRepository.findAll();
        return discountCars.stream().map(DiscountCarsConverter::toDiscountCarsDto).toList();
    }

    @Override
    public List<DiscountCarsDto> getAllMyDiscounts(String email) {
        List<UserDiscountCars> userDiscountCars = userDiscountCarsRepository.findByEmail(email);
        List<DiscountCarsDto> result = userDiscountCars.stream().map(userDiscountCar ->{
           DiscountCars discountCars = discountCarsRepository.findById(userDiscountCar.getId())
                   .orElseThrow(()-> new ResourceNotFoundException("DiscountCars","id",String.valueOf(userDiscountCar.getDiscountCarId())));
           return DiscountCarsConverter.toDiscountCarsDto(discountCars);
        }).toList();
        return result;
    }

    @Override
    public UserDiscountCarDto saveDiscount(UserDiscountCarDto userDiscountCarDto) {
        UserDiscountCars userDiscountCars = UserDiscountCars.builder()
                .discountCarId(userDiscountCarDto.getDiscountCarId())
                .email(userDiscountCarDto.getEmail())
                .build();
        userDiscountCarsRepository.save(userDiscountCars);
        return userDiscountCarDto;
    }
}
