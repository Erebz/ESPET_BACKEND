package com.iot.espet.services;

import com.iot.espet.entities.User;
import com.iot.espet.repositories.DeviceRepository;
import com.iot.espet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService{
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository UserRepository) {
        this.userRepository = UserRepository;
    }

    @Override
    public Optional<User> getById(Integer id_user){
        return userRepository.findById(id_user);
    }
}
