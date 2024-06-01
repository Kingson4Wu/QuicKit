package com.kxw.quickit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.io.FileUtils;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.Required;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class ParallelTaskTest {


    @Rule
    public ContiPerfRule contiPerfRule = new ContiPerfRule();

    //The final test report is located at target/contiperf-report/index.html. Please open it using a web browser.
    @Test
    @PerfTest(invocations = 10000, threads = 100)//100 threads executing 10,000 times.
    @Required(throughput = 20, average = 50, totalTime = 5000, percentile99 = 10000)
    //The specified criteria include the following: the minimum requirement is to execute at least 20 tests per second, ensuring that the average execution time does not exceed 50ms. Additionally, the total execution time should not exceed 5 seconds, and it is expected that 99% of the tests will be completed within 10 seconds.
    public void test() {
        StringBuffer sb = new StringBuffer();
        ParallelTask.newTask()
                .addTask(() -> sb.append("f"))
                .addTask(() -> sb.append("g"))
                .addTask(() -> sb.append("v")).execute();
        Assert.assertEquals(sb.length(), 3);
    }


    @Test
    public void executor() throws ExecutionException, InterruptedException {
        ThreadFactory guavaThreadFactory = new ThreadFactoryBuilder().setNameFormat("rire-%d").build();

        ExecutorService executor = new ThreadPoolExecutor(4, 2 * 2,
                60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1024), guavaThreadFactory, (r, e) -> {
            throw new RejectedExecutionException(r.toString());
        });

        StringBuffer sb = new StringBuffer();
        ParallelTask.newTask()
                .addTask(() -> {
                    sb.append("f");
                    System.out.println(Thread.currentThread().getName());
                })
                .addTask(() -> {
                    sb.append("b");
                    System.out.println(Thread.currentThread().getName());
                })
                .addTask(() -> {
                    sb.append("n");
                    System.out.println(Thread.currentThread().getName());
                }).execute(executor);
        Assert.assertEquals(sb.length(), 3);

    }

    @Test
    public void execute() {

        FileUtils.deleteQuietly(new File("work" + File.separator + "ParallelTaskTest.txt"));

        IntStream.range(0, 100).parallel().forEach(i -> {

            StringBuffer sb = new StringBuffer();

            ParallelTask parallelTask =
                    ParallelTask.newTask()
                            .addTask(() -> {
                                try {
                                    Thread.sleep(100L);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                sb.append("f");
                                System.out.println(1);
                            })
                            .addTask(() -> {
                                try {
                                    Thread.sleep(100L);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                sb.append("g");
                                System.out.println(2);
                            })
                            .addTask(() -> {
                                try {
                                    Thread.sleep(100L);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                sb.append("v");
                                System.out.println(3);
                            });

            parallelTask.execute();

            System.out.println(i + "-------" + sb.length() + "----" + sb);
            try {
                FileUtils.writeStringToFile(new File("work" + File.separator + "ParallelTaskTest.txt"), i + "-------" + sb.length() + "----" + sb + "===" + parallelTask.taskSize() + "====" + "\n", Charset.defaultCharset(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Assert.assertEquals(sb.length(), 3);
        });


    }

}
