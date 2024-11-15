package dasturlash.uz.controller;

import dasturlash.uz.dtos.CommentDTO;
import dasturlash.uz.dtos.CommentResponseDTO;
import dasturlash.uz.repository.customInterfaces.CommentMapper;
import dasturlash.uz.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("")
    @PreAuthorize("hasAnyRole('ROLE_MODERATOR', 'ROLE_ADMIN', 'ROLE_USER', 'ROLE_PUBLISHER')")
    public ResponseEntity<CommentResponseDTO> makeComment(@RequestBody CommentDTO request) {

        CommentResponseDTO response = commentService.makeComment(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_MODERATOR', 'ROLE_ADMIN', 'ROLE_USER', 'ROLE_PUBLISHER')")
    public ResponseEntity<CommentResponseDTO> getCommentById(@RequestParam("commentId") String commentId) {

        return ResponseEntity.ok().body(commentService.getCommentById(commentId));
    }

    @PutMapping("")
    @PreAuthorize("hasAnyRole('ROLE_MODERATOR', 'ROLE_ADMIN', 'ROLE_USER', 'ROLE_PUBLISHER')")
    public ResponseEntity<CommentResponseDTO> updateCommentById(@RequestParam("commentId") String commentId,
                                                                 @RequestBody CommentDTO request) {
        return ResponseEntity.ok().body(commentService.updateCommentById(commentId, request));
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Void> deleteCommentById(@RequestParam("commentId") String commendId){

        commentService.deleteCommentById(commendId);

        return ResponseEntity.noContent().build();
    }
    // Public access for everyone
    @GetMapping("/article/{articleId}/comments")
    public ResponseEntity<PageImpl<CommentResponseDTO>> getArticleComments(@PathVariable String articleId,
                                                                           @RequestParam(name = "page", defaultValue = "1") int page,
                                                                           @RequestParam(name = "size", defaultValue = "25") int size) {
        PageImpl<CommentResponseDTO> commentList = commentService.getCommentListByArticleId(articleId, page - 1, size);
        return ResponseEntity.ok().body(commentList);
    }

    // Admin-only access
    @GetMapping("/comments-list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PageImpl<CommentResponseDTO>> getAdminCommentListByArticleId(@RequestParam String articleId,
                                                                                       @RequestParam(name = "page", defaultValue = "1") int page,
                                                                                       @RequestParam(name = "size", defaultValue = "10") int size) {
        PageImpl<CommentResponseDTO> commentList = commentService.getCommentListByArticleId(articleId, page - 1, size);
        return ResponseEntity.ok().body(commentList);
    }



}
