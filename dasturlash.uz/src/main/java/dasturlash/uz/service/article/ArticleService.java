package dasturlash.uz.service.article;

import dasturlash.uz.dtos.AttachDTO;
import dasturlash.uz.dtos.article.*;
import dasturlash.uz.dtos.articleType.ArticleTypeResponseDTO;
import dasturlash.uz.entity.article.Article;
import dasturlash.uz.enums.ArticleStatus;
import dasturlash.uz.exceptions.DataNotFoundException;
import dasturlash.uz.repository.ArticleRepository;
import dasturlash.uz.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleTypeMapService articleTypeMapService;
    @Autowired
    private ArticleAttacheService articleAttacheService;



    public Article createArticle(ArticleRequestDTO request) {

        ArticleRequestDTO dto = request;

        Article newArticle = new Article();
        newArticle.setTitle(request.getTitle());
        newArticle.setDescription(request.getDescription());
        newArticle.setContent(request.getContent());
        newArticle.setSharedCount(0);
        newArticle.setVisible(Boolean.TRUE);
        newArticle.setViewCount(0);
        newArticle.setRegionId(request.getRegionId());
        newArticle.setCategoryId(request.getCategoryId());
        newArticle.setStatus(ArticleStatus.NOT_PUBLISHED);
        newArticle.setModeratorId(SpringSecurityUtil.getUserId());

        newArticle.setCreatedDate(LocalDateTime.now());

        articleRepository.save(newArticle);

        String articleId = newArticle.getId();
        // setting article types
        articleTypeMapService.merge(articleId, request.getArticleTypeList());

        // setting attaches
        articleAttacheService.merge(articleId, request.getAttachDTOList());

        // dto ga o'girib keyin return qilish kerak [...]
        return newArticle;


    }


    public Article updateArticle(String articleId, ArticleRequestDTO request) {

        Article oldArticle = articleRepository.findById(articleId).
                orElseThrow(() -> new DataNotFoundException("Article not found"));


        oldArticle.setTitle(request.getTitle());
        oldArticle.setDescription(request.getDescription());
        oldArticle.setContent(request.getContent());
        oldArticle.setSharedCount(0);
        oldArticle.setVisible(Boolean.TRUE);
        oldArticle.setViewCount(0);
        oldArticle.setRegionId(request.getRegionId());
        oldArticle.setCategoryId(request.getCategoryId());
        oldArticle.setStatus(ArticleStatus.NOT_PUBLISHED);


        articleRepository.save(oldArticle);

        // setting attaches
        articleAttacheService.merge(articleId, request.getAttachDTOList());


        // setting article types
        articleTypeMapService.merge(articleId, request.getArticleTypeList());



        return oldArticle;
    }


    public void deleteArticle(String id) {

    }


    public Article changeStatus(String id, String status) {
        return null;
    }


    public List<ArticleShortInfoDTO> getLastNArticlesByTypes(List<String> types, int count) {
        return List.of();
    }


    public List<ArticleShortInfoDTO> getLast3ArticlesByTypes(List<String> types) {
        return List.of();
    }


    public List<ArticleShortInfoDTO> getLast8ArticlesExcluding(List<String> excludedIds) {
        return List.of();
    }


    public ArticleFullInfoDTO getArticleByIdAndLang(String id, String lang) {
        return null;
    }


    public List<ArticleShortInfoDTO> getLastNArticlesByTypesExcluding(List<String> types, String excludeId, int count) {
        return List.of();
    }


    public List<ArticleShortInfoDTO> getMostReadArticles(int count) {
        return List.of();
    }


    public List<ArticleShortInfoDTO> getLast4ArticlesByTagName(String tagName) {
        return List.of();
    }


    public List<ArticleShortInfoDTO> getLast5ArticlesByTypesAndRegion(List<String> types, String regionKey) {
        return List.of();
    }


    public List<ArticleShortInfoDTO> getArticlesByRegion(String regionKey, int page, int size) {
        return List.of();
    }


    public List<ArticleShortInfoDTO> getLast5ArticlesByCategory() {
        return List.of();
    }


    public List<ArticleShortInfoDTO> getArticlesByCategory(String categoryKey, int page, int size) {
        return List.of();
    }


    public void increaseArticleViewCount(String articleId) {

    }


    public void increaseShareViewCount(String articleId) {

    }


    public List<ArticleShortInfoDTO> filterArticles(String id, String title, String regionId, String categoryId, String createdDateFrom, String createdDateTo, String publishedDateFrom, String publishedDateTo, String moderatorId, String publisherId, String status, int page, int size) {
        return List.of();
    }

    public ArticleDTO getArticleById(String articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> {
            throw new RuntimeException("Article not found");
        });

        ArticleDTO dto = new ArticleDTO();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setDescription(article.getDescription());
        dto.setContent(article.getContent());
        dto.setRegionId(article.getRegionId());
        dto.setCategoryId(article.getCategoryId());


        // getting photo ids and urls by post id from ArticleAttachService
        List<AttachDTO> attachDTOList = articleAttacheService.getAttachList(articleId);

        // setting the details of photos in the articles
        dto.setImageList(attachDTOList);

        List<ArticleTypeResponseDTO> articleTypeList = articleTypeMapService.getArticleTypeList(articleId);
        dto.setArticleTypeList(articleTypeList);


        // returning articleDto with ids and urls of article as well as title and content
        return dto;
    }
}
