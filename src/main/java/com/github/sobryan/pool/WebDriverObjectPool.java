package com.github.sobryan.pool;

import org.openqa.grid.common.GridRole;
import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.internal.utils.SelfRegisteringRemote;
import org.openqa.grid.internal.utils.configuration.GridHubConfiguration;
import org.openqa.grid.internal.utils.configuration.GridNodeConfiguration;
import org.openqa.grid.shared.GridNodeServer;
import org.openqa.grid.web.Hub;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.server.SeleniumServer;
import org.springframework.aop.target.CommonsPool2TargetSource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WebDriverObjectPool extends CommonsPool2TargetSource {


    public void initializeObjects() throws Exception {

        List<WebDriver> pool = new ArrayList<WebDriver>();
        initializeGridAndNode();

        for (int i = 0; i < this.getMinIdle(); i++) {
            pool.add((WebDriver) this.getTarget());
        }
        for (WebDriver instance : pool) {
            this.releaseTarget(instance);
        }
        pool.clear();
    }

    private void initializeGridAndNode() throws Exception {
        System.setProperty("webdriver.chrome.driver", "/home/morbo/Documents/webdrivers/current/chromedriver");
        System.setProperty("webdriver.gecko.driver","/home/morbo/Documents/webdrivers/current/geckodriver");


        //  HUB Configuration - org.openqa.grid.internal.utils.configuration.GridHubConfiguration
        GridHubConfiguration gridHubConfig = new GridHubConfiguration();
        gridHubConfig.host = "127.0.0.1";
        gridHubConfig.port = 4444;
        gridHubConfig.newSessionWaitTimeout = 50000;
        File hubJson = ResourceUtils.getFile("classpath:gridHub.json");
        gridHubConfig.loadFromJSON( hubJson.toString() );

        Hub hub = new Hub(gridHubConfig);
        hub.start();

        // initialize node
        // NODE Configuration - org.openqa.selenium.remote.server.SeleniumServer
        GridNodeConfiguration gridNodeConfiguration = new GridNodeConfiguration();

        File nodeJson = ResourceUtils.getFile("classpath:registerNode.json");
        gridNodeConfiguration.loadFromJSON( nodeJson.toString() );
        gridNodeConfiguration.role = GridRole.NODE.name();
//        gridNodeConfiguration.port = HUBPORT;

        RegistrationRequest request = new RegistrationRequest( gridNodeConfiguration );

        GridNodeServer node = new SeleniumServer( request.getConfiguration() );

        SelfRegisteringRemote remote = new SelfRegisteringRemote( request );
        remote.setRemoteServer( node );
        remote.startRemoteServer();
        remote.startRegistrationProcess();



    }

}
