package com.webstore.util.reflection;

import com.webstore.domain.Scheduler;
import com.webstore.service.SchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Calendar;
import java.util.Date;

public class ThreadReflectionMethod implements Runnable {
    final SchedulerService schedulerService;
    final Long schedulerId;
    final String objectName;
    final String methodName;
    final Class<?>[] paramsTypes;
    final Boolean isGetFirst;
    final Boolean convertToPrimitive;
    final Object[] paramsA;
    private static final Logger log = LoggerFactory.getLogger(ThreadReflectionMethod.class);



    public ThreadReflectionMethod(SchedulerService schedulerService, Long schedulerId , String objectName, String methodName, Class<?>[] paramsTypes, Boolean isGetFirst, Boolean convertToPrimitive, Object...paramsA) {
        this.schedulerService = schedulerService;
        this.schedulerId = schedulerId;
        this.objectName = objectName;
        this.methodName = methodName;
        this.paramsTypes = paramsTypes;
        this.isGetFirst = isGetFirst;
        this.convertToPrimitive = convertToPrimitive;
        this.paramsA = paramsA;
    }

    @Override
    public void run() {
        ReflectionMethods reflectionMethods = new ReflectionMethods();
        Scheduler scheduler = schedulerService.find(this.schedulerId);
        //сразу обновим старт задачи и обнулим новый период
        scheduler.setNextStamp(null);
        scheduler.setWorkStartTime(new Date());



        schedulerService.update(scheduler);

        try {
            reflectionMethods.execMethodBySignature(objectName,methodName,paramsTypes,isGetFirst,convertToPrimitive,paramsA);
            log.info("runThreadReflectionMethod{}");
            log.info("thread start{}",methodName);
            log.info("thread id{}",this.schedulerId);

            scheduler.setWorkEndTime(new Date());

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(scheduler.getTypePeriod(), scheduler.getPeriod());
            scheduler.setNextStamp(cal.getTime());

            schedulerService.update(scheduler);
            log.info("thread end{}",methodName);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }
}
