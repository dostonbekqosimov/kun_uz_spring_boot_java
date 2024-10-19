package dasturlash.uz.controller;

import dasturlash.uz.dtos.ArticleTypeCreationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/article-type")
public class ArticleTypeController {

    @PostMapping({"", "/"})
    public ResponseEntity<?> createArticleType(@RequestBody ArticleTypeCreationDTO creationDTO) {

    }
}
