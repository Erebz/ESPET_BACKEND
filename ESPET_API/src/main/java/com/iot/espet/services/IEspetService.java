package com.iot.espet.services;

import com.iot.espet.entities.Espet;

public interface IEspetService {
    boolean save(Espet espet);

    boolean create(Espet espet);
}
