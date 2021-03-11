package com.rebirth.simplepost.domain.entities;

import lombok.*;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "comments")
@NoArgsConstructor
@RequiredArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "comment_id"))
@EqualsAndHashCode(callSuper = true)
public class Comment extends Auditor<Long> {

    @NonNull
    @Column(columnDefinition = "text")
    private String body;

    @Column(name = "post_id", nullable = false)
    private Long postId;

}
