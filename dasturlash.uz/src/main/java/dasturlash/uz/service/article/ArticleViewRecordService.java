package dasturlash.uz.service.article;

import dasturlash.uz.entity.article.ArticleShareRecord;
import dasturlash.uz.entity.article.ArticleViewRecord;
import dasturlash.uz.repository.ArticleShareRecordRepository;
import dasturlash.uz.repository.ArticleViewRecordRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ArticleViewRecordService {

    private final ArticleViewRecordRepository articleViewRecordRepository;
    private final ArticleShareRecordRepository articleShareRecordRepository;



    // TODO
    // 1. Trigger
    // 2. Function bilan
    public boolean increaseViewCount(String articleId, String ipAddress) {
        boolean result = articleViewRecordRepository.existsByArticleIdAndIpAddress(articleId, ipAddress);
        if (!result) {
            ArticleViewRecord entity = new ArticleViewRecord();
            entity.setArticleId(articleId);
            entity.setIpAddress(ipAddress);
            entity.setCreatedDate(LocalDateTime.now());
            articleViewRecordRepository.save(entity);
            return true;
        }
        return false;
    }

    public void increaseShareCount(String articleId, String ipAddress){
        articleShareRecordRepository.save(new ArticleShareRecord(articleId, ipAddress));

    }
}
