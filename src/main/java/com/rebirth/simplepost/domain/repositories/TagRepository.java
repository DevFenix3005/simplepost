package com.rebirth.simplepost.domain.repositories;

import com.rebirth.simplepost.domain.entities.Post;
import com.rebirth.simplepost.domain.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;


public interface TagRepository extends JpaRepository<Tag, Long> {
    Boolean existsByName(@Param("name") String name);

    Tag findByName(@Param("name") String name);

    @Query("SELECT T.posts FROM Tag T WHERE T.id = :tag")
    Set<Post> getPostByTag(@Param("tag") Long tag);


}


