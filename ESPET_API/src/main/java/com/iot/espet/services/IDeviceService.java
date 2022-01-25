package com.iot.espet.services;

import com.iot.espet.entities.Device;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface IDeviceService {
    Optional<Device> getById(Integer id_device);

    Optional<Device> getByMac(String mac);

    @Transactional
    boolean save(Device device);
}
