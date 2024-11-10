package dasturlash.uz.service.article;


import dasturlash.uz.dtos.articleType.ArticleTypeResponseDTO;
import dasturlash.uz.entity.article.ArticleTypeMapping;
import dasturlash.uz.exceptions.ArticleNotFoundException;
import dasturlash.uz.repository.ArticleTypeMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleTypeMapService {

    private final ArticleTypeMapRepository articleTypeMapRepository;
    private final ArticleTypeService articleTypeService;

    public List<ArticleTypeResponseDTO> getArticleTypeList(String articleId) {
        List<Long> typeIdList = articleTypeMapRepository.findAllByArticleId(articleId);

        List<ArticleTypeResponseDTO> typeList = new ArrayList<>();
        for (Long typeId : typeIdList) {
            typeList.add(articleTypeService.getArticleTypeById(typeId));
        }

        return typeList;
    }

    public void merge(String articleId, List<Long> newTypeIdList) {
        if (newTypeIdList == null) {
            newTypeIdList = new ArrayList<>();
        }

        List<Long> oldTypeIdList = articleTypeMapRepository.findAllByArticleId(articleId);

        // Remove types that are no longer present
        for (Long typeId : oldTypeIdList) {
            if (!newTypeIdList.contains(typeId)) {
                articleTypeMapRepository.deleteByArticleIdAndTypeId(articleId, typeId);
            }
        }

        // Add new types
        for (Long newTypeId : newTypeIdList) {
            if (!oldTypeIdList.contains(newTypeId)) {
                ArticleTypeMapping entity = new ArticleTypeMapping();
                entity.setArticleId(articleId);
                entity.setArticleTypeId(newTypeId);
                entity.setCreatedDate(LocalDateTime.now());
                articleTypeMapRepository.save(entity);
            }
        }
    }

    public List<String> getNArticleIdListByTypeId(Long articleTypeId, Integer count) {

        List<String> articleIdList = articleTypeMapRepository.findAllArticleIdListByTypeId(articleTypeId, PageRequest.of(0, count));



        return articleIdList;
    }
}
