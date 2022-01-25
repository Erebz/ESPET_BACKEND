package com.iot.espet.repositories;

import com.iot.espet.entities.Device;
import com.iot.espet.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {
    Device findDeviceByMac(String mac);
    Device findFirstByMac(String mac);

    //@Query("SELECT d FROM Device d WHERE d.id_device = ?1")
    //Device getById(int id_device);
}
