package dasturlash.uz.service;

import dasturlash.uz.dtos.CommentDTO;
import dasturlash.uz.dtos.CommentResponseDTO;
import dasturlash.uz.entity.Comment;
import dasturlash.uz.exceptions.DataNotFoundException;
import dasturlash.uz.repository.CommentRepository;
import dasturlash.uz.util.SpringSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static dasturlash.uz.util.SpringSecurityUtil.getUserId;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentResponseDTO makeComment(CommentDTO request) {

        Comment newComment = new Comment();

        newComment.setContent(request.getContent());
        newComment.setProfileId(getUserId());
        newComment.setArticleId(request.getArticleId());
        newComment.setReplyId(request.getReplyId());
        newComment.setVisible(Boolean.TRUE);
        newComment.setCreatedDate(LocalDateTime.now());

        Comment savedComment = commentRepository.save(newComment);

        // Map to Response DTO
        return commentToDto(savedComment);
    }

    public CommentResponseDTO getCommentById(String commentId) {

        Comment entity = commentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("Comment not found with id: " + commentId));

        return commentToDto(entity);


    }

    public CommentResponseDTO updateCommentById(String commentId, CommentDTO request) {

        Comment entity = commentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("Comment not found with id: " + commentId));

        entity.setContent(request.getContent());
        Comment savedComment = commentRepository.save(entity);

        return commentToDto(savedComment);
    }




    private CommentResponseDTO commentToDto(Comment savedComment) {
        CommentResponseDTO response = new CommentResponseDTO();
        response.setId(savedComment.getId());
        response.setContent(savedComment.getContent());
        response.setArticleId(savedComment.getArticleId());
        response.setReplyId(savedComment.getReplyId());
        response.setUserInfo(SpringSecurityUtil.getCurrentUserShortInfo());
        response.setCreatedDate(savedComment.getCreatedDate());
        return response;
    }


}
