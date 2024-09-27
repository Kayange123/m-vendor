package dev.kayange.Multivendor.E.commerce.utils;

import java.security.SecureRandom;
import java.util.UUID;

public abstract class Utils {
    public static String generateProductSku(String name){
        String productName = name.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        String uniqueId = UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        return productName.substring(0, Math.min(5, productName.length())) + uniqueId;
    }

    public static String generateUserId(String name){
        String username = name.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        String uniqueId = UUID.randomUUID().toString().substring(0, 5).toLowerCase();
        return (username.substring(0, Math.min(5, username.length())) + uniqueId).toUpperCase();
    }

    public static String generateActivationOtp(Integer otpLength){
        String digits = "0123456789";
        StringBuilder sb = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for(int i=0; i<otpLength; i++){
            int index=  secureRandom.nextInt(digits.length());
            sb.append(digits.charAt(index));
        }

        return sb.toString();
    }
}
