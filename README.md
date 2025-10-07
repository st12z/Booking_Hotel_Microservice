# Booking Hotel Microservice System

## Giới thiệu

**Booking Hotel** là một hệ thống đặt phòng khách sạn hiện đại, được xây dựng theo kiến trúc **Microservice** với mục tiêu:

- Tối ưu hiệu năng và khả năng mở rộng
- Tích hợp thanh toán và thông báo realtime
- Bảo mật với phân quyền chi tiết theo vai trò
- Hỗ trợ đầy đủ chức năng từ tìm kiếm, đặt phòng, đánh giá, thanh toán và quản trị

---

## Kiến trúc tổng quan

Hệ thống gồm **7 service** chính:

| Service         | Chức năng chính                                                                 |
|------------------|----------------------------------------------------------------------------------|
| `users`          | Quản lý người dùng, đăng ký, đăng nhập, phân quyền RBAC                         |
| `rooms`          | Quản lý phòng, trips, destinations, bộ lọc tìm kiếm                             |
| `bookings`       | Đặt phòng, giữ phòng tạm thời, xác nhận booking, xuất hóa đơn                   |
| `payments`       | Tích hợp thanh toán (VNPAY), hoàn tiền, xử lý gian lận                          |
| `messages`       | Nhắn tin realtime, gửi email, cập nhật trạng thái hóa đơn, sử dụng WebSocket    |
| `gatewayserver`  | Cổng giao tiếp giữa frontend và các service backend                            |
| `eurekaserver`   | Service Discovery cho các microservice                                          |

---

## Xác thực và Phân quyền (RBAC)

- Sử dụng **Keycloak** làm **Authorization Server**
- Phân quyền người dùng dựa trên **Role** (RBAC)
- Mỗi người dùng có thể được gán nhiều vai trò:
  - `ROLE_USER`, `ROLE_ADMIN`, `ROLE_MANAGER`, `ROLE_OWNER`, ...

---

## Công nghệ sử dụng

| Công nghệ      | Mục đích                                                                 |
|----------------|--------------------------------------------------------------------------|
| Spring Boot    | Framework chính cho từng service                                         |
| Spring Security + Keycloak | Xác thực và phân quyền người dùng                             |
| Redis          | Cache hóa truy vấn, giữ phòng tạm thời, khóa OTP                         |
| Redisson       | Distributed Lock xử lý đồng thời khi đặt phòng, lưu phiếu giảm giá       |
| RabbitMQ       | Xử lý bất đồng bộ (email, thanh toán, cập nhật đơn hàng)                |
| VNPAY          | Tích hợp thanh toán và hoàn tiền online                                 |
| Cloudinary     | Lưu trữ ảnh phòng, người dùng                                            |
| Spring WebSocket | Nhắn tin, thông báo realtime, cập nhật trạng thái thanh toán            |
| Apache POI     | Xuất báo cáo và dữ liệu ra file Excel                                   |
| Eureka Server  | Quản lý các service (Service Registry)                                  |
| Gateway Server | Định tuyến API chung cho toàn hệ thống                                  |

---

## Các tính năng nổi bật

### Tìm kiếm & Lọc thông minh
- API tìm kiếm và lọc theo:
  - Tên phòng, loại phòng, giá, địa điểm
  - Điểm đến (`destination`), chuyến đi (`trip`)
  - Các thuộc tính (`property`) của khách sạn

### Hệ thống đánh giá & nhận xét
- Người dùng sau khi hoàn thành đặt phòng có thể:
  - **Gửi đánh giá (rating)** theo thang điểm (1-5)
  - **Bình luận (comment)** nội dung trải nghiệm
- Trung bình đánh giá được tính động trên từng phòng & hiển thị

### Thống kê & Quản lý
- Trang quản trị có thể:
  - Thống kê doanh thu, số lượng đặt phòng
  - Quản lý danh sách người dùng, quyền hạn
  - Thêm/sửa/xóa vai trò và phân quyền tương ứng
  - Xuất dữ liệu ra Excel (phòng, booking, người dùng, đánh giá...)

### Thanh toán & Gian lận
- Tích hợp cổng **VNPAY**
- Cơ chế bảo vệ:
  - Thanh toán vượt ngưỡng → yêu cầu **OTP**
  - Thất bại nhiều lần → khóa tạm trong **1 giờ** (Redis)
- RabbitMQ gửi sự kiện thanh toán:
  - Thành công, thất bại, hoàn tiền
  - Cập nhật đơn hàng, thông báo người dùng

### Xử lý đồng thời
- **Redis + Redisson** đảm bảo:
  - Tránh tình trạng giữ cùng 1 phòng cho nhiều người
  - Đặt phiếu giảm giá đồng thời không bị lỗi race condition

### Realtime thông báo
- Dùng **Spring WebSocket** để:
  - Gửi thông báo trạng thái thanh toán
  - Gửi tin nhắn chat giữa người dùng và quản lý
  - Cập nhật hóa đơn, số tiền, điểm thưởng realtime
## Triển khai (Deployment): render
