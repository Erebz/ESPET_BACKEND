package com.iot.espet.services;

import com.iot.espet.entities.Device;
import com.iot.espet.entities.User;
import com.iot.espet.repositories.DeviceRepository;
import com.iot.espet.repositories.UserRepository;
import com.iot.espet.util.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class ConnexionService implements IConnexionService{
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;

    @Autowired
    public ConnexionService(UserRepository UserRepository, DeviceRepository DeviceRepository) {
        this.userRepository = UserRepository;
        this.deviceRepository = DeviceRepository;
    }

    @Override
    public Optional<User> authentificationUser(String mail, String password) {
        User u = userRepository.findUserByMailAndPassword(mail, password);
        return Optional.ofNullable(u);
    }

    @Override
    public Optional<Device> authentificationDevice(String mac) {
        Device d = deviceRepository.findDeviceByMac(mac);
        return Optional.ofNullable(d);
    }

    @Override
    public void setLoginToken(User user, String token) {
        Date today = new Date();
        user.setLogin_token(token);
        user.setToken_date(today);
        userRepository.save(user);
    }

    @Override
    public void setLoginToken(Device device, String token) {
        Date today = new Date();
        device.setLogin_token(token);
        device.setToken_date(today);
        deviceRepository.save(device);
    }

    @Override
    @Transactional
    public boolean register(User user) {
        User u = userRepository.findUserByMail(user.getMail());
        Optional<User> _u = Optional.ofNullable(u);
        if(_u.isPresent()){
            return false;
        }else {
            userRepository.save(user);
            return true;
        }
    }

    @Override
    @Transactional
    public boolean register(Device device) {
        Device d = deviceRepository.findFirstByMac(device.getMac());
        Optional<Device> _d = Optional.ofNullable(d);
        if(_d.isPresent()){
            return false;
        }else {
            deviceRepository.save(device);
            return true;
        }
    }

    @Override
    public void makeToken(User user) {
        String token = Token.getToken(10);
        setLoginToken(user, token);
    }

    @Override
    public void makeToken(Device device) {
        String token = Token.getToken(10);
        setLoginToken(device, token);
    }
}
