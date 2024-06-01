package com.kxw.quickit;

import com.kxw.quickit.utils.CommonUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

public class RetryUtilsTest {

    @Test
    public void retryCall() {

        Function<Resp, Boolean> isFailure = (resp) -> resp.code != 0;

        AtomicInteger count = new AtomicInteger();

        Supplier<Resp> task = () -> {

            Resp resp = new Resp();
            resp.code = 1;
            if (count.getAndIncrement() == 2) {
                resp.code = 0;
            }
            return resp;
        };
        Resp resp =
                RetryUtils.retryCall("testTask", isFailure, 3, task, new Resp());
        Assert.assertEquals(resp.code, 0);
        Assert.assertEquals(count.get(), 3);

    }

    class Resp {
        int code;
    }


    @Ignore
    @Test
    public void retryCall2() {

        Function<Resp, Boolean> isFailure = (resp) -> resp.code != 0;

        AtomicInteger count = new AtomicInteger();

        Supplier<Resp> task = () -> {

            Resp resp = new Resp();
            resp.code = 1;
            if (count.getAndIncrement() == 2) {
                resp.code = 0;
            }
            CommonUtils.sleep(5);
            return resp;
        };
        Resp resp =
                RetryUtils.retryCall("testTask", isFailure, 3, task, new Resp());
        Assert.assertEquals(resp.code, 0);
        Assert.assertEquals(count.get(), 3);

    }
}
