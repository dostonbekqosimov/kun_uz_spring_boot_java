package dasturlash.uz.repository;

import dasturlash.uz.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String> {
}
