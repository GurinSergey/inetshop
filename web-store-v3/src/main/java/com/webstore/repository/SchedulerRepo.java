package com.webstore.repository;

import com.webstore.domain.Scheduler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface SchedulerRepo extends JpaRepository<Scheduler,Long>{
     Scheduler findBySchedulerName(String name);
     List<Scheduler> findAllByNextStampAfterOrderBySchedulerId(Date nextDate);
     List<Scheduler> findAllByNextStampIsBeforeOrderBySchedulerId(Date nextDate);

     @Query("Select sch from Scheduler sch where sch.nextStamp<= ?1 and sch.schedulerType <> 0")
     List<Scheduler> findAllCurrentTask(Date date);

}
