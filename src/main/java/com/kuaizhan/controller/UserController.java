package com.kuaizhan.controller;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.interactions.Actions;
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

    @RequestMapping("/apiv1")
    /**
     * @params uri 资源名
     * @params type 资源类型, video, img, pdf, link
     * @params action 操作类型, show, pause, replay, previous, next, shift
     * @prames index 全景图下标
     * */
    public void test(
            @RequestParam(value = "uri", required = true) String uriStr,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "action", required = true) String action,
            @RequestParam(value = "index", required = true) int index,
            @RequestParam(value = "direction", required = false) String direction
    ) {
        if (action.equals("pause") && type.equals("video")) {
            pauseVideo();
            return;
        } else if (action.equals("replay") && type.endsWith("video")) {
            replayVideo();
            return;
        }

        if (action.equals("next") && type.equals("pdf")) {
            nextPdf();
            return;
        } else if (action.equals("previous") && type.equals("pdf")) {
            previousPdf();
            return;
        }

        if (action.equals("shift") && type.equals("link")) {
            shiftLink(direction, index);
            return;
        }

        if (type.equals("video")) {
            showVideo(uriStr);
        } else if (type.equals("img")) {
            showImg(uriStr);
        } else if (type.equals("pdf")) {
            showPdf(uriStr);
        } else if (type.equals("link")) {
            showLink(uriStr);
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
        WebElement webElement = driver.findElement(By.id("fullScreen"));
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        webElement.click();
    }

    @Async
    public void nextPdf() {
        WebElement webElement = driver.findElement(By.id("fullScreen_next"));
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("arguments[0].click()", webElement);
    }

    @Async
    public void previousPdf() {
        WebElement webElement = driver.findElement(By.id("fullScreen_previous"));
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("arguments[0].click()", webElement);
    }

    @Async
    public void showLink(String uriStr) {
        driver.get(uriStr);
    }

    @Async
    public void shiftLink(String direction, int index) {
        List<WebElement> webElements = driver.findElements(By.className("scenebar-thumb"));
        int size = webElements.size();
        if (index >= 0 && index < size) {
            webElements.get(index).click();
        }

        Actions action = new Actions(driver);
        if (direction != null) {
            JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;

            WebElement webElement = driver.findElement(By.tagName("body"));
            if( "up".equals(direction)) {
                javascriptExecutor.executeScript("var e1 = new Event(\"keydown\");\n" +
                        "  e1.keyCode=38;\n" +
                        "  e1.which=e1.keyCode;\n" +
                        "  document.dispatchEvent(e1);\n" +
                        "var e2 = new Event(\"keyup\");\n" +
                        "  e2.keyCode=38;\n" +
                        "  e2.which=e2.keyCode;\n" +
                        "  setTimeout(() => {document.dispatchEvent(e2)}, 500);\n");
            }else if("down".equals(direction)) {
                javascriptExecutor.executeScript("var e1 = new Event(\"keydown\");\n" +
                        "  e1.keyCode=40;\n" +
                        "  e1.which=e1.keyCode;\n" +
                        "  document.dispatchEvent(e1);\n" +
                        "var e2 = new Event(\"keyup\");\n" +
                        "  e2.keyCode=40;\n" +
                        "  e2.which=e2.keyCode;\n" +
                        "  setTimeout(() => {document.dispatchEvent(e2)}, 500);\n");
            }else if("left".equals(direction)){
                javascriptExecutor.executeScript("var e1 = new Event(\"keydown\");\n" +
                        "  e1.keyCode=37;\n" +
                        "  e1.which=e1.keyCode;\n" +
                        "  document.dispatchEvent(e1);\n" +
                        "var e2 = new Event(\"keyup\");\n" +
                        "  e2.keyCode=37;\n" +
                        "  e2.which=e2.keyCode;\n" +
                        "  setTimeout(() => {document.dispatchEvent(e2)}, 500);\n");
            }else if("right".equals(direction)){
                javascriptExecutor.executeScript("var e1 = new Event(\"keydown\");\n" +
                        "  e1.keyCode=39;\n" +
                        "  e1.which=e1.keyCode;\n" +
                        "  document.dispatchEvent(e1);\n" +
                        "var e2 = new Event(\"keyup\");\n" +
                        "  e2.keyCode=39;\n" +
                        "  e2.which=e2.keyCode;\n" +
                        "  setTimeout(() => {document.dispatchEvent(e2)}, 500);\n");
            }
        }

    }
}
