package com.webstore.repository;

import com.webstore.domain.EmailNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailNotificationRepo extends JpaRepository<EmailNotification,Long> {
    @Query("Select en from EmailNotification en where en.issend is null")
    List<EmailNotification> findAllCurrentNotification();

}
