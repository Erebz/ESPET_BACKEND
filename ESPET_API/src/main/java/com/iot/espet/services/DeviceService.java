package com.iot.espet.services;

import com.iot.espet.entities.Device;
import com.iot.espet.entities.User;
import com.iot.espet.repositories.DeviceRepository;
import com.iot.espet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DeviceService implements IDeviceService{
    private final DeviceRepository deviceRepository;

    @Autowired
    public DeviceService(DeviceRepository DeviceRepository) {
        this.deviceRepository = DeviceRepository;
    }

    @Override
    public Optional<Device> getById(Integer id_device) {
        return deviceRepository.findById(id_device);
    }

    @Override
    public Optional<Device> getByMac(String mac) {
        return Optional.ofNullable(deviceRepository.findFirstByMac(mac));
    }

    @Override
    @Transactional
    public boolean save(Device device) {
        Device d = deviceRepository.findFirstByMac(device.getMac());
        Optional<Device> _d = Optional.ofNullable(d);
        if(_d.isPresent()){
            return false;
        }else {
            deviceRepository.save(device);
            return true;
        }
    }
}
