package com.example.zoo_fantastico.service;

import com.example.zoo_fantastico.exception.ResourceNotFoundException;
import com.example.zoo_fantastico.model.Creature;
import com.example.zoo_fantastico.repository.CreatureRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreatureService {
    private final CreatureRepository creatureRepository;

    public CreatureService(CreatureRepository creatureRepository) {
        this.creatureRepository = creatureRepository;
    }

    public Creature create(Creature creature) { return creatureRepository.save(creature); }
    public List<Creature> findAll() { return creatureRepository.findAll(); }

    public Creature findById(Long id) {
        return creatureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Creature not found"));
    }

    public Creature update(Long id, Creature updated) {
        Creature c = findById(id);
        c.setName(updated.getName());
        c.setSpecies(updated.getSpecies());
        c.setSize(updated.getSize());
        c.setDangerLevel(updated.getDangerLevel());
        c.setHealthStatus(updated.getHealthStatus());
        return creatureRepository.save(c);
    }

    public void delete(Long id) {
        Creature c = findById(id);
        if ("critical".equalsIgnoreCase(c.getHealthStatus())) {
            throw new IllegalStateException("Cannot delete a creature in critical health");
        }
        creatureRepository.delete(c);
    }
}
