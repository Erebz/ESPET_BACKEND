package com.iot.espet.services;

import com.iot.espet.entities.Espet;
import com.iot.espet.repositories.EspetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class EspetService implements IEspetService{
    private final EspetRepository espetRepository;

    @Autowired
    public EspetService(EspetRepository EspetRepository) {
        this.espetRepository = EspetRepository;
    }

    public Optional<Espet> getById(Integer id_pet) {
        return espetRepository.findById(id_pet);
    }

    @Override
    @Transactional
    public boolean save(Espet espet) {
        Optional<Espet> e = espetRepository.findById(espet.getId_pet());
        if(e.isPresent()) {
            Espet _e = e.get();
            espet.setUser(_e.getUser());
            espetRepository.save(espet);
            return true;
        }else return false;
    }

    @Override
    @Transactional
    public boolean create(Espet espet) {
        if(espet.getId_pet() != null) return false;
        espet.setHappiness(0d);
        espet.setHunger(0d);
        espet.setFatigue(0d);
        espet.setAge(0d);
        espet.setHealth(0d);
        espet.setType("baby");
        espetRepository.save(espet);
        return true;
    }
}
