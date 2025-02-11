package com.uploadservice.utils;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.uploadservice.exception.InvalidFileException;

public class CsvFileUtility {
	
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public static String convertToUTC(String dateStr) {
        LocalDate localDate = LocalDate.parse(dateStr, DATE_FORMATTER);
        ZonedDateTime utcDateTime = localDate.atStartOfDay(ZoneOffset.UTC);
        return utcDateTime.toString();
    }

	public static boolean isValidDateFormat(String date) {
	    try {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        LocalDate.parse(date, formatter);
	        return true;
	    } catch (DateTimeParseException e) {
	        return false;
	    }
	}
	
	public static String validateStringField(String field, String fieldName) {
	    if (field == null || field.trim().isEmpty()) {
	        throw new InvalidFileException(fieldName + " cannot be empty.");
	    }
	    return field.trim();
	}
}
