package com.uploadservice.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.Test;

import com.uploadservice.exception.InvalidFileException;

class CsvFileUtilityTest {

    @Test
    void convertToUTC_ValidDate_ReturnsUTCString() {
        String dateStr = "2024-02-10";
        String utcDate = CsvFileUtility.convertToUTC(dateStr);
        
        assertThat(utcDate).isEqualTo(LocalDate.parse(dateStr).atStartOfDay().atOffset(java.time.ZoneOffset.UTC).toString());
    }

    @Test
    void convertToUTC_InvalidDate_ThrowsException() {
        String invalidDateStr = "10-02-2024";
        
        assertThatThrownBy(() -> CsvFileUtility.convertToUTC(invalidDateStr))
                .isInstanceOf(DateTimeParseException.class);
    }

    @Test
    void isValidDateFormat_ValidDate_ReturnsTrue() {
        assertThat(CsvFileUtility.isValidDateFormat("2024-02-10")).isTrue();
    }

    @Test
    void isValidDateFormat_InvalidDate_ReturnsFalse() {
        assertThat(CsvFileUtility.isValidDateFormat("10-02-2024")).isFalse();
    }

    @Test
    void validateStringField_ValidString_ReturnsTrimmedString() {
        String result = CsvFileUtility.validateStringField("  Test String  ", "Field");
        assertThat(result).isEqualTo("Test String");
    }

    @Test
    void validateStringField_EmptyString_ThrowsInvalidFileException() {
        assertThatThrownBy(() -> CsvFileUtility.validateStringField(" ", "Field"))
                .isInstanceOf(InvalidFileException.class)
                .hasMessage("Field cannot be empty.");
    }
}
