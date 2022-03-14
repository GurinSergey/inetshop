package com.webstore;

import com.webstore.service.StorageFileService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;


@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
public class WebstoreApplication implements CommandLineRunner{
	/*@Resource
	StorageFileService storageFileService;*/

	public static void main(String[] args) {
		SpringApplication.run(WebstoreApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		/*storageFileService.init();*/
	}
}
