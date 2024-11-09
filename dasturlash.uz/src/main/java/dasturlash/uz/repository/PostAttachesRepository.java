package dasturlash.uz.repository;

import dasturlash.uz.entity.PostAttaches;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostAttachesRepository extends JpaRepository<PostAttaches, String> {


    @Query("select attachId from PostAttaches where postId =?1")
    List<String> findAllByPostId(Integer postId);

    @Modifying
    @Transactional
    @Query("delete from PostAttaches where postId =?1")
    void deleteByPostId(Integer postId);

    @Modifying
    @Transactional
    @Query("delete from PostAttaches where postId =?1 and attachId = ?2")
    void deleteByPostIdAndAttachId(Integer postId, String attachId);


}
