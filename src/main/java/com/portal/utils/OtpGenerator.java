package com.portal.utils;

import java.util.Random;

public class OtpGenerator {
    
    // Generate a 6-digit OTP
    public static String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Ensures OTP is 6 digits
        return String.valueOf(otp);
    }
}
