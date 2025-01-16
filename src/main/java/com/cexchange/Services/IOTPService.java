package com.cexchange.Services;

public interface IOTPService {
    String generateOTP(String phoneNumber);
    boolean validateOTP(String phoneNumber, String otp);
}
