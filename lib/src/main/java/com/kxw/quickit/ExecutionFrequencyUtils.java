package com.kxw.quickit;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.kxw.quickit.utils.CommonUtils;
import com.kxw.quickit.utils.ConfigUtil;
import io.netty.util.HashedWheelTimer;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class ExecutionFrequencyUtils {


    private static volatile HashedWheelTimer timer;

    private static HashedWheelTimer getTimer() {

        if (Objects.nonNull(timer)) {
            return timer;
        }
        synchronized (ExecutionFrequencyUtils.class) {
            if (Objects.isNull(timer)) {

                ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("ExecutionFrequencyUtils-%d").build();

                timer = new HashedWheelTimer(threadFactory, 1, TimeUnit.SECONDS, 60);

                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    if (Objects.nonNull(timer)) {
                        timer.stop();
                    }
                }));

            }
        }
        return timer;
    }

    public static void submitAsync(String taskName, List<Task> taskList, int executeNumPerSeconds) {

        List<List<Task>> subList = taskPartition(taskName, taskList, executeNumPerSeconds);
        int delaySeconds = 1;
        for (List<Task> list : subList) {

            int finalDelaySeconds = delaySeconds;

            for (Task task : list) {
                getTimer().newTimeout(timeout -> {
                    if (!isStop(taskName)) {
                        task.run();
                    }
                }, finalDelaySeconds, TimeUnit.SECONDS);
            }
            delaySeconds++;
        }
    }

    public static void submit(String taskName, List<Task> taskList, int executeNumPerSeconds) {

        List<List<Task>> subList = taskPartition(taskName, taskList, executeNumPerSeconds);
        for (List<Task> list : subList) {

            CommonUtils.sleep(1);

            for (Task task : list) {
                if (!isStop(taskName)) {
                    task.run();
                }
            }
        }
    }

    private static List<List<Task>> taskPartition(String taskName, List<Task> taskList, int executeNumPerSeconds) {
        int taskLimit = taskLimit(taskName);
        if (taskLimit == 0) {
            return Collections.emptyList();
        }

        if (isStop(taskName)) {
            return Collections.emptyList();
        }

        if (Objects.isNull(taskList) || taskList.isEmpty()) {
            return Collections.emptyList();
        }

        int executeNum = executeNumPerSeconds(taskName) > 0 ? executeNumPerSeconds(taskName) : executeNumPerSeconds;
        if (executeNum <= 0) {
            executeNum = 1;
        }

        List<List<Task>> subList = taskLimit > 0 ?
                Lists.partition(taskList.subList(0, Math.min(taskList.size(), taskLimit)), executeNum) :
                Lists.partition(taskList, executeNum);
        return subList;
    }

    private static boolean isStop(String taskName) {
        return ConfigUtil.getBooleanValue("ExecutionFrequencyUtils-" + taskName + "-stop", false);
    }

    private static int taskLimit(String taskName) {
        return ConfigUtil.getIntValue("ExecutionFrequencyUtils-" + taskName + "-limit", -1);
    }

    private static int executeNumPerSeconds(String taskName) {
        return ConfigUtil.getIntValue("ExecutionFrequencyUtils-" + taskName + "-executeNumPerSeconds", -1);
    }

}
