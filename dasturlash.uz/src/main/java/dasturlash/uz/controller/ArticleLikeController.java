package dasturlash.uz.controller;


import dasturlash.uz.service.ArticleLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class ArticleLikeController {

    private final ArticleLikeService articleLikeService;

    @PostMapping("/article-like")
    public ResponseEntity<Void> likeArticle(@RequestParam("articleId") String articleId) {

        articleLikeService.likeArticle(articleId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/article-dislike")
    public ResponseEntity<Void> dislikeArticle(@RequestParam("articleId") String articleId) {

        articleLikeService.dislikeArticle(articleId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/remove-reaction")
    public ResponseEntity<Void> removeLike(@RequestParam("articleId") String articleId) {

        articleLikeService.removeLike(articleId);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }


}
