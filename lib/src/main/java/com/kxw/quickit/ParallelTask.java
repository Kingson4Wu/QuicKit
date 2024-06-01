package com.kxw.quickit;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class ParallelTask {

    private List<Task> taskList;

    public static ParallelTask newTask() {

        ParallelTask parallelTask = new ParallelTask();
        parallelTask.taskList = new ArrayList<>(4);

        return parallelTask;
    }

    public ParallelTask addTask(Task task) {
        taskList.add(task);
        return this;
    }

    public void execute() {
        if (CollectionUtils.isNotEmpty(taskList)) {

            taskList.parallelStream().forEach(Task::run);
        }
    }

    public void execute(ExecutorService executor) throws InterruptedException, ExecutionException {
        List<Future<Void>> futureList = executor.invokeAll(taskList.stream().map(task -> (Callable<Void>) () -> {
            task.run();
            return null;
        }).collect(Collectors.toList()));
        for (Future<Void> f : futureList) {
            f.get();
        }
    }

    public int taskSize() {
        return Optional.ofNullable(taskList).map(List::size).orElse(0);
    }

}
