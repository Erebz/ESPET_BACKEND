package com.iot.espet.repositories;

import com.iot.espet.entities.Device;
import com.iot.espet.entities.Espet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspetRepository extends JpaRepository<Espet, Integer> {

    //@Query("SELECT d FROM Device d WHERE d.id_device = ?1")
    //Device getById(int id_device);
}
