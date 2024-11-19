package dasturlash.uz.service;

import dasturlash.uz.entity.CommentLike;
import dasturlash.uz.entity.CommentLike;
import dasturlash.uz.enums.ReactionType;
import dasturlash.uz.exceptions.ArticleNotFoundException;
import dasturlash.uz.repository.ArticleRepository;
import dasturlash.uz.repository.CommentLikeRepository;
import dasturlash.uz.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static dasturlash.uz.util.SpringSecurityUtil.getCurrentUserId;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;


    public void likeArticle(String commentId) {
        Long profileId = getCurrentUserId();
        Optional<CommentLike> entity = commentLikeRepository.findByProfileIdAndCommentId(profileId, commentId);

        if (entity.isEmpty()) {
            CommentLike newLike = new CommentLike();
            newLike.setProfileId(profileId);
            newLike.setCommentId(commentId);
            newLike.setVisible(true);
            newLike.setType(ReactionType.LIKE);
            newLike.setCreatedDate(LocalDateTime.now());

            incrementLikeCount(commentId);

            commentLikeRepository.save(newLike);
        } else {
            CommentLike like = entity.get();
            if (!like.getVisible()) {
                commentLikeRepository.changeVisibility(commentId, true, profileId);
                incrementLikeCount(commentId);
            }
        }
    }

    public void dislikeArticle(String commentId) {

        Long profileId = getCurrentUserId();
        Optional<CommentLike> entity = commentLikeRepository.findByProfileIdAndCommentId(profileId, commentId);

        if (entity.isPresent()) {
            CommentLike like = entity.get();
            if (like.getVisible() && like.getType() == ReactionType.LIKE) {
                like.setVisible(false);
                like.setType(ReactionType.DISLIKE);

                decrementLikeCount(commentId);

                commentLikeRepository.save(like);
            } else if (!like.getVisible()){
                like.setVisible(true);
                like.setType(ReactionType.DISLIKE);

                incrementDislikeCount(commentId);
            }
        } else {
            CommentLike newLike = new CommentLike();
            newLike.setProfileId(profileId);
            newLike.setCommentId(commentId);
            newLike.setVisible(true);
            newLike.setType(ReactionType.DISLIKE);
            newLike.setCreatedDate(LocalDateTime.now());

            incrementDislikeCount(commentId);

            commentLikeRepository.save(newLike);


        }
    }

    public void removeLike(String articleId) {

        Long profileId = getCurrentUserId();

        Optional<CommentLike> entity = commentLikeRepository.findByProfileIdAndCommentId(profileId, articleId);

        if (entity.isPresent()) {
            CommentLike reaction = entity.get();

            if (reaction.getType() == ReactionType.LIKE) {
                Integer result = commentLikeRepository.changeVisibility(articleId, Boolean.FALSE, profileId);
                decrementLikeCount(articleId); // Or wherever decrementLikeCount is defined
                System.out.println(result);
            } else if (reaction.getType() == ReactionType.DISLIKE) {
                Integer result = commentLikeRepository.changeVisibility(articleId, Boolean.FALSE, profileId);
                decrementDislikeCount(articleId); // Or wherever decrementDislikeCount is defined
                System.out.println(result);
            }
        } else {
            throw new ArticleNotFoundException("No visible like or dislike found for the given profile and article.");
        }

    }

    private void incrementLikeCount(String articleId) {
        commentRepository.incrementLikeCount(articleId);
    }

    private void decrementLikeCount(String articleId) {
        commentRepository.decrementLikeCount(articleId);
    }

    private void incrementDislikeCount(String articleId) {
        commentRepository.incrementDislikeCount(articleId);
    }

    private void decrementDislikeCount(String articleId) {
        commentRepository.decrementDislikeCount(articleId);
    }


}
