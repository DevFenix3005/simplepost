package com.rebirth.simplepost.domain.repositories;

import java.util.List;

import com.rebirth.simplepost.domain.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(collectionResourceRel = "post", path = "post")
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitle(@Param("title") String title);


}


