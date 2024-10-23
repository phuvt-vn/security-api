package com.example.main.api.server.xss;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

@RestController
@RequestMapping("/api/xss/danger/v1")
public class XssSimpleDangerApi {

    @GetMapping(value = "/greeting")
    public String greeting(@RequestParam(value = "name", required = true) String name) {
        var nowHour = LocalTime.now().getHour();
        return (nowHour >= 6 && nowHour < 18) ? ("good morning " + name ) : ("good night " + name);
    }

    @GetMapping(value = "/file")
    public Resource loadFile() {
        return new ClassPathResource("static/fileWithXss.csv");
    }

}
