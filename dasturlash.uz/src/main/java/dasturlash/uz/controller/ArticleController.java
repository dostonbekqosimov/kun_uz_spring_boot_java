package dasturlash.uz.controller;

import dasturlash.uz.dtos.article.*;
import dasturlash.uz.entity.article.Article;
import dasturlash.uz.service.article.ArticleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ROLE_MODERATOR', 'ROLE_ADMIN')")
    public ResponseEntity<ArticleDTO> createArticle(@RequestBody @Valid ArticleRequestDTO request) {

        return ResponseEntity.ok().body(articleService.createArticle(request));


    }

    // 2. Update article (remove old image)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<ArticleDTO> updateArticle(@PathVariable String id, @RequestBody @Valid ArticleRequestDTO request) {

        ArticleDTO updatedArticle = articleService.updateArticle(id, request);
        return ResponseEntity.ok(updatedArticle);

    }

    // buni o'zim yozganman talablarda yo'q
    @GetMapping("/{articleId}")
    public ArticleDTO getArticleById(@PathVariable("articleId") String articleId) {

        return articleService.getArticleById(articleId);
    }

    // 3. Delete Article
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<Void> deleteArticle(@PathVariable String id) {

        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build(); // No Content

    }

    // 4. Change article status
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ROLE_PUBLISHER')")
    public ResponseEntity<ArticleDTO> changeStatus(@PathVariable String id, @RequestBody ArticleStatusRequestDTO statusRequest) {
        ArticleDTO updatedStatus = articleService.changeStatus(id, statusRequest.getStatus());
        return ResponseEntity.ok(updatedStatus);

    }

    // 5. Get Last 5 Articles By Types
    @GetMapping("/type/latest")
    public ResponseEntity<List<ArticleShortInfoDTO>> getLast5ArticlesByTypes(@RequestParam("typeId") Long articleTypeId) {

        List<ArticleShortInfoDTO> articles = articleService.getLastNArticlesByTypes(articleTypeId, 5);
        return ResponseEntity.ok(articles);
    }

    // 6. Get Last 3 Articles By Types
    @GetMapping("/type/latest/three")
    public ResponseEntity<List<ArticleShortInfoDTO>> getLast3ArticlesByTypes(@RequestParam("typeId") Long articleTypeId) {

        List<ArticleShortInfoDTO> articles = articleService.getLastNArticlesByTypes(articleTypeId, 3);
        return ResponseEntity.ok(articles);

    }

    // 7. Get Last 8 Articles excluding given IDs
    @GetMapping("/exclude")
    public ResponseEntity<List<ArticleShortInfoDTO>> getLast8ArticlesExcluding(@RequestParam("excludedIds") List<String> excludedIds) {

        List<ArticleShortInfoDTO> articles = articleService.getLast8ArticlesExcluding(excludedIds);
        return ResponseEntity.ok(articles);

    }

    // 8. Get Article By Id And Lang  [???]
    @GetMapping("/{id}/lang/{lang}")
    public ResponseEntity<ArticleFullInfoDTO> getArticleByIdAndLang(@PathVariable String articleId, @PathVariable String lang) {

        ArticleFullInfoDTO article = articleService.getArticleByIdAndLang(articleId, lang);
        return ResponseEntity.ok(article);

    }

    // 9. Get Last 4 Articles By Types and exclude given article id
    @GetMapping("/type/exclude/{articleId}")
    public ResponseEntity<List<ArticleShortInfoDTO>> getLast4ArticlesByTypesExcluding(@PathVariable String articleId, @RequestParam("type") Long type) {

        List<ArticleShortInfoDTO> articles = articleService.getLastNArticlesByTypesExcluding(type, articleId, 4);
        return ResponseEntity.ok(articles);

    }

    // 10. Get 4 most read articles [done]
    @GetMapping("/most-read")
    public ResponseEntity<List<ArticleShortInfoDTO>> getMostReadArticles() {

        List<ArticleShortInfoDTO> articles = articleService.getMostReadArticles(4);
        return ResponseEntity.ok(articles);

    }

    // 11. Get Last 4 Articles By TagName [...]
    @GetMapping("/tags/{tagName}")
    public ResponseEntity<List<ArticleShortInfoDTO>> getLast4ArticlesByTagName(@PathVariable String tagName) {

        List<ArticleShortInfoDTO> articles = articleService.getLast4ArticlesByTagName(tagName);
        return ResponseEntity.ok(articles);

    }

    // 12. Get Last 5 Articles By Type And By Region Key
    @GetMapping("/type/region")
    public ResponseEntity<List<ArticleShortInfoDTO>> getLast5ArticlesByTypesAndRegion(@RequestParam("articleTypeId") Long type, @RequestParam("regionId") Long regionId) {

        List<ArticleShortInfoDTO> articles = articleService.getLast5ArticlesByTypeAndRegion(type, regionId, 5);
        return ResponseEntity.ok(articles);

    }

    // 13. Get Article list by Region Key (Pagination)
    @GetMapping("/region/{regionId}")
    public ResponseEntity<PageImpl<ArticleShortInfoDTO>> getArticlesByRegion(@PathVariable Long regionId,
                                                                             @RequestParam(name = "page", defaultValue = "1") int page,
                                                                             @RequestParam(name = "size", defaultValue = "5") int size) {

        PageImpl<ArticleShortInfoDTO> articles = articleService.getArticlesByRegion(regionId, page - 1, size);
        return ResponseEntity.ok(articles);

    }

    // 14. Get Last 5 Articles By Category ID
    @GetMapping("/category/latest")
    public ResponseEntity<List<ArticleShortInfoDTO>> getLast5ArticlesByCategory(@RequestParam("categoryId") Long categoryId) {

        List<ArticleShortInfoDTO> articles = articleService.getLast5ArticlesByCategoryId(categoryId, 5);
        return ResponseEntity.ok(articles);

    }

    // 15. Get Article By Category ID (Pagination)
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PageImpl<ArticleShortInfoDTO>> getArticlesByCategory(@PathVariable Long categoryId,
                                                                               @RequestParam(name = "page", defaultValue = "1") int page,
                                                                               @RequestParam(name = "size", defaultValue = "5") int size) {

        PageImpl<ArticleShortInfoDTO> articles = articleService.getArticlesByCategory(categoryId, page - 1, size);
        return ResponseEntity.ok(articles);

    }

    // 16. Increase Article View Count
    @PostMapping("/{id}/view")
    public ResponseEntity<Void> increaseArticleViewCount(@PathVariable String id) {

        articleService.increaseArticleViewCount(id);
        return ResponseEntity.ok().build();

    }

    // 17. Increase Share View Count
    @PostMapping("/{id}/share")
    public ResponseEntity<Void> increaseShareViewCount(@PathVariable String id) {

        articleService.increaseShareViewCount(id);
        return ResponseEntity.ok().build();

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

        List<ArticleShortInfoDTO> articles = articleService.filterArticles(id, title, regionId, categoryId,
                createdDateFrom, createdDateTo, publishedDateFrom, publishedDateTo, moderatorId,
                publisherId, status, page, size);
        return ResponseEntity.ok(articles);

    }
}