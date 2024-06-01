package com.kxw.quickit;

import com.kxw.quickit.utils.ConfigUtil;
import com.kxw.quickit.utils.DateUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigUtil.class})
public class ExecutionFrequencyUtilsTest {

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(ConfigUtil.class);

        BDDMockito.given(ConfigUtil.getBooleanValue(eq("ExecutionFrequencyUtils-sync-stop"), anyBoolean())).willReturn(false);
        BDDMockito.given(ConfigUtil.getIntValue(eq("ExecutionFrequencyUtils-sync-limit"), anyInt())).willReturn(-1);

    }

    @Test
    public void submitAsync() {

        AtomicInteger total = new AtomicInteger();

        List<Task> taskList = new ArrayList<>(108);
        IntStream.range(0, 108).forEach(i -> {
            taskList.add(() -> {
                System.out.println(i + "----" + DateUtil.formatTime(Instant.now()));
                total.incrementAndGet();
            });
        });

        ExecutionFrequencyUtils.submitAsync("sync", taskList, 35);


        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(total.get());

        Assert.assertEquals(total.get(), 108);


        ////----


        total.set(0);
        ExecutionFrequencyUtils.submitAsync("sync", taskList, 35);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BDDMockito.given(ConfigUtil.getBooleanValue(eq("ExecutionFrequencyUtils-sync-stop"), anyBoolean())).willReturn(true);

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(total.get() < 108);

        BDDMockito.given(ConfigUtil.getBooleanValue(eq("ExecutionFrequencyUtils-sync-stop"), anyBoolean())).willReturn(false);
        ////----

        total.set(0);
        BDDMockito.given(ConfigUtil.getIntValue(eq("ExecutionFrequencyUtils-sync-limit"), anyInt())).willReturn(50);

        ExecutionFrequencyUtils.submitAsync("sync", taskList, 35);


        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(total.get());

        Assert.assertEquals(total.get(), 50);
    }


    @Test
    public void submit() {

        AtomicInteger total = new AtomicInteger();

        List<Task> taskList = new ArrayList<>(108);
        IntStream.range(0, 108).forEach(i -> {
            taskList.add(() -> {
                System.out.println(i + "----" + DateUtil.formatTime(Instant.now()));
                total.incrementAndGet();
            });
        });

        ExecutionFrequencyUtils.submit("sync", taskList, 35);

        System.out.println(total.get());

        Assert.assertEquals(total.get(), 108);


        ////----


        total.set(0);

        DelayQueueUtils.delay(()-> BDDMockito.given(ConfigUtil.getBooleanValue(eq("ExecutionFrequencyUtils-sync-stop"), anyBoolean())).willReturn(true),1);

        ExecutionFrequencyUtils.submit("sync", taskList, 35);


        Assert.assertTrue(total.get() < 108);

        BDDMockito.given(ConfigUtil.getBooleanValue(eq("ExecutionFrequencyUtils-sync-stop"), anyBoolean())).willReturn(false);
        ////----

        total.set(0);
        BDDMockito.given(ConfigUtil.getIntValue(eq("ExecutionFrequencyUtils-sync-limit"), anyInt())).willReturn(50);

        ExecutionFrequencyUtils.submit("sync", taskList, 35);

        System.out.println(total.get());

        Assert.assertEquals(total.get(), 50);


        total.set(0);
        BDDMockito.given(ConfigUtil.getIntValue(eq("ExecutionFrequencyUtils-sync-limit"), anyInt())).willReturn(0);

        ExecutionFrequencyUtils.submit("sync", taskList, 35);

        System.out.println(total.get());

        Assert.assertEquals(total.get(), 0);
    }

}
