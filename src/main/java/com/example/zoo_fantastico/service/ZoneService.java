package com.example.zoo_fantastico.service;

import com.example.zoo_fantastico.exception.ResourceNotFoundException;
import com.example.zoo_fantastico.exception.ZoneNotEmptyException;
import com.example.zoo_fantastico.model.Zone;
import com.example.zoo_fantastico.repository.ZoneRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneService{

    private final ZoneRepository zoneRepository;

    public ZoneService(ZoneRepository zoneRepository) {this.zoneRepository = zoneRepository; }

    public Zone create(Zone zone){ return zoneRepository.save(zone); }

    public List<Zone> findAll() { return zoneRepository.findAll(); }

    public Zone findById(long id){
        return zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));
    }

    public Zone update(long id, Zone updated){
        Zone z = findById(id);
        z.setName(updated.getName());
        z.setDescription(updated.getDescription());
        z.setCapacity(updated.getCapacity());
        return zoneRepository.save(z);
    }

    public void delete(long id){
        Zone zone = findById(id);
        if(!zone.getCreatures().isEmpty()){
            throw new ZoneNotEmptyException(
                String.format("Cannot delete zone '%s' (ID: %d) because it contains %d creatures. Remove all creatures first.", 
                    zone.getName(), 
                    zone.getId(), 
                    zone.getCreatures().size())
            );
        }
        zoneRepository.delete(zone);
    }


}
