package com.example.zoo_fantastico.service;

import com.example.zoo_fantastico.exception.ResourceNotFoundException;
import com.example.zoo_fantastico.model.Zone;
import com.example.zoo_fantastico.repository.ZoneRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneService{

    private final ZoneRepository zoneRepository;

    public ZoneRepository(ZoneRepository zoneRepository) {this.zoneRepository = zoneRepository; }

    public Zone create(Zone zone){ return zoneRepository.save(zone); }

    public List<Zone> findAll() { return zoneRepository.findAll(); }

    public Zone findById(long id){
        return zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));
    }

    public Zone update(long id, Zone updated){
        Zone z = findById(id);
        z.setName(updated.getName());
        z.setZoneType(updated.getZoneType());
        z.setAreaMeters(updated.getAreaMeters());
        return zoneRepository.save(z);
    }

    public void delete(long id){
        Zone z = findById(id);
        if(z.getCreatures().isEmpty()){
            zoneRepository.delete(z);
        }else{
            throw new IllegalStateException("Cannot delete a zone with creatures assigned");
        }
    }



}
