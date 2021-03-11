package com.rebirth.simplepost.services.impl;

import com.rebirth.simplepost.domain.entities.Auditor;
import com.rebirth.simplepost.services.BaseService;
import com.rebirth.simplepost.web.errors.BadRequestAlertException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class AbstractBaseService<T extends Auditor<ID>, ID
        extends Serializable, REPO extends JpaRepository<T, ID>>
        implements BaseService<T, ID> {

    private final REPO repository;

    public AbstractBaseService(REPO repository) {
        this.repository = repository;
    }

    @Override
    public List<T> fetchAll() {
        return repository.findAll();
    }

    @Override
    public Optional<T> fetchById(ID id) {
        return repository.findById(id);
    }

    @Override
    public T create(T entity) {
        return repository.save(entity);
    }

    @Override
    public T update(ID id, T entity) {
        if (id == entity.getId()) {
            return repository.save(entity);
        } else {
            throw new BadRequestAlertException("No se puede actualizar, ya que los ids no coinciden", entity.getClass().getName(), "Update fail");
        }
    }

    @Override
    public void remove(ID id) {
        repository.deleteById(id);
    }
}
