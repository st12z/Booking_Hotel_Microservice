package com.thuc.rooms.service.impl;


import com.thuc.rooms.converter.DiscountConverter;
import com.thuc.rooms.dto.DiscountDto;
import com.thuc.rooms.dto.UserDiscountDto;
import com.thuc.rooms.entity.Discount;
import com.thuc.rooms.entity.UserDiscount;
import com.thuc.rooms.exception.BadRequestCustomException;
import com.thuc.rooms.exception.ResourceAlreadyExistsException;
import com.thuc.rooms.exception.ResourceNotFoundException;
import com.thuc.rooms.repository.DiscountRepository;
import com.thuc.rooms.repository.UserDiscountRepository;
import com.thuc.rooms.service.IDiscountService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements IDiscountService {
    private final DiscountRepository discountRepository;
    private final Logger log = LoggerFactory.getLogger(DiscountServiceImpl.class);
    private final StreamBridge streamBridge;
    private final RedissonClient redissonClient;
    private final UserDiscountRepository userDiscountRepository;    @Override
    public List<DiscountDto> getAllDiscounts() {
        log.debug("Request to get all Discounts successfully");
        List<Discount> discounts = discountRepository.findAll();
        return discounts.stream().map(DiscountConverter::toDiscountDto).toList();
    }



    @Override
    public UserDiscountDto saveDiscount(UserDiscountDto userDiscountDto) {
        log.debug("19-save discount with {}", userDiscountDto);
        handleSaveDiscount(userDiscountDto);
        return userDiscountDto;
    }

    @Override
    public List<DiscountDto> getMyDiscountsByEmail(String email) {
        List<UserDiscount> myDiscounts = userDiscountRepository.findByEmail(email);
        return myDiscounts.stream().map(userDiscount -> {
            int discountId= userDiscount.getDiscountId();
            Discount discount = getDiscountById(discountId);
            return DiscountConverter.toDiscountDto(discount);
        }).toList();
    }

    private void handleSaveDiscount(UserDiscountDto userDiscountDto) {
        int discountId=userDiscountDto.getDiscountId();
        String email = userDiscountDto.getEmail();
        RLock lock = redissonClient.getLock(String.format("save:%d", discountId));
        boolean isLocked = false;
        try{
            isLocked = lock.tryLock(10,10, TimeUnit.SECONDS);
            log.debug("Email :{}",email);
            log.debug("isLocked:{}", isLocked);
            log.debug("ThreadName:{}", Thread.currentThread().getName());
            if(isLocked){
                boolean check = handleCheckQuantity(userDiscountDto);
                if(check){
                    UserDiscount existDiscount = userDiscountRepository.
                            findByDiscountIdAndEmail(discountId,email);
                    if(existDiscount!=null){
                        throw new ResourceAlreadyExistsException("UserDiscount","discountId-email",
                                String.format("%d-%s",discountId,email));
                    }
                    UserDiscount userDiscount = UserDiscount.builder()
                            .email(email)
                            .discountId(discountId)
                            .build();
                    userDiscountRepository.save(userDiscount);
                }
                else{
                    throw new BadRequestCustomException("Discount is sold out");
                }
            }
        }catch (InterruptedException e){
            throw new RuntimeException("Failure acquire lock ",e);
        }finally {
            if(isLocked && lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }

    }

    private boolean handleCheckQuantity(UserDiscountDto userDiscountDto) {
        Discount discount = getDiscountById(userDiscountDto.getDiscountId());
        int quantity = discount.getQuantity();
        log.debug("current quantity:{}", quantity);
        if(quantity>0){
            discount.setQuantity(quantity-1);
            discountRepository.save(discount);
            return true;
        }
        return false;
    }

    private Discount getDiscountById( int discountId) {
        return discountRepository.findById(discountId)
                .orElseThrow(() -> new ResourceNotFoundException("Discount","id",String.valueOf(discountId) ));
    }
}
