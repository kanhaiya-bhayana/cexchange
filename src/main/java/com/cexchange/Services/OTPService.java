package com.cexchange.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class OTPService implements IOTPService {

    private static final int OTP_LENGTH = 6;
    private static final int TIME_STEP = 60; // 5 minutes in seconds
    private static final String HASH_ALGORITHM = "HmacSHA256";
    @Value("${otp.secret}")
    private String secretKey; // Should be stored securely in application.properties

    @Override
    public String generateOTP(String identifier) {
        try {
            // Get current timestamp window
            long timeWindow = Instant.now().getEpochSecond() / TIME_STEP;

            // Combine identifier and time window
            String message = identifier + "|" + timeWindow;

            // Create HMAC-SHA256 hash
            Mac hmac = Mac.getInstance(HASH_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(
                    secretKey.getBytes(),
                    HASH_ALGORITHM
            );
            hmac.init(keySpec);

            // Generate hash
            byte[] hash = hmac.doFinal(message.getBytes());

            // Convert to number and get last 6 digits
            int offset = hash[hash.length - 1] & 0xf;
            int binary = ((hash[offset] & 0x7f) << 24) |
                    ((hash[offset + 1] & 0xff) << 16) |
                    ((hash[offset + 2] & 0xff) << 8) |
                    (hash[offset + 3] & 0xff);

            // Get last 6 digits
            int otp = binary % (int) Math.pow(10, OTP_LENGTH);

            // Format to ensure 6 digits with leading zeros
            return String.format("%0" + OTP_LENGTH + "d", otp);

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Error generating OTP", e);
        }
    }

    @Override
    public boolean validateOTP(String identifier, String otpToValidate) {
        // Check current time window
        String currentOTP = generateOTP(identifier);
        if (otpToValidate.equals(currentOTP)) {
            return true;
        }

        // Also check previous time window to allow for slight delays
        long previousTimeWindow = (Instant.now().getEpochSecond() - TIME_STEP) / TIME_STEP;
        String message = identifier + "|" + previousTimeWindow;

        try {
            Mac hmac = Mac.getInstance(HASH_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(
                    secretKey.getBytes(),
                    HASH_ALGORITHM
            );
            hmac.init(keySpec);

            byte[] hash = hmac.doFinal(message.getBytes());

            int offset = hash[hash.length - 1] & 0xf;
            int binary = ((hash[offset] & 0x7f) << 24) |
                    ((hash[offset + 1] & 0xff) << 16) |
                    ((hash[offset + 2] & 0xff) << 8) |
                    (hash[offset + 3] & 0xff);

            int previousOTP = binary % (int) Math.pow(10, OTP_LENGTH);
            String previousOTPString = String.format("%0" + OTP_LENGTH + "d", previousOTP);

            return otpToValidate.equals(previousOTPString);

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Error validating OTP", e);
        }
    }
}
