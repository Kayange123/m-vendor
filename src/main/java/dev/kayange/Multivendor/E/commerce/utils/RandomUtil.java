package dev.kayange.Multivendor.E.commerce.utils;

import org.apache.commons.lang3.RandomStringUtils;

public final class RandomUtil {
    private static final String NUMERIC_STRING = "0123456789";

    private static final int DEF_COUNT = 20;

    private RandomUtil() {
    }

    public static String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(DEF_COUNT).toUpperCase();
    }

    public static String generateActivationKey() {
        return RandomStringUtils.randomNumeric(32).toUpperCase();
    }

    public static String generateResetKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT).toUpperCase();
    }

    public static String generateTransactionNumber() {
        return RandomStringUtils.randomAlphanumeric(16).toUpperCase();
    }

    public static String generateTransactionNumber(int length) {
        return RandomStringUtils.randomAlphanumeric(length).toUpperCase();
    }

    public static String generateOrderNumber() {
        return RandomStringUtils.randomAlphanumeric(20).toUpperCase();
    }

    public static String generatePublicId() {
        return RandomStringUtils.randomAlphanumeric(10).toUpperCase();
    }

    public static String generateContractNumber() {
        return RandomStringUtils.randomAlphanumeric(10).toUpperCase();
    }

    public static String randomNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*NUMERIC_STRING.length());
            builder.append(NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}
