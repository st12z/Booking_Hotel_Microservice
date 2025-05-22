package com.thuc.payments.config;

import com.thuc.payments.entity.PaymentTransaction;
import com.thuc.payments.utils.VnpayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.*;

@Data
@Configuration
public class VnpayRefundConfig {
    @Value("${refund.vnpay.vnp_RefundUrl}")
    private String vnp_RefundUrl;

    @Value("${refund.vnpay.vnp_Version}")
    private String vnp_Version;

    @Value("${refund.vnpay.vnp_Command}")
    private String vnp_Command;

    @Value("${refund.vnpay.vnp_TmnCode}")
    private String vnp_TmnCode;

    @Value("${refund.vnpay.secretKey}")
    private String secretKey;

    @Value("${payment.vnpay.orderType}")
    private String orderType;
    public LinkedHashMap<String,String> getVNPayRefundConfig(HttpServletRequest request, PaymentTransaction paymentTransaction,int rate) {
        String email = request.getHeader("X-User-Email");
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = sdf.format(cal.getTime());
        String ipAddress = VnpayUtil.getIpAddress(request);
        LinkedHashMap<String,String> config = new LinkedHashMap<>();
        config.put("vnp_RequestId",VnpayUtil.getRandomString(8));
        config.put("vnp_Version", vnp_Version);
        config.put("vnp_Command", vnp_Command);
        config.put("vnp_TmnCode", vnp_TmnCode);
        config.put("vnp_TransactionType",String.format("%02d",rate));
        config.put("vnp_TxnRef",paymentTransaction.getVnpTxnRef());
        int amount = paymentTransaction.getVnpAmount();
        if(rate==3) config.put("vnp_Amount",String.valueOf(amount*100/2));
        else config.put("vnp_Amount",String.valueOf(amount*100));
        config.put("vnp_TransactionNo",paymentTransaction.getVnpTransactionNo());
        config.put("vnp_TransactionDate",paymentTransaction.getVnpTransactionDate());
        config.put("vnp_createBy",email);
        config.put("vnp_CreateDate",vnpCreateDate);
        config.put("vnp_IpAddr",ipAddress);
        config.put("vnp_OrderInfo","Refund");

        return config;
    }
}