package com.example.main.api.server.xss;

import com.example.main.api.response.xss.XssArticleSearchResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@RestController
@RequestMapping("/api/xss/danger/v1")
@CrossOrigin(origins = "http://localhost:3000")
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
