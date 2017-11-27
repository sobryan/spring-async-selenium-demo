package com.github.sobryan;

import com.github.sobryan.service.BrowserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.concurrent.Future;

@SpringBootApplication
public class Application implements CommandLineRunner{

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Autowired
    BrowserService browserService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Override
    public void run(String... args) throws Exception {

        ArrayList<String> urls = new ArrayList<>();
        urls.add("https://www.google.com");
        urls.add("https://news.google.com");
        urls.add("https://adafruit.com");
        urls.add("https://developer.google.com");
        urls.add("https://www.weather.com");

        ArrayList<Future<ArrayList<String>>> futures = new ArrayList<>();
        for(int i=0; i<5;i++) {
            Future<ArrayList<String>> future = browserService.collectLinksFromPage(urls.get(i));
            futures.add(future);
        }

        futures.forEach(result -> {
            try{
                result.get();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        });

        System.out.println("Completed the operation");

    }
}
