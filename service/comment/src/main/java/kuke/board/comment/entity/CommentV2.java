package kuke.board.comment.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Table(name = "comment_v2")
@Getter
@Entity
@ToString
@NoArgsConstructor
public class CommentV2 {

    @Id
    private Long commentId;
    private String content;
    private Long articleId; // shard key
    private Long writerId;
    @Embedded
    private CommentPath commentPath;
    private boolean deleted;
    private LocalDateTime createdAt;
}
