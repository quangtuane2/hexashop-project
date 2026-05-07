package com.example.hexashop_project.service;

import com.example.hexashop_project.model.SaleOrder;
import com.example.hexashop_project.model.SaleOrderProduct;
import com.example.hexashop_project.model.User;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Service gửi email qua Gmail SMTP.
 * Hỗ trợ 2 loại:
 * 1. Email xác minh tài khoản (khi đăng ký)
 * 2. Email xác nhận đơn hàng (sau khi checkout)
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Lấy từ application.properties: app.base-url=http://localhost:9090
    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    // EMAIL XÁC MINH TÀI KHOẢN

    /**
     * Gửi email chứa link xác minh sau khi user đăng ký.
     * 
     * @param user  User vừa đăng ký
     * @param token UUID token xác minh
     */
    public void sendVerificationEmail(User user, String token) {
        try {
            String verifyLink = baseUrl + "/verify-email?token=" + token;
            String fullName = user.getFirstname() + (user.getLastname() != null ? " " + user.getLastname() : "");

            String html = buildVerificationEmailHtml(fullName, verifyLink);

            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
            helper.setFrom(fromEmail, "HexaShop");
            helper.setTo(user.getEmail());
            helper.setSubject("📧 Xác minh tài khoản HexaShop của bạn");
            helper.setText(html, true); // true = HTML

            mailSender.send(msg);
        } catch (Exception e) {
            // Không để lỗi email làm crash luồng đăng ký
            System.err.println("[EmailService] Lỗi gửi email xác minh: " + e.getMessage());
        }
    }

    // EMAIL XÁC NHẬN ĐƠN HÀNG

    /**
     * Gửi email xác nhận đơn hàng sau khi checkout thành công.
     * 
     * @param order Đơn hàng vừa được lưu vào DB
     */
    public void sendOrderConfirmation(SaleOrder order) {
        try {
            String html = buildOrderEmailHtml(order);

            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
            helper.setFrom(fromEmail, "HexaShop");
            helper.setTo(order.getCustomerEmail());
            helper.setSubject("✅ Xác nhận đơn hàng #" + order.getCode() + " - HexaShop");
            helper.setText(html, true);

            mailSender.send(msg);
        } catch (Exception e) {
            System.err.println("[EmailService] Lỗi gửi email đơn hàng: " + e.getMessage());
        }
    }

    // PRIVATE: Xây dựng nội dung HTML email

    private String buildVerificationEmailHtml(String fullName, String verifyLink) {
        return "<!DOCTYPE html>" +
                "<html><head><meta charset='UTF-8'>" +
                "<style>" +
                "  body{margin:0;padding:0;background:#f5f6fa;font-family:Arial,sans-serif;}" +
                "  .wrap{max-width:600px;margin:30px auto;background:#fff;border-radius:16px;overflow:hidden;box-shadow:0 4px 20px rgba(0,0,0,.08);}"
                +
                "  .header{background:linear-gradient(135deg,#1a1a2e,#ff5e00);padding:36px 40px;text-align:center;}" +
                "  .header h1{color:#fff;margin:0;font-size:28px;letter-spacing:2px;}" +
                "  .header p{color:rgba(255,255,255,.8);margin:6px 0 0;font-size:13px;}" +
                "  .body{padding:36px 40px;}" +
                "  .body h2{color:#1a1a2e;font-size:20px;margin:0 0 12px;}" +
                "  .body p{color:#555;font-size:15px;line-height:1.7;margin:0 0 20px;}" +
                "  .btn{display:block;width:fit-content;margin:24px auto;padding:16px 40px;" +
                "       background:linear-gradient(135deg,#ff5e00,#e05200);color:#fff;text-decoration:none;" +
                "       border-radius:10px;font-size:16px;font-weight:700;text-align:center;}" +
                "  .note{background:#fff8f5;border-left:4px solid #ff5e00;padding:14px 18px;border-radius:4px;" +
                "        font-size:13px;color:#666;margin-top:20px;}" +
                "  .footer{background:#f8f9fb;padding:20px 40px;text-align:center;font-size:12px;color:#aaa;}" +
                "</style></head><body>" +
                "<div class='wrap'>" +
                "  <div class='header'><h1>HexaShop</h1><p>Fashion for Everyone</p></div>" +
                "  <div class='body'>" +
                "    <h2>Chào " + fullName + "! 👋</h2>" +
                "    <p>Cảm ơn bạn đã đăng ký tài khoản tại <strong>HexaShop</strong>.</p>" +
                "    <p>Để hoàn tất đăng ký và bắt đầu mua sắm, vui lòng nhấn nút bên dưới để xác minh địa chỉ email của bạn:</p>"
                +
                "    <a href='" + verifyLink + "' class='btn'>✅ Xác minh Email ngay</a>" +
                "    <div class='note'>⏰ Link xác minh sẽ hết hạn sau <strong>24 giờ</strong>.<br>" +
                "    Nếu bạn không đăng ký tài khoản này, hãy bỏ qua email này.</div>" +
                "    <p style='margin-top:24px;font-size:13px;color:#999;'>Hoặc copy link sau vào trình duyệt:<br>" +
                "    <span style='color:#ff5e00;word-break:break-all;'>" + verifyLink + "</span></p>" +
                "  </div>" +
                "  <div class='footer'>© 2024 HexaShop Co., Ltd. All Rights Reserved.</div>" +
                "</div></body></html>";
    }

    private String buildOrderEmailHtml(SaleOrder order) {
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String orderDate = (order.getCreateDate() != null) ? sdf.format(order.getCreateDate()) : "N/A";

        // Xây dựng bảng sản phẩm
        StringBuilder rows = new StringBuilder();
        if (order.getSaleOrderProducts() != null) {
            int stt = 1;
            for (SaleOrderProduct item : order.getSaleOrderProducts()) {
                String price = (item.getPrice() != null) ? nf.format(item.getPrice()) + " $" : "N/A";
                rows.append("<tr>")
                        .append("<td style='padding:10px 14px;border-bottom:1px solid #f0f0f0;'>").append(stt++)
                        .append("</td>")
                        .append("<td style='padding:10px 14px;border-bottom:1px solid #f0f0f0;font-weight:600;'>")
                        .append(item.getName()).append("</td>")
                        .append("<td style='padding:10px 14px;border-bottom:1px solid #f0f0f0;color:#666;font-size:13px;'>")
                        .append(item.getDescription() != null ? item.getDescription() : "").append("</td>")
                        .append("<td style='padding:10px 14px;border-bottom:1px solid #f0f0f0;text-align:center;'>")
                        .append(item.getQuantity()).append("</td>")
                        .append("<td style='padding:10px 14px;border-bottom:1px solid #f0f0f0;text-align:right;color:#ff5e00;font-weight:700;'>")
                        .append(price).append("</td>")
                        .append("</tr>");
            }
        }

        String total = (order.getTotal() != null) ? nf.format(order.getTotal()) + " $" : "N/A";
        String trackUrl = baseUrl + "/order-tracking";
        String statusName = (order.getOrderStatus() != null) ? order.getOrderStatus().getName() : "Pending";

        return "<!DOCTYPE html>" +
                "<html><head><meta charset='UTF-8'>" +
                "<style>" +
                "  body{margin:0;padding:0;background:#f5f6fa;font-family:Arial,sans-serif;}" +
                "  .wrap{max-width:640px;margin:30px auto;background:#fff;border-radius:16px;overflow:hidden;box-shadow:0 4px 20px rgba(0,0,0,.08);}"
                +
                "  .header{background:linear-gradient(135deg,#1a1a2e,#ff5e00);padding:36px 40px;text-align:center;}" +
                "  .header h1{color:#fff;margin:0;font-size:28px;letter-spacing:2px;}" +
                "  .header p{color:rgba(255,255,255,.8);margin:6px 0 0;}" +
                "  .body{padding:32px 40px;}" +
                "  .success-badge{display:inline-block;background:#e8f5e9;color:#2e7d32;padding:8px 20px;border-radius:20px;font-weight:700;font-size:14px;margin-bottom:20px;}"
                +
                "  .info-row{display:flex;gap:16px;margin-bottom:24px;flex-wrap:wrap;}" +
                "  .info-box{flex:1;min-width:130px;background:#f8f9fb;border-radius:10px;padding:14px 18px;}" +
                "  .info-box label{display:block;font-size:11px;color:#999;font-weight:700;text-transform:uppercase;letter-spacing:.5px;margin-bottom:5px;}"
                +
                "  .info-box span{font-size:15px;font-weight:700;color:#1a1a2e;}" +
                "  table{width:100%;border-collapse:collapse;}" +
                "  th{background:#f8f9fb;padding:10px 14px;text-align:left;font-size:12px;color:#999;text-transform:uppercase;letter-spacing:.5px;border-bottom:2px solid #eee;}"
                +
                "  .total-row td{padding:14px;font-size:16px;font-weight:800;color:#ff5e00;border-top:2px solid #eee;}"
                +
                "  .ship-box{background:#f8f9fb;border-radius:10px;padding:18px;margin:20px 0;}" +
                "  .ship-box h4{margin:0 0 10px;color:#1a1a2e;font-size:14px;}" +
                "  .ship-box p{margin:4px 0;font-size:14px;color:#555;}" +
                "  .btn{display:block;width:fit-content;margin:28px auto 0;padding:14px 36px;" +
                "       background:linear-gradient(135deg,#ff5e00,#e05200);color:#fff;text-decoration:none;" +
                "       border-radius:10px;font-size:15px;font-weight:700;text-align:center;}" +
                "  .footer{background:#f8f9fb;padding:20px 40px;text-align:center;font-size:12px;color:#aaa;}" +
                "</style></head><body>" +
                "<div class='wrap'>" +
                "  <div class='header'><h1>HexaShop</h1><p>Xác nhận đơn hàng</p></div>" +
                "  <div class='body'>" +
                "    <span class='success-badge'>✅ Đặt hàng thành công!</span>" +
                "    <p style='color:#333;font-size:15px;'>Chào <strong>" + order.getCustomerName() + "</strong>,<br>" +
                "    Cảm ơn bạn đã mua sắm tại HexaShop! Đơn hàng của bạn đã được ghi nhận.</p>" +
                "    <div class='info-row'>" +
                "      <div class='info-box'><label>Mã đơn hàng</label><span>#" + order.getCode() + "</span></div>" +
                "      <div class='info-box'><label>Ngày đặt</label><span>" + orderDate + "</span></div>" +
                "      <div class='info-box'><label>Trạng thái</label><span style='color:#ff5e00;'>" + statusName
                + "</span></div>" +
                "    </div>" +
                // Bảng sản phẩm
                "    <h3 style='color:#1a1a2e;font-size:16px;margin:0 0 12px;'>📦 Chi tiết đơn hàng</h3>" +
                "    <table><thead><tr>" +
                "      <th>#</th><th>Sản phẩm</th><th>Phân loại</th><th>SL</th><th style='text-align:right;'>Giá</th>" +
                "    </tr></thead><tbody>" + rows + "</tbody>" +
                "    <tfoot><tr class='total-row'>" +
                "      <td colspan='4' style='padding:14px;'>Tổng cộng</td>" +
                "      <td style='text-align:right;padding:14px;font-size:18px;font-weight:800;color:#ff5e00;'>" + total
                + "</td>" +
                "    </tr></tfoot></table>" +
                // Địa chỉ giao hàng
                "    <div class='ship-box'>" +
                "      <h4>🚚 Thông tin giao hàng</h4>" +
                "      <p><strong>" + order.getCustomerName() + "</strong></p>" +
                "      <p>" + (order.getCustomerAddress() != null ? order.getCustomerAddress() : "") + "</p>" +
                "      <p>📞 " + (order.getCustomerMobile() != null ? order.getCustomerMobile() : "") + "</p>" +
                "    </div>" +
                "    <a href='" + trackUrl + "' class='btn'>👁 Xem đơn hàng của tôi</a>" +
                "  </div>" +
                "  <div class='footer'>© 2024 HexaShop Co., Ltd. All Rights Reserved.</div>" +
                "</div></body></html>";
    }
}
