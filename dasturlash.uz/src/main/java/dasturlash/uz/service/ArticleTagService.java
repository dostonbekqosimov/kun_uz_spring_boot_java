package dasturlash.uz.service;

import dasturlash.uz.repository.ArticleTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleTagService {

    private final ArticleTagRepository articleTagRepository;

    public void addTagToArticle(String articleId, Long tagId) {
        
    }
}
