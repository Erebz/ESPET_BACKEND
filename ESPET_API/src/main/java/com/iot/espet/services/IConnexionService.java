package com.iot.espet.services;

import com.iot.espet.entities.Device;
import com.iot.espet.entities.User;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

public interface IConnexionService {
    Optional<User> authentificationUser(String mail, String password);
    Optional<Device> authentificationDevice(String mac);
    void setLoginToken(User user, String token);
    void setLoginToken(Device device, String token);

    boolean register(User user);
    boolean register(Device device);

    void makeToken(User user);
    void makeToken(Device device);
}
