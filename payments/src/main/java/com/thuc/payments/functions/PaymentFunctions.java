package com.thuc.payments.functions;

import com.thuc.payments.dto.SuspiciousUpdateDto;
import com.thuc.payments.entity.SuspiciousPaymentLog;
import com.thuc.payments.exception.ResourceNotFoundException;
import com.thuc.payments.repository.SuspiciousPaymentLogRepository;
import com.thuc.payments.service.IRedisPrimitive;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class PaymentFunctions {
    private final Logger logger = LoggerFactory.getLogger(PaymentFunctions.class);
    private final IRedisPrimitive redisPrimitive;
    private final SuspiciousPaymentLogRepository suspiciousPaymentLogRepository;
    @Bean
    public Function<SuspiciousUpdateDto,String> sendUpdateSuspiciousTran(){
        return suspiciousUpdateDto->{
            logger.info("sendUpdateSuspiciousTran :{}",suspiciousUpdateDto);
            String uniqueCheck = suspiciousUpdateDto.getUniqueCheck();
            Integer id = redisPrimitive.getData(String.format("suspiciousPaymentTranId-%s",uniqueCheck),Integer.class);
            if(id!=null){
                SuspiciousPaymentLog suspiciousPaymentLog = suspiciousPaymentLogRepository
                        .findById(id).orElseThrow(()-> new ResourceNotFoundException("SuspiciousPaymentLog","id",String.valueOf(id)));
                suspiciousPaymentLog.setBillCode(suspiciousUpdateDto.getBillCode());
                SuspiciousPaymentLog savedSuspiciousPaymentLog=suspiciousPaymentLogRepository.save(suspiciousPaymentLog);
                logger.debug("new SuspiciousPaymentLog saved :{}",savedSuspiciousPaymentLog);
            }
            return "Update billCode of SuspiciousPaymentLog success";
        };
    }
}
