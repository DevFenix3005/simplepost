package com.rebirth.simplepost.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name = "posts")
@NonNull
@RequiredArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(columnDefinition = "character varying(100)")
    @NonNull
    private String title;

    @Column(columnDefinition = "text")
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
