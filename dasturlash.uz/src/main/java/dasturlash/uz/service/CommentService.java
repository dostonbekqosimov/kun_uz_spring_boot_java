package dasturlash.uz.service;

import dasturlash.uz.dtos.CommentDTO;
import dasturlash.uz.dtos.CommentResponseDTO;
import dasturlash.uz.entity.Comment;
import dasturlash.uz.enums.Role;
import dasturlash.uz.exceptions.DataNotFoundException;
import dasturlash.uz.exceptions.ForbiddenException;
import dasturlash.uz.repository.CommentRepository;
import dasturlash.uz.repository.customInterfaces.CommentMapper;
import dasturlash.uz.util.SpringSecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static dasturlash.uz.util.SpringSecurityUtil.getCurrentUserId;
import static dasturlash.uz.util.SpringSecurityUtil.getCurrentUserRole;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentResponseDTO makeComment(CommentDTO request) {

        Comment newComment = new Comment();

        newComment.setContent(request.getContent());
        newComment.setProfileId(getCurrentUserId());
        newComment.setArticleId(request.getArticleId());
        newComment.setReplyId(request.getReplyId());
        newComment.setVisible(Boolean.TRUE);
        newComment.setCreatedDate(LocalDateTime.now());

        Comment savedComment = commentRepository.save(newComment);

        // Map to Response DTO
        return commentToDto(savedComment);
    }

    public CommentResponseDTO getCommentById(String commentId) {

        Comment currentComment = getCommentEntityById(commentId);
        return commentToDto(currentComment);


    }

    public CommentResponseDTO updateCommentById(String commentId, CommentDTO request) {

        Comment oldComment = getCommentEntityById(commentId);


        oldComment.setContent(request.getContent());
        oldComment.setUpdatedDate(LocalDateTime.now());
        Comment savedComment = commentRepository.save(oldComment);

        return commentToDto(savedComment);
    }

    public void deleteCommentById(String commentId) {
        // Fetch the comment entity
        Comment oldComment = getCommentEntityById(commentId);


        // Validate if the current user is authorized to hide the comment
        if (!isUserAuthorizedToModifyComment(oldComment)) {
            throw new ForbiddenException("You are not authorized to delete this comment.");
        }

        // Update visibility
        int rowsUpdated = commentRepository.updateVisibilityById(Boolean.FALSE, commentId);
        oldComment.setUpdatedDate(LocalDateTime.now());
        log.info("Rows updated: {}", rowsUpdated);

    }

    // Helper method to check authorization
    private boolean isUserAuthorizedToModifyComment(Comment comment) {
        Long currentUserId = getCurrentUserId();
        Role currentUserRole = getCurrentUserRole();

        return comment.getProfileId().equals(currentUserId)
                && (currentUserRole == Role.ROLE_ADMIN
                || currentUserRole == Role.ROLE_USER);
    }

    public PageImpl<CommentResponseDTO> getCommentListByArticleId(String articleId, int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdDate").descending());

        PageImpl<CommentMapper> resultList = commentRepository.findCommentListByArticleId(articleId, pageRequest);
        List<CommentResponseDTO> response = resultList.stream()
                .map(this::mapCommentMapperToDto)
                .toList();

        return new PageImpl<>(response, pageRequest, resultList.getTotalElements());
    }

    private CommentResponseDTO mapCommentMapperToDto(CommentMapper commentMapper) {
        CommentResponseDTO response = new CommentResponseDTO();
        response.setId(commentMapper.getId());
        response.setReplyId(commentMapper.getReplyId());
        response.setContent(commentMapper.getContent());
        response.setCreatedDate(commentMapper.getCreatedDate());
        response.setUpdatedDate(commentMapper.getUpdatedDate());
        response.setUserInfo(commentMapper.getProfile());
        return response;
    }


    private Comment getCommentEntityById(String commentId) {


        return commentRepository.findByIdAndVisibleTrue(commentId)
                .orElseThrow(() -> new DataNotFoundException("Comment not found with id: " + commentId));

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
