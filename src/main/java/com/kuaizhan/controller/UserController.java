package com.kuaizhan.controller;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.*;

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
//        if (driver != null) {
//            driver.close();
//        }
//        driver = new RemoteWebDriver(chromeDriverService.getUrl(), desiredCapabilities);
//        driver.navigate().to(uriStr);
//        return;
        int timeout = 5; //秒.
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Boolean result = false;
        Future<Boolean> future = executor.submit(new MyJob(chromeDriverService, desiredCapabilities, uriStr));
        try {
            result = future.get(timeout * 1000, TimeUnit.MILLISECONDS);
            System.out.println(result);
        } catch (InterruptedException e) {
            System.out.println("线程中断出错。");
            future.cancel(true);// 中断执行此任务的线程
        } catch (ExecutionException e) {
            System.out.println("线程服务出错。");
            future.cancel(true);// 中断执行此任务的线程
        } catch (TimeoutException e) {// 超时异常
            System.out.println("超时。");
//            try {
//                Field runner = future.getClass().getDeclaredField("runner");
//                runner.setAccessible(true);
//                Thread execThread = (Thread) runner.get(future);
//                execThread.stop();
//                System.out.println("强行杀死进程");
//                System.gc();
//            } catch (NoSuchFieldException e1) {
//                e1.printStackTrace();
//            } catch (IllegalAccessException e1) {
//                e1.printStackTrace();
//            }
            future.cancel(true);
        } finally {
            System.out.println("线程服务关闭。");
            executor.shutdown();
        }
        return;
    }

    static class MyJob implements Callable<Boolean> {

        public ChromeDriverService chromeDriverService;
        public DesiredCapabilities desiredCapabilities;
        public String uriStr;

        public MyJob(ChromeDriverService chromeDriverService, DesiredCapabilities desiredCapabilities, String uriStr) {
            this.chromeDriverService = chromeDriverService;
            this.desiredCapabilities = desiredCapabilities;
            this.uriStr = uriStr;
        }

        public Boolean call() {
            if (driver != null) {
                driver.close();
            }
//            driver.close();
            driver = new RemoteWebDriver(chromeDriverService.getUrl(), desiredCapabilities);
            driver.navigate().to(uriStr);

            if (Thread.interrupted()) {
                return false;
            }
            return true;
        }
    }
}

