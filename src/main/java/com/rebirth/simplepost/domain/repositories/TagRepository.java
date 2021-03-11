package com.rebirth.simplepost.domain.repositories;

import com.rebirth.simplepost.domain.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByName(@Param("name") String name);


}


