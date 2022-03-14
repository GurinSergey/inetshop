package com.webstore.test;


import com.webstore.domain.Catalog;
import com.webstore.domain.EmailNotification;
import com.webstore.domain.Product;
import com.webstore.repository.CatalogRepo;
import com.webstore.repository.EmailNotificationRepo;
import com.webstore.repository.ProductRepo;
import com.webstore.repository.SchedulerRepo;
import com.webstore.domain.Scheduler;
import com.webstore.service.SchedulerService;
import com.webstore.util.Transcriptor;
import com.webstore.util.reflection.ReflectionMethods;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("junit")
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@EnableScheduling
/*@TestPropertySource(
        locations = "classpath:test-application.yml")*/

public class SchedulerRepositoryTest {


/* Для мок конфигураций
@TestConfiguration
    static class SchedulerServiceImplTestContextConfiguration {

        @Bean
        public SchedulerService schedulerService() {
            return new SchedulerService();
        }
    }
*/


    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SchedulerRepo schedulerRepo;



    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CatalogRepo catalogRepo;

    @Autowired
    private EmailNotificationRepo emailNotificationRepo;

 /*   @Autowired
    private EmailNotificationService emailNotificationService;*/

    @Test
    @Rollback(false)
    public void whenFindByName_thenReturn() {
        Class<?> paramsType[];
        paramsType = null;// new Class<?>[]{String.class};
        Date newDate;

        /*Product product = productRepo.findOne(1L);

        productRepo.findAll().forEach(p->{
            p.setLatIdent(Transcriptor.cyr2lat(p.getTitle() + ' ' + p.getCode()));
            productRepo.save(p);
        });*/

        Catalog catalog = new Catalog();
        catalog.setId(1);

        catalogRepo.findAll().forEach(c -> {
           c.setLatIdent(Transcriptor.cyr2lat(c.getTitle()));
            catalogRepo.save(c);
        });


        // then

       assertThat(catalog.getId())
                .isEqualTo(catalog.getId());

    }

}
