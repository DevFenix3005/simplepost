package com.rebirth.simplepost.services;

import com.rebirth.simplepost.components.FilterComponent;
import org.jooq.impl.DAOImpl;
import org.jooq.impl.UpdatableRecordImpl;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public abstract class AbstractService<
        RECORD extends UpdatableRecordImpl<RECORD>,
        DTO extends Serializable,
        ID extends Serializable,
        REPO extends DAOImpl<RECORD, DTO, ID>>
        implements BaseService<DTO, ID> {

    private final String idKey;
    protected final REPO repository;
    protected final FilterComponent filterComponent;

    protected AbstractService(String idKey, REPO repository, FilterComponent filterComponent) {
        this.idKey = idKey;
        this.repository = repository;
        this.filterComponent = filterComponent;
    }

    protected AbstractService(REPO repository, FilterComponent filterComponent) {
        this("id", repository, filterComponent);
    }


    @Override
    public List<DTO> fetchAll() {
        return this.repository.findAll();
    }

    public abstract List<DTO> fetch2UpdateMovil();

    @Override
    public Optional<DTO> fetchById(ID id) {
        DTO dto = this.repository.findById(id);
        if (Objects.isNull(dto)) return Optional.empty();
        else return Optional.of(dto);
    }

    @Override
    public DTO create(DTO entity) {
        this.repository.insert(entity);
        return entity;
    }

    @Override
    public DTO update(ID id, DTO entity) {
        DTO oldValue = this.repository.findById(id);
        if (Objects.nonNull(oldValue)) {
            this.noLoseAuditFields(oldValue, entity);
            this.repository.update(entity);
            return entity;
        } else {
            return this.create(entity);
        }
    }

    @SuppressWarnings("unchecked")
    private void noLoseAuditFields(DTO oldDto, DTO newDto) {
        Class<? extends Serializable> klass = oldDto.getClass();
        try {
            Method getId = klass.getMethod(getterName(this.idKey));
            ID oldId = (ID) getId.invoke(oldDto);
            ID newId = (ID) getId.invoke(newDto);

            if (!oldId.equals(newId)) {
                throw new RuntimeException("El id no concide");
            }
            Method getCreateAtMethod = klass.getMethod(getterName("createAt"));
            Method getCreateByMethod = klass.getMethod(getterName("createBy"));
            Method getVersionMethod = klass.getMethod(getterName("version"));

            Method setCreateAtMethod = klass.getMethod(setterName("createAt"), LocalDateTime.class);
            Method setCreateByMethod = klass.getMethod(setterName("createBy"), String.class);
            Method setVersionMethod = klass.getMethod(setterName("version"), Long.class);

            LocalDateTime createAt = (LocalDateTime) getCreateAtMethod.invoke(oldDto);
            setCreateAtMethod.invoke(newDto, createAt);

            String createBy = (String) getCreateByMethod.invoke(oldDto);
            setCreateByMethod.invoke(newDto, createBy);

            Long version = (Long) getVersionMethod.invoke(oldDto);
            setVersionMethod.invoke(newDto, version);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public String getterName(String attrname) {
        return "get" + attrname.substring(0, 1).toUpperCase(Locale.ROOT) + attrname.substring(1);
    }

    public String setterName(String attrname) {
        return "set" + attrname.substring(0, 1).toUpperCase(Locale.ROOT) + attrname.substring(1);
    }


    @Override
    public void remove(ID id) {
        this.repository.existsById(id);
    }
}
