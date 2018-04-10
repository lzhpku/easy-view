package com.kuaizhan.controller;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.openqa.selenium.WebElement;


import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.openqa.selenium.JavascriptExecutor;


/**
 * Created by lanzheng on 2017/11/28.
 */

@RestController
@Slf4j
public class UserController {

    public static WebDriver driver;

    @Autowired
    public ChromeDriverService chromeDriverService;

    @Autowired
    public DesiredCapabilities desiredCapabilities;

    @RequestMapping("/get_uri")
    /**
     * https://pic.kuaizhan.com//g3/bd/2e/600c-7fcb-4ea7-b2e4-1861eb61b8c944.mp4
     * */
    public void getUri(
            @RequestParam(value = "uri", required = true) String uriStr
    ) {
        try {
            URI uri = new URI(uriStr);
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/test")
    /**
     * https://pic.kuaizhan.com//g3/bd/2e/600c-7fcb-4ea7-b2e4-1861eb61b8c944.mp4
     * http://pic.kuaizhan.com/004B0DD0D04852C4AB026DF7B4FD4A28.png
     * */
    public void test(
            @RequestParam(value = "uri", required = false) String uriStr
    ) {
          play(uriStr);
          return;
    }

    @Async
    public void play(String uriStr)
    {
        if (driver != null) {
            driver.close();
        }
        driver = new RemoteWebDriver(chromeDriverService.getUrl(), desiredCapabilities);
//        driver.navigate().to(uriStr);
        driver.get("file:///Users/Martin/project/JavaProjection/easy-view/video.html");

        WebElement full = driver.findElement(By.tagName("button"));
        full.click();

        WebElement element_video = driver.findElement(By.tagName("video"));
        //对video这个元素执行播放操作
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor)driver;

        javascriptExecutor.executeScript("arguments[0].play()", element_video);


        //对video这个元素执行暂停操作
//        javascriptExecutor.executeScript("arguments[0].pause()", element_video);
//        //对video这个元素执行重新加载视频的操作
//        javascriptExecutor.executeScript("arguments[0].load()", element_video);
    }



}
