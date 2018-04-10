package com.kuaizhan.Driver;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * Created by lanzheng on 2018/4/8.
 */
@Component
@Slf4j
public class ChromeDriver {

    @Bean
    public ChromeDriverService getChromeDriverService() {
        File file = new File("chromedriver");
        ChromeDriverService service = new ChromeDriverService.Builder().usingDriverExecutable(file)
                .usingAnyFreePort()
                .build();
        try {
            service.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return service;
    }

    @Bean
    public DesiredCapabilities getDesiredCapabilities() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-fullscreen");
        options.addArguments("disable-infobars");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        capabilities.setCapability("video", "True");
        return capabilities;
    }

    @Bean
    public WebDriver getWebDriver(ChromeDriverService service, DesiredCapabilities capabilities) {
        return new RemoteWebDriver(service.getUrl(), capabilities);
    }
}
