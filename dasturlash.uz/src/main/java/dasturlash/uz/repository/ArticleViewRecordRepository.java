package dasturlash.uz.repository;

import dasturlash.uz.entity.article.ArticleViewRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleViewRecordRepository extends JpaRepository<ArticleViewRecord, Long> {


    boolean existsByArticleIdAndIpAddress(String articleId, String ipAddress);

}
