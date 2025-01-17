package com.cexchange.Services;

public interface IOTPService {
    String generateOTP(String identifier);
    boolean validateOTP(String identifier, String otpToValidate);
}
