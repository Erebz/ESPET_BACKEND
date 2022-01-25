package com.iot.espet.repositories;

import com.iot.espet.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByMailAndPassword(String mail, String password);
    User findUserByMail(String mail);

    //@Query("SELECT u FROM User u WHERE u.id_user = ?1")
    //User getById(int id_user);
}
