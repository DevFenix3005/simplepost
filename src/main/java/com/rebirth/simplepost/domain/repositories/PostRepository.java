package com.rebirth.simplepost.domain.repositories;

import java.util.List;

import com.rebirth.simplepost.domain.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;


public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitle(@Param("title") String title);


}


