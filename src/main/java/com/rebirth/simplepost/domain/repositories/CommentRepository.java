package com.rebirth.simplepost.domain.repositories;

import com.rebirth.simplepost.domain.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT C FROM Comment C WHERE C.postId = :id")
    List<Comment> findByPostId(@Param("id") Long id);
}


