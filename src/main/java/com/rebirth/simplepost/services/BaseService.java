package com.rebirth.simplepost.services;

import com.rebirth.simplepost.domain.entities.Auditor;
import com.rebirth.simplepost.services.dtos.BaseDto;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseService<
        T extends Auditor<ID>,
        DTO extends BaseDto,
        ID extends Serializable> {

    List<DTO> fetchAll();

    Optional<DTO> fetchById(ID id);

    DTO create(DTO entity);

    DTO update(ID id, DTO entity);

    void remove(ID id);


}
