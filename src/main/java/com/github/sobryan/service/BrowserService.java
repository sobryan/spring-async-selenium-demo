package com.github.sobryan.service;


import org.openqa.selenium.WebDriver;
import org.springframework.aop.target.CommonsPool2TargetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.Future;

@Service
public class BrowserService {


    @Autowired
    @Qualifier("poolTargetSourceWebDriver")
    CommonsPool2TargetSource poolTargetSourceWebDriver;

    @Async("linkCollectorTaskExecutor")
    public Future<ArrayList<String>> collectLinksFromPage(String page) throws Exception {
        WebDriver driver = (WebDriver) poolTargetSourceWebDriver.getTarget();
        driver.get(page);

        // echo for demo
        ArrayList<String> links = new ArrayList<String>();
        links.add("Result echo" + page.split(" ")[0]);

        poolTargetSourceWebDriver.releaseTarget(driver);
        return new AsyncResult<ArrayList<String>>(links);
    }

}
