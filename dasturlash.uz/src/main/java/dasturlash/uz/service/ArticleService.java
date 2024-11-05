package dasturlash.uz.service;

import dasturlash.uz.dtos.ArticleCreateRequestDTO;
import dasturlash.uz.dtos.ArticleFullInfoDTO;
import dasturlash.uz.dtos.ArticleShortInfoDTO;
import dasturlash.uz.dtos.ArticleUpdateRequestDTO;
import dasturlash.uz.entity.Article;

import java.util.List;

public interface ArticleService {

    // 1. Create Article
    Article createArticle(ArticleCreateRequestDTO request);

    // 2. Update Article
    Article updateArticle(String id, ArticleUpdateRequestDTO request);

    // 3. Delete Article
    void deleteArticle(String id);

    // 4. Change Article Status
    Article changeStatus(String id, String status); // status can be "publish" or "not_publish"

    // 5. Get Last N Articles By Types
    List<ArticleShortInfoDTO> getLastNArticlesByTypes(List<String> types, int count);

    // 6. Get Last 3 Articles By Types
    List<ArticleShortInfoDTO> getLast3ArticlesByTypes(List<String> types);

    // 7. Get Last 8 Articles Excluding Given IDs
    List<ArticleShortInfoDTO> getLast8ArticlesExcluding(List<String> excludedIds);

    // 8. Get Article By Id And Language
    ArticleFullInfoDTO getArticleByIdAndLang(String id, String lang);

    // 9. Get Last 4 Articles By Types Excluding Given Article ID
    List<ArticleShortInfoDTO> getLastNArticlesByTypesExcluding(List<String> types, String excludeId, int count);

    // 10. Get 4 Most Read Articles
    List<ArticleShortInfoDTO> getMostReadArticles(int count);

    // 11. Get Last 4 Articles By Tag Name
    List<ArticleShortInfoDTO> getLast4ArticlesByTagName(String tagName);

    // 12. Get Last 5 Articles By Types And By Region Key
    List<ArticleShortInfoDTO> getLast5ArticlesByTypesAndRegion(List<String> types, String regionKey);

    // 13. Get Article List by Region Key with Pagination
    List<ArticleShortInfoDTO> getArticlesByRegion(String regionKey, int page, int size);

    // 14. Get Last 5 Articles By Category Key
    List<ArticleShortInfoDTO> getLast5ArticlesByCategory();

    // 15. Get Articles By Category Key with Pagination
    List<ArticleShortInfoDTO> getArticlesByCategory(String categoryKey, int page, int size);

    // 16. Increase Article View Count
    void increaseArticleViewCount(String articleId);

    // 17. Increase Share View Count
    void increaseShareViewCount(String articleId);

    // 18. Filter Articles with Pagination
    List<ArticleShortInfoDTO> filterArticles(String id, String title, String regionId, String categoryId,
                                          String createdDateFrom, String createdDateTo,
                                          String publishedDateFrom, String publishedDateTo,
                                          String moderatorId, String publisherId,
                                          String status, int page, int size);
}

