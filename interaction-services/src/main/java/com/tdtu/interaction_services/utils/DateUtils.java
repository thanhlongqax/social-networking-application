package com.tdtu.interaction_services.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateUtils {
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        Instant instantUtc = localDateTime.toInstant(ZoneId.of("UTC").getRules().getOffset(localDateTime));

        ZonedDateTime zonedDateTimeVn = instantUtc.atZone(ZoneId.of("Asia/Ho_Chi_Minh"));

        return Date.from(zonedDateTimeVn.toInstant());
    }
}
