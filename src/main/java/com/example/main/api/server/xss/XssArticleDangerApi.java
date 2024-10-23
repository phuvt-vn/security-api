package com.example.main.api.server.xss;

import com.example.main.api.response.xss.XssArticleSearchResponse;
import com.example.main.entity.XssArticle;
import com.example.main.repository.XssArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/xss/danger/v1/article")
@CrossOrigin(origins = "http://localhost:3000")
public class XssArticleDangerApi {

    @Autowired
    private XssArticleRepository xssArticleRepository;

    @PostMapping(value = "")
    public String create(@RequestBody (required = true )XssArticle xssArticle) {
        var saveArticle = xssArticleRepository.save(xssArticle);
        return saveArticle.toString();
    }

    @GetMapping(value = "")
    public XssArticleSearchResponse search(@RequestParam (required = true) String title){
        var article = xssArticleRepository.findByTitleContainsIgnoreCase(title);
        var resp = new XssArticleSearchResponse();
        resp.setResult(article);

        if (article.size() < 100){
            resp.setQueryCount(title + "<strong>ABC</strong>");
        }else {
            resp.setQueryCount(title + "<strong>XYZ</strong>");
        }

        return resp;
    }
}
