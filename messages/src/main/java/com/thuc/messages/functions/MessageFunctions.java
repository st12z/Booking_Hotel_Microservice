package com.thuc.messages.functions;

import com.thuc.messages.dto.BillDto;
import com.thuc.messages.dto.RefundBillDto;
import com.thuc.messages.dto.UserDto;
import com.thuc.messages.utils.CustomMailSender;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class MessageFunctions {
    private static final Logger log = LoggerFactory.getLogger(MessageFunctions.class);
    private final CustomMailSender mailSender;
    @Bean
    public Function<UserDto, String> sendEmailCreateAccount(){
        return userDto->{
            try{
                log.debug("send email with userDto={}", userDto.toString());
                String subject = "Tạo tài khoản";
                String content = "<h3>Tạo tại khoản thành công</h3>" +
                        "<h3>Email:" +userDto.email()+"<h3/>"+
                        "<h3>Mật khẩu:"+userDto.password()+"<h3/>"
                        ;
                mailSender.sendMail(userDto.email(),content,subject);
                return userDto.email();
            }catch (Exception e){
                log.debug("send email failed with {}", userDto.toString());
                throw new RuntimeException(e);
            }
        };
    };
    @Bean
    public Function<BillDto,String> sendEmailConfirmBill(){
        return billDto->{
          try {
              log.debug("send email with billDto={}", billDto.toString());
              String subject = "Hóa đơn đặt phòng";
              String content = "<h3>Thông tin chi tiết về hóa đơn đặt phòng" +
                      "<table border='1'>\n" +
                      "    <thead>\n" +
                      "      <tr>\n" +
                      "        <th>Mã hóa đơn</th>\n" +
                      "        <th>Họ tên</th>\n" +
                      "        <th>Tên</th>\n" +
                      "        <th>Email</th>\n" +
                      "        <th>Số điện thoại</th>\n" +
                      "        <th>Huyện</th>\n" +
                      "        <th>Thành phố</th>\n" +
                      "        <th>Quốc gia</th>\n" +
                      "        <th>Địa chỉ chi tiết</th>\n" +
                      "        <th>Tổng tiền</th>\n" +
                      "      </tr>\n" +
                      "    </thead>\n" +
                      "    <tbody>\n" +
                      "      <tr>\n" +
                      "        <td>" + billDto.billCode() + "</td>\n" +
                      "        <td>" + billDto.firstName() + "</td>\n" +
                      "        <td>" + billDto.lastName() + "</td>\n" +
                      "        <td>" + billDto.email() + "</td>\n" +
                      "        <td>" + billDto.phoneNumber() + "</td>\n" +
                      "        <td>" + billDto.district() + "</td>\n" +
                      "        <td>" + billDto.city() + "</td>\n" +
                      "        <td>" + billDto.country() + "</td>\n" +
                      "        <td>" + billDto.addressDetail() + "</td>\n" +
                      "        <td>" + billDto.originTotalPayment() + "</td>\n" +
                      "        <tr>\n" +
                      "  </table>";
              mailSender.sendMail(billDto.email(),content,subject);
              return "Gửi mail thành công";
          }catch (Exception e){
              log.debug("send email failed with {}", billDto.toString());
              throw new RuntimeException(e);
          }
        };
    }
    @Bean
    public Function<RefundBillDto,String> sendEmailRefundBill(){
        return refundBillDto->{
            try {
                log.debug("send email with billDto={}", refundBillDto.toString());
                String subject = "Thông tin hủy phòng";
                String content = "<h3>Thông tin chi tiết về hủy phòng" +
                        "<table border='1'>\n" +
                        "    <thead>\n" +
                        "      <tr>\n" +
                        "        <th>vnp_response_id</th>\n" +
                        "        <th>vnp_response_code</th>\n" +
                        "        <th>vnp_tmn_code</th>\n" +
                        "        <th>vnp_transaction_no</th>\n" +
                        "        <th>vnp_transaction_status</th>\n" +
                        "        <th>vnp_transaction_type</th>\n" +
                        "        <th>vnp_txn_ref</th>\n" +
                        "        <th>vnp_amount</th>\n" +
                        "        <th>vnp_bank_code</th>\n" +
                        "        <th>vnp_command</th>\n" +
                        "        <th>vnp_message</th>\n" +
                        "        <th>vnp_order_info</th>\n" +
                        "      </tr>\n" +
                        "    </thead>\n" +
                        "    <tbody>\n" +
                        "      <tr>\n" +
                        "        <td>" + refundBillDto.vnp_ResponseId() + "</td>\n" +
                        "        <td>" + refundBillDto.vnp_ResponseCode() + "</td>\n" +
                        "        <td>" + refundBillDto.vnp_TmnCode() + "</td>\n" +
                        "        <td>" + refundBillDto.vnp_TransactionNo() + "</td>\n" +
                        "        <td>" + refundBillDto.vnp_TransactionStatus() + "</td>\n" +
                        "        <td>" + refundBillDto.vnp_TransactionType() + "</td>\n" +
                        "        <td>" + refundBillDto.vnp_TxnRef() + "</td>\n" +
                        "        <td>" + refundBillDto.vnp_Amount() + "</td>\n" +
                        "        <td>" + refundBillDto.vnp_BankCode() + "</td>\n" +
                        "        <td>" + refundBillDto.vnp_Command() + "</td>\n" +
                        "        <td>" + refundBillDto.vnp_Message() + "</td>\n" +
                        "        <td>" + refundBillDto.vnp_OrderInfo() + "</td>\n" +
                        "        <tr>\n" +
                        "  </table>";
                mailSender.sendMail(refundBillDto.email(),content,subject);
                return "Gửi mail thành công";
            }catch (Exception e){
                log.debug("send email failed with {}", refundBillDto.toString());
                throw new RuntimeException(e);
            }
        };
    }
}
