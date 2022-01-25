package com.iot.espet.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.iot.espet.entities.Device;
import com.iot.espet.entities.Espet;
import com.iot.espet.entities.User;
import com.iot.espet.services.ConnexionService;
import com.iot.espet.services.DeviceService;
import com.iot.espet.services.EspetService;
import com.iot.espet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Optional;

@RestController
@Validated
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private ConnexionService connexionService;

    @Autowired
    private UserService userService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private EspetService espetService;

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<?> authentification(@RequestBody String request) {
        Gson g = new Gson();
        JsonObject json = g.fromJson( request, JsonObject.class);
        JsonElement mail = json.get("mail");
        JsonElement mdp = json.get("password");
        if(mail == null || mdp == null) return ResponseEntity.unprocessableEntity().build();
        Optional<User> user = connexionService.authentificationUser(mail.getAsString(), mdp.getAsString());
        if(user.isPresent()) {
            User u = user.get();
            connexionService.makeToken(user.get());
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(u);
        }
        else return null;
    }

    @PostMapping(value = "/new", consumes = "application/json")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        if(user.getId_user() != null) return ResponseEntity.unprocessableEntity().build();
        if(connexionService.register(user)){
            connexionService.makeToken(user);
            //HashMap<String,Object> json = new HashMap<String, Object>();
            //json.put("token", user.getLogin_token());
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
        }else {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @GetMapping(value = "/{id_user}/devices/list")
    public ResponseEntity<?> listDevices(@PathVariable Integer id_user){
        Optional<User> user = userService.getById(id_user);
        if(user.isPresent()) return ResponseEntity.ok(user.get().getDevices());
        else return ResponseEntity.unprocessableEntity().build();
    }

    @GetMapping(value = "/{id_user}/devices/{id_device}")
    public ResponseEntity<?> getDevice(@PathVariable Integer id_user, @PathVariable Integer id_device){
        Optional<User> user = userService.getById(id_user);
        Optional<Device> device = deviceService.getById(id_device);
        if(device.isPresent() && user.isPresent()) {
            User u = user.get();
            Device d = device.get();
            if (u.getDevices().contains(d)) return ResponseEntity.ok(d);
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    @GetMapping(value = "/{id_user}/games/list")
    public ResponseEntity<?> listGames(@PathVariable Integer id_user){
        Optional<User> user = userService.getById(id_user);
        if(user.isPresent()) return ResponseEntity.ok(user.get().getEspets());
        else return ResponseEntity.unprocessableEntity().build();
    }

    @GetMapping(value = "/{id_user}/games/{id_pet}")
    public ResponseEntity<?> getGame(@PathVariable Integer id_user, @PathVariable Integer id_pet){
        Optional<User> user = userService.getById(id_user);
        Optional<Espet> espet = espetService.getById(id_pet);
        if(espet.isPresent() && user.isPresent()) {
            User u = user.get();
            Espet e = espet.get();
            if (u.getEspets().contains(e)) return ResponseEntity.ok(e);
        }
        return ResponseEntity.unprocessableEntity().build();
    }

}
