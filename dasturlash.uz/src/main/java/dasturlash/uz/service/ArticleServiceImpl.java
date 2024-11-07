package dasturlash.uz.service;

import dasturlash.uz.dtos.ArticleCreateRequestDTO;
import dasturlash.uz.dtos.ArticleFullInfoDTO;
import dasturlash.uz.dtos.ArticleShortInfoDTO;
import dasturlash.uz.dtos.ArticleUpdateRequestDTO;
import dasturlash.uz.entity.Article;
import dasturlash.uz.enums.ArticleStatus;
import dasturlash.uz.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public Article createArticle(ArticleCreateRequestDTO request) {

        Article newArticle = new Article();
        newArticle.setTitle(request.getTitle());
        newArticle.setDescription(request.getDescription());                
        newArticle.setContent(request.getContent());
        newArticle.setSharedCount(0);
        newArticle.setImageId(request.getImageId());
        newArticle.setRegionId(request.getRegionId());
        newArticle.setCategoryId(request.getCategoryId());
        newArticle.setStatus(ArticleStatus.NOT_PUBLISHED);
        newArticle.setCreatedDate(LocalDateTime.now());

        articleRepository.save(newArticle);

        return newArticle;


    }

    @Override
    public Article updateArticle(String id, ArticleUpdateRequestDTO request) {
        return null;
    }

    @Override
    public void deleteArticle(String id) {

    }

    @Override
    public Article changeStatus(String id, String status) {
        return null;
    }

    @Override
    public List<ArticleShortInfoDTO> getLastNArticlesByTypes(List<String> types, int count) {
        return List.of();
    }

    @Override
    public List<ArticleShortInfoDTO> getLast3ArticlesByTypes(List<String> types) {
        return List.of();
    }

    @Override
    public List<ArticleShortInfoDTO> getLast8ArticlesExcluding(List<String> excludedIds) {
        return List.of();
    }

    @Override
    public ArticleFullInfoDTO getArticleByIdAndLang(String id, String lang) {
        return null;
    }

    @Override
    public List<ArticleShortInfoDTO> getLastNArticlesByTypesExcluding(List<String> types, String excludeId, int count) {
        return List.of();
    }

    @Override
    public List<ArticleShortInfoDTO> getMostReadArticles(int count) {
        return List.of();
    }

    @Override
    public List<ArticleShortInfoDTO> getLast4ArticlesByTagName(String tagName) {
        return List.of();
    }

    @Override
    public List<ArticleShortInfoDTO> getLast5ArticlesByTypesAndRegion(List<String> types, String regionKey) {
        return List.of();
    }

    @Override
    public List<ArticleShortInfoDTO> getArticlesByRegion(String regionKey, int page, int size) {
        return List.of();
    }

    @Override
    public List<ArticleShortInfoDTO> getLast5ArticlesByCategory() {
        return List.of();
    }

    @Override
    public List<ArticleShortInfoDTO> getArticlesByCategory(String categoryKey, int page, int size) {
        return List.of();
    }

    @Override
    public void increaseArticleViewCount(String articleId) {

    }

    @Override
    public void increaseShareViewCount(String articleId) {

    }

    @Override
    public List<ArticleShortInfoDTO> filterArticles(String id, String title, String regionId, String categoryId, String createdDateFrom, String createdDateTo, String publishedDateFrom, String publishedDateTo, String moderatorId, String publisherId, String status, int page, int size) {
        return List.of();
    }
}
