package com.rebirth.simplepost.web.controllers;

import com.rebirth.simplepost.web.errors.BadRequestAlertException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.Serializable;
import java.net.URI;

public class BaseController<T, ID extends Serializable> {

    Class<T> klass;

    public BaseController(Class<T> klass) {
        this.klass = klass;
    }

    protected BadRequestAlertException resoruceNotFound(ID id) {
        return new BadRequestAlertException(String.format("El recurso con el id %s no fue encontrado", id), this.klass.getName(), "NOT_FOUND");
    }

    protected URI generateNewUri(Long id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .build(id);
    }

}
