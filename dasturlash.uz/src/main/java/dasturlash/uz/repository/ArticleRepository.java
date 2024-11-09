package dasturlash.uz.repository;

import dasturlash.uz.entity.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, String> {
}
