package com.iot.espet.services;

import com.iot.espet.entities.User;

import java.util.Optional;

public interface IUserService {
    public Optional<User> getById(Integer id_user);
}
