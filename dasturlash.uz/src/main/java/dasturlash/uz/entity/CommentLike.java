package dasturlash.uz.entity;

import dasturlash.uz.entity.article.Article;
import dasturlash.uz.enums.ReactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comment_likes")
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    private ReactionType type;

    @Column(name = "profile_id")
    private Long profileId;
    @ManyToOne
    @JoinColumn(name = "profile_id", updatable = false, insertable = false)
    private Profile profile;

    @Column(name = "comment_id")
    private String commentId;
    @ManyToOne
    @JoinColumn(name = "comment_id", updatable = false, insertable = false)
    private Comment comment;

    private LocalDateTime createdDate;

    private Boolean visible;
}
