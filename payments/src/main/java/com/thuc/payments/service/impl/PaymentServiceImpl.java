package com.thuc.bookings.service.impl;

import com.thuc.bookings.config.VnpayConfig;
import com.thuc.bookings.dto.requestDto.BookingDto;
import com.thuc.bookings.service.IPaymentService;
import com.thuc.bookings.utils.VnpayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements IPaymentService {
    private final VnpayConfig vnpayConfig;
    private final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);
    @Override
    public String getUrlPayment(HttpServletRequest request,BookingDto bookingDto) {
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
        return paymentUrl;
    }
}
