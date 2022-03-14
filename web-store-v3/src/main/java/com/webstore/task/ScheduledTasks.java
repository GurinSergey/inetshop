package com.webstore.task;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.webstore.domain.Scheduler;
import com.webstore.service.SchedulerService;
import com.webstore.util.reflection.ThreadReflectionMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;


@Component
public class ScheduledTasks {

    @Autowired
    public SchedulerService schedulerService;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @PostConstruct
    public void initializeNextStamp() {

        List<Scheduler> tasks = schedulerService.getAll();

//Если сервер был рестратован, может быть так что поле NextStamp будет null
//поэтому проинициализируем такие даты

        for (Scheduler task : tasks) {

            if (task.getNextStamp() == null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(task.getWorkEndTime());
                cal.add(task.getTypePeriod(), task.getPeriod());
                task.setNextStamp(cal.getTime());
                schedulerService.update(task);
            }
        }

    }

    @Scheduled(fixedDelay = 100000)
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
    }

    @Scheduled(fixedDelay = 100000)
    public void runTaskByScheduler() {

        List<Scheduler> tasks = schedulerService.findAllCurrentTask(new Date());

        log.info("runTaskByScheduler {}", dateFormat.format(new Date()));
        final ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("MainThreadInetshop")
                .build();
        final ExecutorService executor = Executors.newFixedThreadPool(8, threadFactory);
        for (Scheduler scheduler : tasks) {

            executor.submit(new ThreadReflectionMethod(
                    schedulerService,
                    scheduler.getSchedulerId(),
                    scheduler.getObject(),
                    scheduler.getMethod(),
                    null,
                    true,
                    true,
                    (Object[]) scheduler.getParamsArrayString()));

        }

    }
}
