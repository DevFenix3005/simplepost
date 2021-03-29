package com.rebirth.simplepost.services;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface BaseService<
        DTO extends Serializable,
        ID extends Serializable
        > {

    List<DTO> fetchAll();

    default List<DTO> fetchAll(String filter) {
        if (filter.isBlank()) return fetchAll();
        else return Collections.emptyList();
    }

    Optional<DTO> fetchById(ID id);

    DTO create(DTO entity);

    DTO update(ID id, DTO entity);

    void remove(ID id);


}
