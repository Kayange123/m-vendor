package dev.kayange.Multivendor.E.commerce.utils;

import org.apache.commons.io.FileUtils;
import com.fasterxml.uuid.Generators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class GlobalMethod {

    private static final Logger logger = LoggerFactory.getLogger(GlobalMethod.class);

    @Value("${file.upload.directory}")
    private final String uploadDirectory = AppConstant.UPLOADS_DIRECTORY;

    public static String GenerateUniqueID() {
        UUID uuid = Generators.timeBasedGenerator().generate();
        return uuid.toString().replace("-", "");
    }

    public static String uniqId() {
        UUID uuid = Generators.timeBasedGenerator().generate();
        return uuid.toString().replace("-", "");
    }

    public static long getTimeStamp() {
        return new Date().getTime();
    }

    public static LocalDate stringToLocalDate(String formDate) {
        return LocalDate.parse(formDate);
    }

    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime convertToLocalDateTimeViaMilisecond(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime getLocalDate(long valueToConvert) {
        Date currentDate = new Date(valueToConvert);
        return currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDate stringToDate(String dateToConvert) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(dateToConvert, formatter);
    }

    public static LocalDate stringToLocalDateFormat(String dateToConvert) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dateToConvert, formatter);
    }

    public static Date convertLocalDateToDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

    public static java.sql.Date convertLocalDateToSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

    public static LocalDate stringToDateFormat(String dateToConvert) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[dd-MM-yyyy][yyyy-MM-dd]");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate receivedDate = LocalDate.parse(dateToConvert, formatter);
        String returnDateStr = receivedDate.format(formatter2);

        return LocalDate.parse(returnDateStr, formatter2);
    }

    public static List<?> convertObjectToList(Object obj) {
        List<?> list = new ArrayList<>();
        if (obj.getClass().isArray()) {
            list = Arrays.asList((Object[]) obj);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<?>) obj);
        }
        return list;
    }

    public static boolean isObjectHashMap(Object source) {

        try {
            HashMap<String, Object> result = (HashMap<String, Object>) source;
            return true;
        } catch (ClassCastException e) {
        }

        return false;

    }

    public static boolean isObjectString(Object source) {

        try {
            String result = (String) source;
            return true;
        } catch (ClassCastException e) {
        }

        return false;

    }

    public static String harshMethod(String string) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(string.getBytes());

        byte[] byteData = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xFF) + 256, 16).substring(1));
        }
        return sb.toString();
    }

    public static String convertFileToBase64(File file) {

        try {
            if (file.exists()) {
                byte[] bytes = FileUtils.readFileToByteArray(file);
                return Base64.getEncoder().encodeToString(bytes);
            } else {
                String uploadDirectory = AppConstant.UPLOADS_DIRECTORY;
                File notFoundFile = new File(uploadDirectory, "no-file.pdf");
                byte[] bytes = FileUtils.readFileToByteArray(notFoundFile);
                return Base64.getEncoder().encodeToString(bytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
            byte[] bytesEncoded = Base64.getEncoder().encode("No file found".getBytes());
            return Base64.getEncoder().encodeToString(bytesEncoded);
        }
    }

    public static String convertDateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(date);
    }

    public static String readableLocalDate(LocalDate date) {
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        return date.format(formatters);
    }

    public static LocalDateTime stringToLocalDateTime(String string) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(string, formatter);
    }
}

