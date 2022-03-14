package com.webstore.repository;

import com.webstore.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by SGurin on 17.06.2016.
 */
public interface UserRepo extends JpaRepository<User, Long> {
    public User findUserByUserName(String userName);
}
