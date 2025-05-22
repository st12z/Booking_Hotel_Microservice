package com.thuc.payments.config;

import com.thuc.payments.utils.VnpayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Data
@Configuration
public class VnpayConfig {
    @Value("${payment.vnpay.vnp_PayUrl}")
    private String vnp_PayUrl;

    @Value("${payment.vnpay.vnp_ReturnUrl}")
    private String vnp_ReturnUrl;

    @Value("${payment.vnpay.vnp_TmnCode}")
    private String vnp_TmnCode;

    @Value("${payment.vnpay.secretKey}")
    private String secretKey;

    @Value("${payment.vnpay.vnp_Version}")
    private String vnp_Version;

    @Value("${payment.vnpay.vnp_Command}")
    private String vnp_Command;

    @Value("${payment.vnpay.orderType}")
    private String orderType;
    public Map<String,String> getVNPayConfig(HttpServletRequest request) {
        Map<String,String> config = new HashMap<String,String>();
        config.put("vnp_ReturnUrl", vnp_ReturnUrl);
        config.put("vnp_TmnCode", vnp_TmnCode);
        config.put("vnp_Version", vnp_Version);
        config.put("vnp_Command", vnp_Command);
        config.put("vnp_OrderType", orderType);
        config.put("vnp_CurrCode","VND");
        config.put("vnp_TxnRef", VnpayUtil.getRandomString(8));
        config.put("vnp_OrderInfo","Payment for booking:");
        config.put("vnp_Locale","vn");
        config.put("vnp_BankCode","NCB");
        String ipAddress = VnpayUtil.getIpAddress(request);
        config.put("vnp_IpAddr", ipAddress);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = sdf.format(cal.getTime());
        config.put("vnp_CreateDate", vnpCreateDate);
        cal.add(Calendar.MINUTE,10);
        String vnpExpireDate = sdf.format(cal.getTime());
        config.put("vnp_ExpireDate", vnpExpireDate);
        return config;
    }
}