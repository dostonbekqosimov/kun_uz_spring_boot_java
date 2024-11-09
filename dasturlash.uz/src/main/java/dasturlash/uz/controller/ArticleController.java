package dasturlash.uz.controller;

import dasturlash.uz.dtos.article.*;
import dasturlash.uz.entity.article.Article;
import dasturlash.uz.service.article.ArticleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    // 1. CREATE article

    // jwt da moderator id ni berib yuborish kerak yani profile id
    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody @Valid ArticleRequestDTO request) {

        return ResponseEntity.ok().body(articleService.createArticle(request));


    }

    // 2. Update article (remove old image)
    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable String id, @RequestBody @Valid ArticleRequestDTO request) {

            Article updatedArticle = articleService.updateArticle(id, request);
            return ResponseEntity.ok(updatedArticle);

    }

    @GetMapping("/{articleId}")
    public ArticleDTO getArticleById(@PathVariable("articleId") String articleId) {

        return articleService.getArticleById(articleId);
    }

    // 3. Delete Article
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable String id) {
        try {
            articleService.deleteArticle(id);
            return ResponseEntity.noContent().build(); // No Content
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 4. Change article status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Article> changeStatus(@PathVariable String id, @RequestBody ArticleStatusRequestDTO statusRequest) {
        try {
            Article updatedStatus = articleService.changeStatus(id, statusRequest.getStatus());
            return ResponseEntity.ok(updatedStatus);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 5. Get Last 5 Articles By Types
    @GetMapping("/types/latest")
    public ResponseEntity<List<ArticleShortInfoDTO>> getLast5ArticlesByTypes(@RequestParam List<String> types) {
        try {
            List<ArticleShortInfoDTO> articles = articleService.getLastNArticlesByTypes(types, 5);
            return ResponseEntity.ok(articles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 6. Get Last 3 Articles By Types
    @GetMapping("/types/latest/three")
    public ResponseEntity<List<ArticleShortInfoDTO>> getLast3ArticlesByTypes(@RequestParam List<String> types) {
        try {
            List<ArticleShortInfoDTO> articles = articleService.getLastNArticlesByTypes(types, 3);
            return ResponseEntity.ok(articles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 7. Get Last 8 Articles excluding given IDs
    @GetMapping("/exclude")
    public ResponseEntity<List<ArticleShortInfoDTO>> getLast8ArticlesExcluding(@RequestParam List<String> excludedIds) {
        try {
            List<ArticleShortInfoDTO> articles = articleService.getLast8ArticlesExcluding(excludedIds);
            return ResponseEntity.ok(articles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 8. Get Article By Id And Lang
    @GetMapping("/{id}/lang/{lang}")
    public ResponseEntity<ArticleFullInfoDTO> getArticleByIdAndLang(@PathVariable String id, @PathVariable String lang) {
        try {
            ArticleFullInfoDTO article = articleService.getArticleByIdAndLang(id, lang);
            return ResponseEntity.ok(article);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 9. Get Last 4 Articles By Types and exclude given article id
    @GetMapping("/types/exclude/{id}")
    public ResponseEntity<List<ArticleShortInfoDTO>> getLast4ArticlesByTypesExcluding(@PathVariable String id, @RequestParam List<String> types) {
        try {
            List<ArticleShortInfoDTO> articles = articleService.getLastNArticlesByTypesExcluding(types, id, 4);
            return ResponseEntity.ok(articles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 10. Get 4 most read articles
    @GetMapping("/most-read")
    public ResponseEntity<List<ArticleShortInfoDTO>> getMostReadArticles() {
        try {
            List<ArticleShortInfoDTO> articles = articleService.getMostReadArticles(4);
            return ResponseEntity.ok(articles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 11. Get Last 4 Articles By TagName
    @GetMapping("/tags/{tagName}")
    public ResponseEntity<List<ArticleShortInfoDTO>> getLast4ArticlesByTagName(@PathVariable String tagName) {
        try {
            List<ArticleShortInfoDTO> articles = articleService.getLast4ArticlesByTagName(tagName);
            return ResponseEntity.ok(articles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 12. Get Last 5 Articles By Types And By Region Key
    @GetMapping("/types/region")
    public ResponseEntity<List<ArticleShortInfoDTO>> getLast5ArticlesByTypesAndRegion(@RequestParam List<String> types, @RequestParam String regionKey) {
        try {
            List<ArticleShortInfoDTO> articles = articleService.getLast5ArticlesByTypesAndRegion(types, regionKey);
            return ResponseEntity.ok(articles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 13. Get Article list by Region Key (Pagination)
    @GetMapping("/region/{regionKey}")
    public ResponseEntity<List<ArticleShortInfoDTO>> getArticlesByRegion(@PathVariable String regionKey,
                                                                      @RequestParam int page,
                                                                      @RequestParam int size) {
        try {
            List<ArticleShortInfoDTO> articles = articleService.getArticlesByRegion(regionKey, page, size);
            return ResponseEntity.ok(articles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 14. Get Last 5 Articles By Category Key
    @GetMapping("/category/latest")
    public ResponseEntity<List<ArticleShortInfoDTO>> getLast5ArticlesByCategory() {
        try {
            List<ArticleShortInfoDTO> articles = articleService.getLast5ArticlesByCategory();
            return ResponseEntity.ok(articles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 15. Get Article By Category Key (Pagination)
    @GetMapping("/category/{categoryKey}")
    public ResponseEntity<List<ArticleShortInfoDTO>> getArticlesByCategory(@PathVariable String categoryKey,
                                                                        @RequestParam int page,
                                                                        @RequestParam int size) {
        try {
            List<ArticleShortInfoDTO> articles = articleService.getArticlesByCategory(categoryKey, page, size);
            return ResponseEntity.ok(articles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 16. Increase Article View Count
    @PostMapping("/{id}/view")
    public ResponseEntity<Void> increaseArticleViewCount(@PathVariable String id) {
        try {
            articleService.increaseArticleViewCount(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 17. Increase Share View Count
    @PostMapping("/{id}/share")
    public ResponseEntity<Void> increaseShareViewCount(@PathVariable String id) {
        try {
            articleService.increaseShareViewCount(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 18. Filter Articles with Pagination
    @GetMapping("/filter")
    public ResponseEntity<List<ArticleShortInfoDTO>> filterArticles(@RequestParam(required = false) String id,
                                                                 @RequestParam(required = false) String title,
                                                                 @RequestParam(required = false) String regionId,
                                                                 @RequestParam(required = false) String categoryId,
                                                                 @RequestParam(required = false) String createdDateFrom,
                                                                 @RequestParam(required = false) String createdDateTo,
                                                                 @RequestParam(required = false) String publishedDateFrom,
                                                                 @RequestParam(required = false) String publishedDateTo,
                                                                 @RequestParam(required = false) String moderatorId,
                                                                 @RequestParam(required = false) String publisherId,
                                                                 @RequestParam(required = false) String status,
                                                                 @RequestParam int page,
                                                                 @RequestParam int size) {
        try {
            List<ArticleShortInfoDTO> articles = articleService.filterArticles(id, title, regionId, categoryId,
                    createdDateFrom, createdDateTo, publishedDateFrom, publishedDateTo, moderatorId,
                    publisherId, status, page, size);
            return ResponseEntity.ok(articles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}