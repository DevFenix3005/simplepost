package com.rebirth.simplepost.services;

import com.rebirth.simplepost.domain.entities.Auditor;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseService<T extends Auditor<ID>, ID extends Serializable> {

    List<T> fetchAll();

    Optional<T> fetchById(ID id);

    T create(T entity);

    T update(ID id, T entity);

    void remove(ID id);

    default void remove(T entity) {
        remove(entity.getId());
    }

}
