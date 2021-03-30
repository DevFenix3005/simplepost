package com.rebirth.simplepost.services.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PusherMessage<BODY> {

    private BODY body;
    private String mensaje;
    private final Date now = new Date();

}
