package dasturlash.uz.controller;

import dasturlash.uz.service.ArticleLikeService;
import dasturlash.uz.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/like")
@RequiredArgsConstructor
public class CommentLikeController {
    private final CommentLikeService commentLikeService;

    @PostMapping("/comment-like")
    public ResponseEntity<Void> like(@RequestParam("commentId") String commentId) {
        commentLikeService.likeArticle(commentId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/comment-dislike")
    public ResponseEntity<Void> dislike(@RequestParam("commentId") String commentId) {
        commentLikeService.dislikeArticle(commentId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/remove-reaction")
    public ResponseEntity<Void> removeReaction(@RequestParam("commentId") String commentId) {
        commentLikeService.removeLike(commentId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
