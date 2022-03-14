package com.webstore.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by DVaschenko on 15.06.2016.
 */
public class Runnable {
    public static void main(String[] args) throws JsonProcessingException {


        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.refresh();

    }
}
