package com.rebirth.simplepost.web.controllers;

import com.rebirth.simplepost.web.errors.BadRequestAlertException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.Serializable;
import java.net.URI;
import java.util.StringJoiner;

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

    protected void errorHandler(Errors errors) {
        if (errors.hasErrors()) {
            StringJoiner stringJoiner = new StringJoiner(", ");
            for (FieldError fieldError : errors.getFieldErrors()) {
                String field = fieldError.getField();
                String msg = fieldError.getDefaultMessage();
                String mensaje = String.format("%s : %s", field, msg);
                stringJoiner.add(mensaje);
            }
            throw new BadRequestAlertException("Validation erros: " + stringJoiner.toString(), this.klass.getName(), "VALIDATION_ERROR");
        }
    }

}
