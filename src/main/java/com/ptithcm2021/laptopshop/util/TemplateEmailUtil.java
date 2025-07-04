package com.ptithcm2021.laptopshop.util;

public class TemplateEmailUtil {
    public static String subjectMailSendOTP(String otp){
        return "<!DOCTYPE html><html><head>"
                + "<style>"
                + ".container { font-family: Arial, sans-serif; text-align: center; padding: 20px; border: 1px solid #ddd; border-radius: 10px; width: 400px; margin: auto; box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1); }"
                + ".otp { font-size: 24px; font-weight: bold; color: #007bff; margin: 10px 0; }"
                + ".footer { font-size: 12px; color: #666; margin-top: 20px; }"
                + "</style></head><body>"
                + "<div class='container'><h2>Yêu cầu xác nhận</h2><p>Mã OTP của bạn là:</p>"
                + "<div class='otp'>" + otp + "</div>"
                + "<p>Vui lòng không chia sẻ mã này với bất kỳ ai.</p>"
                + "<p class='footer'>Mã này sẽ hết hạn sau 5 phút.</p></div>"
                + "</body></html>";
    }
}
