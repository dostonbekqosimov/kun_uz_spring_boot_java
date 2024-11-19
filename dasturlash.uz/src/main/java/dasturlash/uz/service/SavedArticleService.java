package dasturlash.uz.service;

import dasturlash.uz.dtos.article.ArticleShortInfoDTO;
import dasturlash.uz.entity.SavedArticles;
import dasturlash.uz.repository.SavedArticleRepository;
import dasturlash.uz.repository.customInterfaces.ArticleShortInfoMapper;
import dasturlash.uz.util.SpringSecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static dasturlash.uz.util.SpringSecurityUtil.getCurrentUserId;

@Service
@AllArgsConstructor
public class SavedArticleService {

    private final SavedArticleRepository savedArticleRepository;
    private final AttachService attachService;

    public void saveArticle(String articleId) {

        Optional<SavedArticles> optional = savedArticleRepository.findByArticleIdAndProfileId(articleId, getCurrentUserId());

        if (optional.isEmpty()) {
            SavedArticles newSavedArticles = new SavedArticles();
            newSavedArticles.setArticleId(articleId);
            newSavedArticles.setProfileId(getCurrentUserId());
            newSavedArticles.setVisible(true);
            newSavedArticles.setCreatedDate(LocalDateTime.now());
            savedArticleRepository.save(newSavedArticles);
        } else {
            SavedArticles savedArticles = optional.get();
            if (!savedArticles.getVisible()) {
                savedArticles.setVisible(true);
                savedArticleRepository.save(savedArticles);
            }

        }
    }

    public void removeSavedArticle(String articleId) {

        if (isExist(articleId)) {
            savedArticleRepository.changeVisibility(articleId, getCurrentUserId(), Boolean.FALSE);
        }
    }


    public List<ArticleShortInfoDTO> getSavedArticles() {

        Long currentUserId = getCurrentUserId();
        List<ArticleShortInfoMapper> result = savedArticleRepository.findAllByProfileId(currentUserId);

        return result.stream().map(this::toArticleShortInfoDTO).toList();
    }

    private boolean isExist(String articleId) {
        Long currentUserId = getCurrentUserId();

        return savedArticleRepository.existsByArticleIdAndProfileId(articleId, currentUserId);

    }

    private ArticleShortInfoDTO toArticleShortInfoDTO(ArticleShortInfoMapper article) {
        ArticleShortInfoDTO dto = new ArticleShortInfoDTO();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setDescription(article.getDescription());
        dto.setImage(attachService.getDto(article.getImageId()));
        dto.setPublishedDate(article.getPublishedDate());
        return dto;
    }
}
