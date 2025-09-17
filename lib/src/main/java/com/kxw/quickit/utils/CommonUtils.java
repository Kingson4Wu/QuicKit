package com.kxw.quickit.utils;

import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class CommonUtils {

    private static final Charset CHARSET_UTF_8 = StandardCharsets.UTF_8;

    private CommonUtils() {
    }

    public static Long getUnixTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static Charset getDefaultCharset() {
        return CHARSET_UTF_8;
    }

    public static void randomSleep(int seconds) {
        if (seconds <= 0) {
            return;
        }
        sleep(ThreadLocalRandom.current().nextInt(seconds));
    }

    public static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(ex);
        }
    }

    public static String getRandomCode(int num) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num; i++) {
            sb.append(ThreadLocalRandom.current().nextInt(10));
        }
        return sb.toString();
    }

    public static String getDomain(String url){

        if (StringUtils.isBlank(url)) {
            return StringUtils.EMPTY;
        }
        try {
            URL urlO = new URL(url);
            return urlO.getHost();
        } catch (MalformedURLException ignored) {
        }
        return StringUtils.EMPTY;
    }
}
