package com.kxw.quickit.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private static final DateTimeFormatter YEAR_DTF = DateTimeFormatter.ofPattern("yyyy");

    private static final DateTimeFormatter MONTH_DTF = DateTimeFormatter.ofPattern("yyyyMM");

    private static final DateTimeFormatter DAY_DTF = DateTimeFormatter.ofPattern("yyyyMMdd");

    private static final DateTimeFormatter HOUR_DTF = DateTimeFormatter.ofPattern("yyyyMMdd HH");

    private static final DateTimeFormatter MINUTE_DTF = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm");

    private static final DateTimeFormatter TIME_DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final ZoneOffset LOCAL_ZONE = ZoneOffset.of("+8");

    public static String getCurrentYear() {
        return Instant.now().atZone(LOCAL_ZONE).toLocalDateTime().format(YEAR_DTF);
    }

    public static String getNextYear() {

        return LocalDateTime
                .now().plusYears(1).atZone(LOCAL_ZONE).toLocalDateTime().format(YEAR_DTF);
    }

    public static String getCurrentMonth() {
        return Instant.now().atZone(LOCAL_ZONE).toLocalDateTime().format(MONTH_DTF);
    }

    public static String getNextMonth() {

        return LocalDateTime
                .now().plusMonths(1).atZone(LOCAL_ZONE).toLocalDateTime().format(MONTH_DTF);
    }

    public static String getCurrentDay() {
        return Instant.now().atZone(LOCAL_ZONE).toLocalDateTime().format(DAY_DTF);
    }

    public static String formatDay(Instant instant) {
        return instant.atZone(LOCAL_ZONE).toLocalDateTime().format(DAY_DTF);
    }

    public static String formatDay(long timeSeconds) {
        return Instant.ofEpochSecond(timeSeconds).atZone(LOCAL_ZONE).toLocalDateTime().format(DAY_DTF);
    }

    public static String formatMonth(Instant instant) {
        return instant.atZone(LOCAL_ZONE).toLocalDateTime().format(MONTH_DTF);
    }

    public static String formatMonth(long timeSeconds) {
        return Instant.ofEpochSecond(timeSeconds).atZone(LOCAL_ZONE).toLocalDateTime().format(MONTH_DTF);
    }

    public static long toEpochSecond(String timeFmt){
        return LocalDateTime.parse(timeFmt, TIME_DTF).toEpochSecond(LOCAL_ZONE);
    }

    public static String formatTime(long timeSeconds) {
        return Instant.ofEpochSecond(timeSeconds).atZone(LOCAL_ZONE).toLocalDateTime().format(TIME_DTF);
    }

    public static String formatTime(Instant instant, DateTimeFormatter dateTimeFormatter) {
        return instant.atZone(LOCAL_ZONE).toLocalDateTime().format(dateTimeFormatter);
    }

    public static String formatTime(Instant instant) {
        return instant.atZone(LOCAL_ZONE).toLocalDateTime().format(TIME_DTF);
    }

    public static String formatHour(Instant instant) {
        return instant.atZone(LOCAL_ZONE).toLocalDateTime().format(HOUR_DTF);
    }

    public static String formatHour(long timeSeconds) {
        return Instant.ofEpochSecond(timeSeconds).atZone(LOCAL_ZONE).toLocalDateTime().format(HOUR_DTF);
    }

    public static String formatMinute(Instant instant) {
        return instant.atZone(LOCAL_ZONE).toLocalDateTime().format(MINUTE_DTF);
    }

    public static String formatMinute(long timeSeconds) {
        return Instant.ofEpochSecond(timeSeconds).atZone(LOCAL_ZONE).toLocalDateTime().format(MINUTE_DTF);
    }


    /** Obtain the maximum time of a certain day: 2020-02-19 23:59:59 */
    public static Instant getEndOfDay(Instant instant) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, LOCAL_ZONE);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return endOfDay.atZone(LOCAL_ZONE).toInstant();
    }

    /**
     * Obtain the minimum time of a certain day: 2020-02-17 00:00:00
     */
    public static Instant getStartOfDay(Instant instant) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, LOCAL_ZONE);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return startOfDay.atZone(LOCAL_ZONE).toInstant();
    }

    public static Instant getStartOfDay(String dayFmt) {
        return LocalDate.parse(dayFmt, DAY_DTF).atStartOfDay().toInstant(LOCAL_ZONE);
    }

    public static Instant getStartOfDay(String dayFmt, int plusDays) {
        return LocalDate.parse(dayFmt, DAY_DTF).plusDays(plusDays).atStartOfDay().toInstant(LOCAL_ZONE);
    }

    public static long formatToTimestamp(CharSequence text, DateTimeFormatter dtf) {
        return LocalDateTime.parse(text, dtf).toInstant(LOCAL_ZONE).getEpochSecond();
    }

}
