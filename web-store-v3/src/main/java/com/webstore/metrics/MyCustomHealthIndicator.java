package com.webstore.metrics;

import com.webstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class MyCustomHealthIndicator implements HealthIndicator {

    @Autowired
    public UserService userService;

    @Override
    public Health health() {

        int errorCode = 0;

        if (userService.findUserByEmail("zaragossasl@gmail.com") == null) {
            return Health.down().withDetail("Второй пользователь: ", "не найден второй пользователь").build();
        }
        return Health.up().withDetail("Второй пользователь: ","найден!").build();
    }
}
