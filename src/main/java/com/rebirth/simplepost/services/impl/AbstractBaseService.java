package com.rebirth.simplepost.services.impl;

import com.github.dozermapper.core.Mapper;
import com.rebirth.simplepost.domain.entities.Auditor;
import com.rebirth.simplepost.services.BaseService;
import com.rebirth.simplepost.services.dtos.BaseDto;
import com.rebirth.simplepost.web.errors.BadRequestAlertException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AbstractBaseService<
        T extends Auditor<ID>,
        DTO extends BaseDto,
        ID extends Serializable,
        REPO extends JpaRepository<T, ID>>
        implements BaseService<T, DTO, ID> {

    protected final REPO repository;
    private final Class<T> daoClass;
    private final Class<DTO> dtoClass;

    @Autowired
    protected Mapper dozerMapper;


    public AbstractBaseService(Class<T> daoClass, Class<DTO> dtoClass, REPO repository) {
        this.repository = repository;
        this.daoClass = daoClass;
        this.dtoClass = dtoClass;
    }

    @Override
    public List<DTO> fetchAll() {
        return repository.findAll()
                .stream()
                .map(this::dao2dto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DTO> fetchById(ID id) {
        Optional<T> result = repository.findById(id);
        if (result.isPresent()) {
            DTO op1 = this.dao2dto(result.get());
            return Optional.of(op1);
        } else {
            return Optional.empty();
        }

    }

    @Override
    public DTO create(DTO entity) {
        T newEntity = this.dto2Dao(entity);
        repository.save(newEntity);
        return this.dao2dto(newEntity);
    }

    @Override
    public DTO update(ID id, DTO entity) {
        if (id == entity.getId()) {
            T result = this.repository.findById(id)
                    .orElseThrow(() -> new BadRequestAlertException("No se encontro el recurso", this.daoClass.getName(), "NOT_FOUND"));
            String createBy = result.getCreateBy();
            LocalDateTime createAt = result.getCreateAt();

            this.dozerMapper.map(entity, result);

            result.setCreateBy(createBy);
            result.setCreateAt(createAt);
            T updateResource = this.repository.save(result);

            return this.dao2dto(updateResource);
        } else {
            throw new BadRequestAlertException("No se puede actualizar, ya que los ids no coinciden", entity.getClass().getName(), "Update fail");
        }
    }

    @Override
    public void remove(ID id) {
        repository.deleteById(id);
    }

    public DTO dao2dto(T entity) {
        return this.dozerMapper.map(entity, dtoClass);
    }

    public T dto2Dao(DTO dto) {
        return this.dozerMapper.map(dto, daoClass);
    }


}
