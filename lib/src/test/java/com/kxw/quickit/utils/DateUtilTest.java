package com.kxw.quickit.utils;

import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


public class DateUtilTest {

    @Test
    public void getCurrentYear() {

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String dateNowStr = sdf.format(d);

        Assert.assertEquals(dateNowStr, DateUtil.getCurrentYear());
    }

    @Test
    public void getNextYear() {


        SimpleDateFormat dft = new SimpleDateFormat("yyyy");
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.YEAR, 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        Assert.assertEquals(dft.format(calendar.getTime()), DateUtil.getNextYear());

    }

    @Test
    public void getCurrentMonth() {

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String dateNowStr = sdf.format(d);

        Assert.assertEquals(dateNowStr, DateUtil.getCurrentMonth());
    }

    @Test
    public void getNextMonth() {


        SimpleDateFormat dft = new SimpleDateFormat("yyyyMM");
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        Assert.assertEquals(dft.format(calendar.getTime()), DateUtil.getNextMonth());

    }

    @Test
    public void getCurrentDay() {

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateNowStr = sdf.format(d);

        Assert.assertEquals(dateNowStr, DateUtil.getCurrentDay());
    }

    @Test
    public void formatDay() {

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateNowStr = sdf.format(d);

        Assert.assertEquals(dateNowStr, DateUtil.formatDay(Instant.now()));

    }


    @Test
    public void formatDay2() {

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateNowStr = sdf.format(d);

        Assert.assertEquals(dateNowStr, DateUtil.formatDay(Instant.now().getEpochSecond()));

    }

    @Test
    public void formatMonth() {

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String dateNowStr = sdf.format(d);

        Assert.assertEquals(dateNowStr, DateUtil.formatMonth(Instant.now()));

    }

    @Test
    public void formatMonth2() {

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String dateNowStr = sdf.format(d);

        Assert.assertEquals(dateNowStr, DateUtil.formatMonth(Instant.now().getEpochSecond()));

    }

    @Test
    public void formatHour() {

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH");
        String dateNowStr = sdf.format(d);

        Assert.assertEquals(dateNowStr, DateUtil.formatHour(Instant.now()));
        Assert.assertEquals(dateNowStr, DateUtil.formatHour(Instant.now().getEpochSecond()));

    }

    @Test
    public void formatMinute() {

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm");
        String dateNowStr = sdf.format(d);

        Assert.assertEquals(dateNowStr, DateUtil.formatMinute(Instant.now()));
        Assert.assertEquals(dateNowStr, DateUtil.formatMinute(Instant.now().getEpochSecond()));

    }

    @Test
    public void toEpochSecond() {
        System.out.println(DateUtil.toEpochSecond("2021-07-26 16:19:00"));
        Assert.assertEquals(1627287540L, DateUtil.toEpochSecond("2021-07-26 16:19:00"));
    }

    @Test
    public void getStartOfDay() {

        Assert.assertEquals(DateUtil.getStartOfDay(Instant.now()).getEpochSecond(), LocalDate.now().atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toEpochSecond());


        Assert.assertEquals(DateUtil.getStartOfDay("20220316").getEpochSecond(), DateUtil.toEpochSecond("2022-03-16 00:00:00"));
        Assert.assertEquals(DateUtil.getStartOfDay("20220316", 1).getEpochSecond(), DateUtil.toEpochSecond("2022-03-17 00:00:00"));
        Assert.assertEquals(DateUtil.getStartOfDay("20220316", -2).getEpochSecond(), DateUtil.toEpochSecond("2022-03-14 00:00:00"));
    }

    @Test
    public void getEndOfDay() {

        Assert.assertEquals(DateUtil.getEndOfDay(Instant.now()).getEpochSecond(), LocalDate.now().atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toEpochSecond());

    }

    @Test
    public void formatToTimestamp() {
        Assert.assertEquals(1644913107, DateUtil.formatToTimestamp("20220215161827", DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
    }

    @Test
    public void formatTime() {

        DateTimeFormatter TIME_DTF = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateNowStr = sdf.format(d);

        Assert.assertEquals(dateNowStr, DateUtil.formatTime(Instant.now(), TIME_DTF));

    }
}
