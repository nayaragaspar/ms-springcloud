package com.nayaragaspar.gprfid.utility;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class Utility {
    public static final String ZONE_ID_AMERICA_SP = "America/Sao_Paulo";
    private static final String TCP_PROTOCOL = "tcp://";

    private Utility() {
    }

    public static Date getFutureDate(int secondsToAdd) {
        Calendar date = Calendar.getInstance();
        long timeInSecs = date.getTimeInMillis();
        return new Date(timeInSecs + (secondsToAdd * 1000L));
    }

    public static String convertObjectToString(Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String stringObj;
            stringObj = objectMapper.writeValueAsString(object);
            return stringObj;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getIpWithTcp(String ip) {
        if (ip.contains("tcp"))
            return ip;

        return TCP_PROTOCOL + ip;
    }

    public static String getIpWithoutTcp(String ip) {
        if (ip.contains("tcp"))
            return ip.replace(TCP_PROTOCOL, "");

        return ip;
    }

    public static String getUtcDateNow() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ISO_INSTANT; // 2011-12-03T10:15:30Z
        return ZonedDateTime.of(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                ZoneId.of(ZONE_ID_AMERICA_SP)).format(dateFormat);
    }

    public static LocalDateTime getLocalDateTimeNow() {
        return LocalDateTime.now(ZoneId.of(Utility.ZONE_ID_AMERICA_SP));
    }
}
