package com.kxw.quickit.utils;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;


public class CommonUtilsTest {


    @Test
    public void getUnixTimeStamp() {
        Assert.assertTrue(CommonUtils.getUnixTimeStamp() > 0);
    }

    @Test
    public void getUUID() {
        Assert.assertTrue(StringUtils.isNotBlank(CommonUtils.getUUID()));
    }

    @Test
    public void getDefaultCharset() {
        Assert.assertEquals(CommonUtils.getDefaultCharset().toString(), "UTF-8");
    }

    @Test
    public void randomSleep() {
        CommonUtils.randomSleep(5);
    }

    @Test
    public void sleep() {
        CommonUtils.sleep(1);
    }

    @Test
    public void getRandomCode(){
        String code = CommonUtils.getRandomCode(6);
        Assert.assertEquals(code.length(), 6);
        Assert.assertTrue(StringUtils.isNumeric(code));

    }

    @Test
    public void getDomain(){
        Assert.assertEquals("dddd.google.com", CommonUtils.getDomain("http://dddd.google.com/error/report"));
        Assert.assertEquals("", CommonUtils.getDomain("ffgggggg"));
        Assert.assertEquals("", CommonUtils.getDomain(""));

    }

}
