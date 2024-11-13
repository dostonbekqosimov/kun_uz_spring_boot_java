package dasturlash.uz.repository.custom;

import dasturlash.uz.dtos.ArticleFilterDTO;
import dasturlash.uz.dtos.article.ArticleShortInfoDTO;
import dasturlash.uz.entity.article.Article;
import dasturlash.uz.service.AttachService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class CustomArticleRepository {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private AttachService attachService;

    public Page<ArticleShortInfoDTO> filter(ArticleFilterDTO filter, int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdDate").descending());

        StringBuilder selectQueryBuilder = new StringBuilder("SELECT a FROM Article a ");
        StringBuilder countQueryBuilder = new StringBuilder("SELECT count(a) FROM Article a ");
        StringBuilder builder = new StringBuilder(" where a.visible = true ");

        Map<String, Object> params = new HashMap<>();

        // Add filter conditions as shown before
        if (filter.getId() != null) {
            builder.append(" and a.id =:id ");
            params.put("id", filter.getId());
        }
        if (filter.getTitle() != null) {
            builder.append(" and LOWER(a.title) like :title ");
            params.put("title", "%" + filter.getTitle().toLowerCase() + "%");
        }
        if (filter.getRegionId() != null) {
            builder.append(" and a.regionId =:regionId ");
            params.put("regionId", filter.getRegionId());
        }
        if (filter.getCategoryId() != null) {
            builder.append(" and a.categoryId =:categoryId ");
            params.put("categoryId", filter.getCategoryId());
        }
        if (filter.getModeratorId() != null) {
            builder.append(" and a.moderatorId =:moderatorId ");
            params.put("moderatorId", filter.getModeratorId());
        }
        if (filter.getPublisherId() != null) {
            builder.append(" and a.publisherId =:publisherId ");
            params.put("publisherId", filter.getPublisherId());
        }
        if (filter.getStatus() != null) {
            builder.append(" and a.status =:status ");
            params.put("status", filter.getStatus());
        }

        if (filter.getCreatedDateFrom() != null && filter.getCreatedDateTo() != null) {
            builder.append(" and a.createdDate between :createdDateFrom and :createdDateTo ");
            params.put("createdDateFrom", filter.getCreatedDateFrom());
            params.put("createdDateTo", filter.getCreatedDateTo());
        } else if (filter.getCreatedDateFrom() != null) {
            builder.append(" and a.createdDate >= :createdDateFrom ");
            params.put("createdDateFrom", filter.getCreatedDateFrom());
        } else if (filter.getCreatedDateTo() != null) {
            builder.append(" and a.createdDate <= :createdDateTo ");
            params.put("createdDateTo", filter.getCreatedDateTo());
        }

        if (filter.getPublishedDateFrom() != null && filter.getPublishedDateTo() != null) {
            builder.append(" and a.publishedDate between :publishedDateFrom and :publishedDateTo ");
            params.put("publishedDateFrom", filter.getPublishedDateFrom());
            params.put("publishedDateTo", filter.getPublishedDateTo());
        } else if (filter.getPublishedDateFrom() != null) {
            builder.append(" and a.publishedDate >= :publishedDateFrom ");
            params.put("publishedDateFrom", filter.getPublishedDateFrom());
        } else if (filter.getPublishedDateTo() != null) {
            builder.append(" and a.publishedDate <= :publishedDateTo ");
            params.put("publishedDateTo", filter.getPublishedDateTo());
        }

        selectQueryBuilder.append(builder);
        countQueryBuilder.append(builder);

        // select query
        Query selectQuery = entityManager.createQuery(selectQueryBuilder.toString());
        selectQuery.setFirstResult(page * size);
        selectQuery.setMaxResults(size);
        params.forEach(selectQuery::setParameter);

        List<Article> articleList = selectQuery.getResultList();

        // totalCount query
        Query countQuery = entityManager.createQuery(countQueryBuilder.toString());
        params.forEach(countQuery::setParameter);

        Long totalElements = (Long) countQuery.getSingleResult();

        // Convert Article to ArticleShortInfoDTO
        List<ArticleShortInfoDTO> response = articleList.stream()
                .map(this::toArticleShortInfoDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(response, pageRequest, totalElements);
    }

    // bu service da qilinishi kerak.
    private ArticleShortInfoDTO toArticleShortInfoDTO(Article article) {
        // Map fields from Article to ArticleShortInfoDTO
        ArticleShortInfoDTO dto = new ArticleShortInfoDTO();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setDescription(article.getDescription());
        dto.setImage(attachService.getDto(article.getImageId()));
        dto.setPublishedDate(article.getPublishedDate());

        return dto;
    }
}
