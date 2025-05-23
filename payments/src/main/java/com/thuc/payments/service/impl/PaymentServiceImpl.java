package com.thuc.payments.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thuc.payments.config.VnpayConfig;
import com.thuc.payments.config.VnpayRefundConfig;
import com.thuc.payments.dto.BookingDto;
import com.thuc.payments.dto.PaymentResponseDto;
import com.thuc.payments.dto.RefundBillDto;
import com.thuc.payments.dto.VnpayRefundResponseDto;
import com.thuc.payments.entity.PaymentTransaction;
import com.thuc.payments.exception.ResourceNotFoundException;
import com.thuc.payments.repository.PaymentTransactionRepository;
import com.thuc.payments.service.IPaymentService;
import com.thuc.payments.utils.TransactionType;
import com.thuc.payments.utils.VnpayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements IPaymentService {
    private final VnpayConfig vnpayConfig;
    private final VnpayRefundConfig vnpayRefundConfig;
    private final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);
    private final StreamBridge streamBridge;
    @Override
    public PaymentResponseDto getUrlPayment(HttpServletRequest request, BookingDto bookingDto) {
        Map<String,String> params = vnpayConfig.getVNPayConfig(request);
        log.debug("params: params={}", params);
        int amount = bookingDto.getNewTotalPayment();
        params.put("vnp_Amount", String.valueOf(amount*100L));

        //Billing
        params.put("vnp_Bill_Mobile",bookingDto.getPhoneNumber());
        params.put("vnp_Bill_Email",bookingDto.getEmail());
        params.put("vnp_Bill_FirstName", bookingDto.getFirstName());
        params.put("vnp_Bill_LastName", bookingDto.getLastName());
        params.put("vnp_Bill_Address",bookingDto.getAddressDetail());
        params.put("vnp_Bill_City", bookingDto.getCity());
        params.put("vnp_Bill_Country", bookingDto.getCountry());
        log.debug("getUrlPayment: params={}", params);
        List fieldNames = new ArrayList(params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash= VnpayUtil.hmacSHA512(vnpayConfig.getSecretKey(), hashData.toString());
        queryUrl+= "&vnp_SecureHash="+vnp_SecureHash;
        String paymentUrl = vnpayConfig.getVnp_PayUrl() +"?"+ queryUrl;
        return PaymentResponseDto.builder()
                .billCode(params.get("vnp_TxnRef"))
                .paymentUrl(paymentUrl)
                .build();
    }

    @Override
    public VnpayRefundResponseDto refund(HttpServletRequest request, String billCode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();
            String email = request.getHeader("X-User-Email");
            PaymentTransaction paymentTransaction = paymentTransactionRepository.
                    findByVnpTxnRefAndTransactionType(billCode,TransactionType.PAYMENT);
            if (paymentTransaction == null) {
                throw new ResourceNotFoundException("PaymentTransaction", "vnpTxnRef", billCode);
            }
            long days = ChronoUnit.DAYS.between(paymentTransaction.getCreatedAt(), LocalDateTime.now());
            if(days>3){
                throw new RuntimeException("Payment transaction refund time is too long");
            }
            LinkedHashMap<String, String> params = new LinkedHashMap<>();
            if(days<=2){
                params = vnpayRefundConfig.getVNPayRefundConfig(request,paymentTransaction,2);
            }
            else{
                params = vnpayRefundConfig.getVNPayRefundConfig(request,paymentTransaction,3);
            }
            List fieldNames = new ArrayList(params.keySet());
            StringBuilder hashData = new StringBuilder();
            Iterator itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) params.get(fieldName);
                if ("vnp_SecureHash".equals(fieldName)) continue;
                if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                    // Build hash data
                    hashData.append(fieldValue);
                    if (itr.hasNext()) {
                        hashData.append('|');
                    }
                }
            }
            log.debug("hashData: hashData={}", hashData.toString());
            String vnp_SecureHash = VnpayUtil.hmacSHA512(vnpayConfig.getSecretKey(), hashData.toString());
            params.put("vnp_SecureHash", vnp_SecureHash);
            logger.debug("params: params={}", params);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(params, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(
                    vnpayRefundConfig.getVnp_RefundUrl(),
                    requestEntity,
                    String.class
            );


            String responseBody = response.getBody();
            VnpayRefundResponseDto vnpayRefundResponseDto = objectMapper.readValue(responseBody, VnpayRefundResponseDto.class);
            PaymentTransaction existPaymentTransaction = paymentTransactionRepository.findByVnpTxnRefAndTransactionType(paymentTransaction.getVnpTxnRef(), TransactionType.REFUND);
            if(existPaymentTransaction==null &&vnpayRefundResponseDto.getVnp_ResponseCode().equals("00")){
                PaymentTransaction paymentTransactionRefund = PaymentTransaction.builder()
                        .vnpTransactionNo(vnpayRefundResponseDto.getVnp_TransactionNo())
                        .vnpAmount(vnpayRefundResponseDto.getVnp_Amount()/100)
                        .vnpResponseCode(vnpayRefundResponseDto.getVnp_ResponseCode())
                        .vnpTransactionDate(vnpayRefundResponseDto.getVnp_PayDate())
                        .vnpTxnRef(vnpayRefundResponseDto.getVnp_TxnRef())
                        .transactionType(TransactionType.REFUND)
                        .build();
                RefundBillDto refundBillDto = RefundBillDto.builder()
                        .vnp_Command(vnpayRefundResponseDto.getVnp_Command())
                        .vnp_BankCode(vnpayRefundResponseDto.getVnp_BankCode())
                        .vnp_TransactionNo(paymentTransaction.getVnpTransactionNo())
                        .vnp_ResponseId(vnpayRefundResponseDto.getVnp_ResponseId())
                        .vnp_TmnCode(vnpayRefundResponseDto.getVnp_TmnCode())
                        .vnp_Amount(vnpayRefundResponseDto.getVnp_Amount()/100)
                        .vnp_Message(vnpayRefundResponseDto.getVnp_Message())
                        .vnp_TransactionStatus(vnpayRefundResponseDto.getVnp_TransactionStatus())
                        .vnp_OrderInfo(vnpayRefundResponseDto.getVnp_OrderInfo())
                        .vnp_PayDate(vnpayRefundResponseDto.getVnp_PayDate())
                        .vnp_ResponseCode(vnpayRefundResponseDto.getVnp_ResponseCode())
                        .vnp_SecureHash(vnpayRefundResponseDto.getVnp_SecureHash())
                        .vnp_TransactionType(vnpayRefundResponseDto.getVnp_TransactionType())
                        .vnp_TxnRef(vnpayRefundResponseDto.getVnp_TxnRef())
                        .email(email)
                        .build();
                paymentTransactionRepository.save(paymentTransactionRefund);
                log.debug("send refund callback :{}", refundBillDto);
                var result = streamBridge.send("sendRefund-out-0",refundBillDto);
                log.debug("Receive payment callback :{}",result);
            }
            return VnpayRefundResponseDto.builder()
                    .vnp_ResponseCode(vnpayRefundResponseDto.getVnp_ResponseCode())
                    .vnp_Message(vnpayRefundResponseDto.getVnp_Message())
                    .vnp_Amount(vnpayRefundResponseDto.getVnp_Amount()/100)
                    .vnp_TransactionStatus(vnpayRefundResponseDto.getVnp_TransactionStatus())
                    .vnp_OrderInfo(vnpayRefundResponseDto.getVnp_OrderInfo())
                    .vnp_PayDate(vnpayRefundResponseDto.getVnp_PayDate())
                    .vnp_TxnRef(vnpayRefundResponseDto.getVnp_TxnRef())
                    .vnp_TransactionType(vnpayRefundResponseDto.getVnp_TransactionType())
                    .build();
    }
}
