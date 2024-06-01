package com.kxw.quickit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.util.HashedWheelTimer;

import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class DelayQueueUtils {


    private volatile static HashedWheelTimer timer;

    private static HashedWheelTimer getTimer() {

        if (Objects.nonNull(timer)) {
            return timer;
        }
        synchronized (DelayQueueUtils.class) {
            if (Objects.isNull(timer)) {

                ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("DelayQueueUtils-%d").build();


                // The default number of slots on the time wheel, known as ticksPerWheel, is set to 512.
                // Increasing the number of allocated slots will result in a larger consumption of memory.
                // Conversely, allocating a smaller number of slots will lead to a higher number of task lists assigned to the same position, ensuring a reasonable distribution based on the actual quantity of tasks.
                // By default, there are 512 slots in a complete rotation. However, if a non-power of 2 value is provided, it will be adjusted to the next power of 2 that is greater than or equal to the specified parameter.
                // This adjustment aims to optimize the calculation of hash values.
                timer = new HashedWheelTimer(threadFactory, 1, TimeUnit.SECONDS, 128);

                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    if (Objects.nonNull(timer)) {
                        timer.stop();
                    }
                }));

            }
        }
        return timer;
    }

    /**
     * It is only suitable for executing tasks with a relatively short duration.
     */
    public static void delay(Task task, long delaySeconds) {

        getTimer().newTimeout(timeout -> task.run(), delaySeconds, TimeUnit.SECONDS);
    }


}
