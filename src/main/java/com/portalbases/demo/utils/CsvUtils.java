package com.portalbases.demo.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CsvUtils {

    public static String cleanValue(String value) {
        return value == null ? null : value.trim().replaceAll("^\"|\"$", "");
    }


    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty() || dateStr.equalsIgnoreCase("N/A")) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            System.err.println("Erro ao converter data '" + dateStr + "': " + e.getMessage());
            return null;
        }
    }
}