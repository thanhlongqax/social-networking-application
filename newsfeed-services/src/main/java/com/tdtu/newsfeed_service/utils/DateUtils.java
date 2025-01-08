package com.tdtu.newsfeed_service.utils;


import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

@Slf4j
public class DateUtils {
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        Instant instantUtc = localDateTime.toInstant(ZoneId.of("UTC").getRules().getOffset(localDateTime));

        ZonedDateTime zonedDateTimeVn = instantUtc.atZone(ZoneId.of("Asia/Ho_Chi_Minh"));

        return Date.from(zonedDateTimeVn.toInstant());
    }

    public static LocalDateTime stringToLocalDate(String stringDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        try {
            return LocalDateTime.parse(stringDate, formatter);
        } catch (DateTimeParseException e) {
            log.info("Invalid date or format");
        }
        return LocalDateTime.now();
    }
}