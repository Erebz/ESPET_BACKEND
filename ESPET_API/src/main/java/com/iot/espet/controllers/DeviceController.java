package com.iot.espet.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.iot.espet.entities.Device;
import com.iot.espet.services.ConnexionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@Validated
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/device")
public class DeviceController {
    @Autowired
    private ConnexionService connexionService;

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<?> authentification(@RequestBody String request) {
        Gson g = new Gson();
        JsonObject json = g.fromJson( request, JsonObject.class);
        JsonElement mac = json.get("mac");
        if(mac == null) return ResponseEntity.unprocessableEntity().build();
        Optional<Device> device = connexionService.authentificationDevice(mac.getAsString());
        if(device.isPresent()) {
            Device d = device.get();
            if(d.getLast_espet() == null) d.setLast_espet(-1);
            connexionService.makeToken(d);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(d);
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("REGISTER");
        }
    }

    @PostMapping(value = "/new", consumes = "application/json")
    public ResponseEntity<?> createDevice(@Valid @RequestBody Device device) {
        if(device.getId_device() != null) return ResponseEntity.unprocessableEntity().build();
        if(connexionService.register(device)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.unprocessableEntity().build();
        }
    }



}
