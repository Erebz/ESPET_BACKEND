package com.iot.espet.controllers;

import com.iot.espet.entities.Device;
import com.iot.espet.entities.Espet;
import com.iot.espet.entities.User;
import com.iot.espet.services.DeviceService;
import com.iot.espet.services.EspetService;
import com.iot.espet.util.ValidRegister;
import com.iot.espet.util.ValidSave;
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
import java.util.Optional;

@RestController
@Validated
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/game")
public class EspetController {

    @Autowired
    private EspetService espetService;

    @Autowired
    private DeviceService deviceService;

    @GetMapping("/load/{id_pet}/{mac}")
    public ResponseEntity<?> loadGame(@PathVariable("id_pet") int id_pet, @PathVariable("mac") String mac){
        Optional<Espet> espet = espetService.getById(id_pet);
        if(espet.isPresent()) {
            System.out.println(mac);
            Optional<Device> device = deviceService.getByMac(mac);
            if(device.isPresent()){
                Device d = device.get();
                d.setLast_espet(id_pet);
                deviceService.save(d);
            }
            device.ifPresent(d -> d.setLast_espet(id_pet));
            Espet e = espet.get();
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(e);
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("ESPET NOT FOUND");
        }
    }

    @PostMapping(value = "/save", consumes = "application/json")
    public ResponseEntity<?> saveGame(@Validated({ValidSave.class}) @RequestBody Espet espet){
        System.out.println("Saved " + espet.getName());
        if(espetService.save(espet)) return ResponseEntity.ok("OK");
        else return ResponseEntity.unprocessableEntity().body("NOT FOUND");
    }

    @PostMapping(value = "/new", consumes = "application/json")
    public ResponseEntity<?> newGame(@Validated({ValidRegister.class}) @RequestBody Espet espet){
        if(espetService.create(espet)) return ResponseEntity.ok().build();
        else return ResponseEntity.badRequest().body("CONFLICT");
    }
}
