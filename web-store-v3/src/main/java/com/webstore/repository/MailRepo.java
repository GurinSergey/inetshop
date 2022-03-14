package com.webstore.repository;

import com.webstore.domain.PostMail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ALisimenko on 02.08.2016.
 */
public interface MailRepo extends JpaRepository<PostMail, Long> {
     PostMail findByDomen(String domen);
}
