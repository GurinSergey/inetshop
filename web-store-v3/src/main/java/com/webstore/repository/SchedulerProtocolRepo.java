package com.webstore.repository;

import com.webstore.domain.SchedulerProtocol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchedulerProtocolRepo extends JpaRepository<SchedulerProtocol,Long> {
}
