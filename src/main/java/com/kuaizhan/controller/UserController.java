package com.kuaizhan.controller;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.lang.Thread.sleep;


/**
 * Created by lanzheng on 2017/11/28.
 */

@RestController
@Slf4j
public class UserController {

    @Autowired
    public WebDriver driver;

    @Value("${html.path}")
    public String htmlPath;

    @Value("${html.video}")
    public String videoHtml;

    @Value("${html.img}")
    public String imgHtml;

    @Value("${html.pdf}")
    public String pdfHtml;

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
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "action", required = true) String action
    ) {
        if (action.equals("pause") && type.equals("video")) {
            pauseVideo();
            return;
        } else if (action.equals("replay") && type.endsWith("video")) {
            replayVideo();
            return;
        }

        if (type.equals("video")) {
            showVideo(uriStr);
        } else if (type.equals("img")) {
            showImg(uriStr);
        } else if (type.equals("pdf")) {
            showPdf(uriStr);
        }

        return;
    }

    @Async
    public void showVideo(String uriStr) {
        driver.get(htmlPath + videoHtml);

        WebElement element_video = driver.findElement(By.tagName("video"));
        WebElement element_source = driver.findElement(By.tagName("source"));

        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;

        javascriptExecutor.executeScript("arguments[0].src='" + uriStr + "'", element_source);
        javascriptExecutor.executeScript("arguments[0].load()", element_video);
        javascriptExecutor.executeScript("arguments[0].play()", element_video);

        WebElement full = driver.findElement(By.tagName("button"));
        full.click();
    }

    @Async
    public void pauseVideo() {
        WebElement element_video = driver.findElement(By.tagName("video"));
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("arguments[0].pause()", element_video);
    }

    @Async
    public void replayVideo() {
        WebElement element_video = driver.findElement(By.tagName("video"));
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("arguments[0].play()", element_video);
    }

    @Async
    public void showImg(String uriStr) {
        driver.get(htmlPath + imgHtml);

        WebElement element_img = driver.findElement(By.tagName("img"));

        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;

        javascriptExecutor.executeScript("arguments[0].src='" + uriStr + "'", element_img);

        WebElement full = driver.findElement(By.tagName("button"));
        full.click();
    }

    @Async
    public void showPdf(String uriStr) {
        driver.get(pdfHtml + "?file=" + uriStr);


//        List<WebElement> element_buttons = driver.findElements(By.tagName("button"));
        WebElement webElement = driver.findElement(By.id("fullScreen"));
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        element_buttons.get(element_buttons.size()-1).click();
        webElement.click();
    }

    @Async
    public void showPdfNext() {
        WebElement webElement = driver.findElement(By.id("fullScreen_next"));
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        webElement.click();
    }

    @Async
    public void showPdfPrevious() {
        WebElement webElement = driver.findElement(By.id("fullScreen_previous"));
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        webElement.click();
    }


}
