package dasturlash.uz.service.article;

import dasturlash.uz.dtos.AttachDTO;
import dasturlash.uz.dtos.article.*;
import dasturlash.uz.dtos.articleType.ArticleTypeResponseDTO;
import dasturlash.uz.entity.article.Article;
import dasturlash.uz.enums.ArticleStatus;
import dasturlash.uz.exceptions.ArticleNotFoundException;
import dasturlash.uz.exceptions.DataNotFoundException;
import dasturlash.uz.repository.ArticleRepository;
import dasturlash.uz.repository.customInterfaces.ArticleFullInfoMapper;
import dasturlash.uz.repository.customInterfaces.ArticleShortInfoMapper;
import dasturlash.uz.service.AttachService;
import dasturlash.uz.util.SpringSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleTypeMapService articleTypeMapService;
    @Autowired
    private ArticleAttacheService articleAttacheService;
    @Autowired
    private AttachService attachService;


    public ArticleDTO createArticle(ArticleRequestDTO request) {

        ArticleRequestDTO dto = request;

        Article newArticle = new Article();
        newArticle.setTitle(request.getTitle());
        newArticle.setDescription(request.getDescription());
        newArticle.setContent(request.getContent());
        newArticle.setImageId(request.getImageId());
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


        ArticleDTO respond = toArticleDTO(newArticle);

        // set moderator id
        respond.setModeratorId(newArticle.getModeratorId());

        // set types ( shu yerda faqat idlarni jo'natib qo'yish ham mumkin
        respond.setArticleTypeList(articleTypeMapService.getArticleTypeList(articleId));

        // set images ( I don't have to do this, but It is good practice, so I just want to keep it
        respond.setImageList(articleAttacheService.getAttachList(articleId));


        return respond;
        // dto ga o'girib keyin return qilish kerak [done]
//        return newArticle;


    }


    public ArticleDTO updateArticle(String articleId, ArticleRequestDTO request) {

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
        oldArticle.setModeratorId(SpringSecurityUtil.getUserId());


        articleRepository.save(oldArticle);

        // setting attaches
        articleAttacheService.merge(articleId, request.getAttachDTOList());


        // setting article types
        articleTypeMapService.merge(articleId, request.getArticleTypeList());

        ArticleDTO respond = toArticleDTO(oldArticle);

        // set moderator id
        respond.setModeratorId(oldArticle.getModeratorId());

        // set types
        respond.setArticleTypeList(articleTypeMapService.getArticleTypeList(articleId));

        // set images ( I don't have to do this, but It is good practice, so I just want to keep it
        respond.setImageList(articleAttacheService.getAttachList(articleId));


        return respond;
    }


    public void deleteArticle(String articleId) {

        Article existingArticle = getArticleEntityById(articleId);

        articleRepository.changeVisibility(articleId);

    }


    public ArticleDTO changeStatus(String articleId, ArticleStatus status) {

        Article existingArticle = getArticleEntityById(articleId);

        // changing the status
        existingArticle.setStatus(status);

        // setting the published time
        existingArticle.setPublishedDate(LocalDateTime.now());

        articleRepository.save(existingArticle);

        ArticleDTO respond = toArticleDTO(existingArticle);

        // set moderator id
        respond.setModeratorId(existingArticle.getModeratorId());

        // set types ( shu yerda faqat idlarni jo'natib qo'yish ham mumkin
        respond.setArticleTypeList(articleTypeMapService.getArticleTypeList(articleId));

        // set images ( I don't have to do this, but It is good practice, so I just want to keep it
        respond.setImageList(articleAttacheService.getAttachList(articleId));

        return respond;


    }


    // Oxirgi qo'shilganlarni olmayabdi nimagadur [done]
    public List<ArticleShortInfoDTO> getLastNArticlesByTypes(Long articleTypeId, Integer count) {

        List<String> articleIdList = articleTypeMapService.getNArticleIdListByTypeId(articleTypeId, count);


        return getArticlesByArticleIds(articleIdList, articleTypeId);
    }


    public List<ArticleShortInfoDTO> getLast8ArticlesExcluding(List<String> excludedIds) {

        List<ArticleShortInfoMapper> result = articleRepository
                .findLast8ArticlesExcluding(ArticleStatus.PUBLISHED, excludedIds, PageRequest.of(0, 8));

        return result.stream().map(item -> toArticleShortInfoDTO(item)).toList();

    }


    public ArticleFullInfoDTO getArticleByIdAndLang(String id, String lang) {


        return null;
    }


    public List<ArticleShortInfoDTO> getLastNArticlesByTypesExcluding(Long articleTypeId, String excludeArticleId, Integer count) {

//        List<ArticleFullInfoMapper> result = articleRepository
//                .findLastNArticlesByTypesExcluding(ArticleStatus.PUBLISHED, excludeArticleId, PageRequest.of(0, count));

        List<String> articleIdList = articleTypeMapService.getNArticleIdListByTypeId(articleTypeId, count);


        return getArticlesByArticleIdListExcluding(articleIdList, excludeArticleId, articleTypeId);
    }


    public List<ArticleShortInfoDTO> getMostReadArticles(Integer count) {

        List<ArticleShortInfoMapper> mostReadArticles = articleRepository.findTopNMostReadArticles(count, PageRequest.of(0, count));

//        if (mostReadArticles.isEmpty()){
//            throw new ArticleNotFoundException()
//        }

        return mostReadArticles.stream().map(this::toArticleShortInfoDTO).toList();
    }


    public List<ArticleShortInfoDTO> getLast4ArticlesByTagName(String tagName) {
        return List.of();
    }


    public List<ArticleShortInfoDTO> getLast5ArticlesByTypeAndRegion(Long articleTypeId, Long regionId, Integer count) {


        // Validate input parameters
        if (articleTypeId == null || regionId == null) {
            throw new IllegalArgumentException("Article type ID and region ID must not be null");
        }

        // Get articles in a single query if possible
        List<ArticleShortInfoMapper> articles = articleRepository.findTopNByArticleTypeAndRegionAndStatus(
                ArticleStatus.PUBLISHED,
                articleTypeId,
                regionId,
                PageRequest.of(0, count));

        // Log warning if fewer articles found than expected
        if (articles.isEmpty()) {
            log.warn("No articles found for type={} and region={}", articleTypeId, regionId);
            return Collections.emptyList();
        }

        if (articles.size() < count) {
            log.info("Found only {} articles out of {} requested for type={} and region={}",
                    articles.size(), count, articleTypeId, regionId);
        }

        // Transform to DTOs
        return articles.stream()
                .map(this::toArticleShortInfoDTO)
                .collect(Collectors.toList());


    }


    public PageImpl<ArticleShortInfoDTO> getArticlesByRegion(Long regionId, int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdDate").descending());

        Page<ArticleShortInfoMapper> result = articleRepository.findAllByRegionId(regionId, pageRequest);

        List<ArticleShortInfoDTO> response = result.stream().map(this::toArticleShortInfoDTO).toList();


        return new PageImpl<>(response, pageRequest, result.getTotalElements());
    }


    public List<ArticleShortInfoDTO> getLast5ArticlesByCategoryId(Long categoryId, Integer count) {

        if (categoryId == null || categoryId <= 0){
            throw new IllegalArgumentException("categoryId cannot be null or negative");
        }

        List<ArticleShortInfoMapper> result = articleRepository
                .findLastNArticlesByCategoryId(categoryId, ArticleStatus.PUBLISHED, PageRequest.of(0, count));

        if (result.isEmpty()){
            throw new ArticleNotFoundException("Article with categoryId: " + categoryId + " not found");
        }

        return result.stream().map(this::toArticleShortInfoDTO).toList();
    }


    public List<ArticleShortInfoDTO> getArticlesByCategory(Long categoryKey, int page, int size) {
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


    public Article getArticleEntityById(String articleId) {
        return articleRepository.findByIdAndVisibleTrue(articleId)
                .orElseThrow(() -> new DataNotFoundException("Article with id: " + articleId + " not found"));
    }

    public List<ArticleShortInfoDTO> getArticlesByArticleIds(List<String> articleIdList, Long articleTypeId) {
        // Fetch articles in bulk using a repository or DAO
        List<ArticleShortInfoMapper> articles = articleRepository.findAllArticlesByIdList(ArticleStatus.PUBLISHED, articleIdList);

        if (articles.isEmpty()) {

            throw new ArticleNotFoundException("No articles found for the specified type: " + articleTypeId);

        }

        // Convert entities to DTOs
        return articles.stream().map(item -> toArticleShortInfoDTO(item)).toList();
    }

    public List<ArticleShortInfoDTO> getArticlesByArticleIdListExcluding(List<String> articleIdList, String excludeArticleId, Long articleTypeId) {
        // Fetch articles in bulk using a repository or DAO
        List<ArticleShortInfoMapper> articles = articleRepository.findAllArticlesByIdListExcluding(ArticleStatus.PUBLISHED, excludeArticleId, articleIdList);

        if (articles.isEmpty()) {

            throw new ArticleNotFoundException("No articles found for the specified type: " + articleTypeId);

        }

        // Convert entities to DTOs
        return articles.stream().map(this::toArticleShortInfoDTO).toList();
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

    // not completed yet [...]
    private ArticleFullInfoDTO toArticleFullInfoDTO(ArticleFullInfoMapper article) {
        ArticleFullInfoDTO dto = new ArticleFullInfoDTO();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setDescription(article.getDescription());
        dto.setContent(article.getContent());
        dto.setSharedCount(article.getSharedCount());
        return dto;
    }

    private ArticleDTO toArticleDTO(Article article) {

        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(article.getId());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setDescription(article.getDescription());
        articleDTO.setCategoryId(articleDTO.getCategoryId());
        articleDTO.setRegionId(articleDTO.getRegionId());
        articleDTO.setCreatedDate(article.getCreatedDate());


        return articleDTO;
    }

}
