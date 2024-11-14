package dasturlash.uz.service;

import dasturlash.uz.entity.ArticleLike;
import dasturlash.uz.enums.ReactionType;
import dasturlash.uz.exceptions.ArticleNotFoundException;
import dasturlash.uz.repository.ArticleLikeRepository;
import dasturlash.uz.repository.ArticleRepository;
import dasturlash.uz.request.ArticleLikeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static dasturlash.uz.util.SpringSecurityUtil.getUserId;

@Service
@RequiredArgsConstructor
public class ArticleLikeService {

    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleRepository articleRepository;


    public void likeArticle(String articleId) {
        Long profileId = getUserId();
        Optional<ArticleLike> entity = articleLikeRepository.findByProfileIdAndArticleId(profileId, articleId);

        if (entity.isEmpty()) {
            ArticleLike newLike = new ArticleLike();
            newLike.setProfileId(profileId);
            newLike.setArticleId(articleId);
            newLike.setVisible(true);
            newLike.setType(ReactionType.LIKE);
            newLike.setCreatedDate(LocalDateTime.now());

            incrementLikeCount(articleId);

            articleLikeRepository.save(newLike);
        } else {
            ArticleLike like = entity.get();
            if (!like.getVisible()) {
                articleLikeRepository.changeVisibility(articleId, true, profileId);
                incrementLikeCount(articleId);
            }
        }
    }

    public void dislikeArticle(String articleId) {

        Long profileId = getUserId();
        Optional<ArticleLike> entity = articleLikeRepository.findByProfileIdAndArticleId(profileId, articleId);

        if (entity.isPresent()) {
            ArticleLike like = entity.get();
            if (like.getVisible() && like.getType() == ReactionType.LIKE) {
                like.setVisible(false);
                like.setType(ReactionType.DISLIKE);

                decrementLikeCount(articleId);

                articleLikeRepository.save(like);
            } else if (!like.getVisible()){
                like.setVisible(true);
                like.setType(ReactionType.DISLIKE);

                incrementDislikeCount(articleId);
            }
        } else {
            ArticleLike newLike = new ArticleLike();
            newLike.setProfileId(profileId);
            newLike.setArticleId(articleId);
            newLike.setVisible(true);
            newLike.setType(ReactionType.DISLIKE);
            newLike.setCreatedDate(LocalDateTime.now());

            incrementDislikeCount(articleId);

            articleLikeRepository.save(newLike);


        }
    }

    public void removeLike(String articleId) {

        Long profileId = getUserId();

        Optional<ArticleLike> entity = articleLikeRepository.findByProfileIdAndArticleId(profileId, articleId);

        if (entity.isPresent()) {
            ArticleLike reaction = entity.get();

            if (reaction.getType() == ReactionType.LIKE) {
                Integer result = articleLikeRepository.changeVisibility(articleId, Boolean.FALSE, profileId);
                decrementLikeCount(articleId); // Or wherever decrementLikeCount is defined
                System.out.println(result);
            } else if (reaction.getType() == ReactionType.DISLIKE) {
                Integer result = articleLikeRepository.changeVisibility(articleId, Boolean.FALSE, profileId);
                decrementDislikeCount(articleId); // Or wherever decrementDislikeCount is defined
                System.out.println(result);
            }
        } else {
            throw new ArticleNotFoundException("No visible like or dislike found for the given profile and article.");
        }

    }

    private void incrementLikeCount(String articleId) {
        articleRepository.incrementLikeCount(articleId);
    }

    private void decrementLikeCount(String articleId) {
        articleRepository.decrementLikeCount(articleId);
    }

    private void incrementDislikeCount(String articleId) {
        articleRepository.incrementDislikeCount(articleId);
    }

    private void decrementDislikeCount(String articleId) {
        articleRepository.decrementDislikeCount(articleId);
    }
}
