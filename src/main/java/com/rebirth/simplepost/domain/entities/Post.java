package com.rebirth.simplepost.domain.entities;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name = "posts")
@NonNull
@NoArgsConstructor
@RequiredArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "post_id"))
@EqualsAndHashCode(callSuper = true)
public class Post extends Auditor<Long> {

    @Column(columnDefinition = "character varying(100)")
    @NonNull
    @NotNull
    @Length(min = 1, max = 150)
    @NotEmpty
    @NotBlank
    private String title;

    @Column(columnDefinition = "text")
    @NonNull
    @NotNull
    @Length(min = 1, max = 300)
    @NotEmpty
    @NotBlank
    private String body;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "post_id")
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany
    Set<Tag> tags;

    public void addComment(Comment comment) {
        if (Objects.isNull(comments))
            comments = new ArrayList<>();
        comments.add(comment);
    }

}
