package com.kuaizhan.controller;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by lanzheng on 2017/11/28.
 */

@RestController
@Slf4j
public class UserController {

    public static WebDriver driver;

    public static String videoHtml = "file:///Users/lanzheng/workspace/easy-view/video.html";
    public static String imgHtml = "file:///Users/lanzheng/workspace/easy-view/img.html";
    public static String pdfHtml = "file:///Users/lanzheng/workspace/easy-view/pdf.html";

    @Autowired
    public ChromeDriverService chromeDriverService;

    @Autowired
    public DesiredCapabilities desiredCapabilities;

    @RequestMapping("/test")
    /**
     * https://pic.kuaizhan.com//g3/bd/2e/600c-7fcb-4ea7-b2e4-1861eb61b8c944.mp4
     * http://pic.kuaizhan.com/004B0DD0D04852C4AB026DF7B4FD4A28.png
     * http://pic.kuaizhan.com//g3/75/27/5163-9bef-418d-ae48-09cc6d6b4a9f18.pdf
     * */
    public void test(
            @RequestParam(value = "uri", required = true) String uriStr,
            @RequestParam(value = "type", required = true) String type
    ) {
        if (type.equals("video")) {
            playVideo(uriStr);
        } else if (type.equals("img")) {
            playImg(uriStr);
        } else if (type.equals("pdf")) {
            playPdf(uriStr);
        }
        return;
    }

    @Async
    public void playVideo(String uriStr)
    {
        if (driver != null) {
            driver.close();
        }
        driver = new RemoteWebDriver(chromeDriverService.getUrl(), desiredCapabilities);
        driver.get(videoHtml);

        WebElement full = driver.findElement(By.tagName("button"));
        full.click();

        WebElement element_video = driver.findElement(By.tagName("video"));
        WebElement element_source = driver.findElement(By.tagName("source"));

        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;

        javascriptExecutor.executeScript("arguments[0].src='" + uriStr + "'", element_source);
        javascriptExecutor.executeScript("arguments[0].load()", element_video);
        javascriptExecutor.executeScript("arguments[0].play()", element_video);

//      javascriptExecutor.executeScript("arguments[0].pause()", element_video);
    }

    @Async
    public void playImg(String uriStr)
    {
        if (driver != null) {
            driver.close();
        }
        driver = new RemoteWebDriver(chromeDriverService.getUrl(), desiredCapabilities);
        driver.get(imgHtml);

        WebElement full = driver.findElement(By.tagName("button"));
        full.click();

        WebElement element_img = driver.findElement(By.tagName("img"));

        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;

        javascriptExecutor.executeScript("arguments[0].src='" + uriStr + "'", element_img);
        javascriptExecutor.executeScript("arguments[0].load()", element_img);
    }

    @Async
    public void playPdf(String uriStr)
    {
        if (driver != null) {
            driver.close();
        }
        driver = new RemoteWebDriver(chromeDriverService.getUrl(), desiredCapabilities);
        driver.get(pdfHtml);

//        WebElement full = driver.findElement(By.tagName("button"));
//        full.click();
//
//        WebElement element_pdf = driver.findElement(By.tagName("embed"));
//
//        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
//
//        javascriptExecutor.executeScript("arguments[0].src='" + uriStr + "'", element_pdf);
//        javascriptExecutor.executeScript("arguments[0].load()", element_pdf);
    }

}
