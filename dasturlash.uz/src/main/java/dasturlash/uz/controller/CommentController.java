package dasturlash.uz.controller;

import dasturlash.uz.dtos.CommentDTO;
import dasturlash.uz.dtos.CommentResponseDTO;
import dasturlash.uz.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

}
