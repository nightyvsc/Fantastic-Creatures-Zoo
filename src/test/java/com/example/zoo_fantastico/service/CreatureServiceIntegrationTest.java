package com.example.zoo_fantastico.service;

import com.example.zoo_fantastico.model.Creature;
import com.example.zoo_fantastico.repository.CreatureRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class CreatureServiceIntegrationTest {

    @Autowired
    private CreatureService creatureService;

    @Autowired
    private CreatureRepository creatureRepository;

    @Test
    void testCreateCreature_ShouldPersistInDatabase() {
        Creature creature = new Creature();
        creature.setName("Unicornio");
        creature.setSpecies("Equino mágico");
        creature.setSize(3.4);
        creature.setDangerLevel(3);
        creature.setHealthStatus("healthy");

        Creature saved = creatureService.create(creature);

        Optional<Creature> found = creatureRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Unicornio", found.get().getName());
    }

    @Test
    void testUpdateCreature_ShouldModifyDatabaseRecord() {
        Creature creature = new Creature();
        creature.setName("Fénix");
        creature.setSpecies("Ave mítica");
        creature.setSize(6.7);
        creature.setDangerLevel(4);
        creature.setHealthStatus("stable");

        Creature saved = creatureService.create(creature);

        Creature updated = new Creature();
        updated.setName("Fénix Dorado");
        updated.setSpecies("Ave legendaria");
        updated.setSize(7.7);
        updated.setDangerLevel(5);
        updated.setHealthStatus("healthy");

        Creature modified = creatureService.update(saved.getId(), updated);

        assertEquals("Fénix Dorado", modified.getName());
        assertEquals(5, modified.getDangerLevel());
    }

    @Test
    void testDeleteCreature_ShouldRemoveFromDatabase() {
        Creature creature = new Creature();
        creature.setName("Dragón");
        creature.setSpecies("Reptil mágico");
        creature.setSize(6.7);
        creature.setDangerLevel(10);
        creature.setHealthStatus("healthy");

        Creature saved = creatureService.create(creature);
        Long id = saved.getId();

        creatureService.delete(id);

        assertFalse(creatureRepository.findById(id).isPresent());
    }

    @Test
    void testDeleteCreature_CriticalHealth_ShouldThrowException() {
        Creature creature = new Creature();
        creature.setName("Sirena");
        creature.setSpecies("Mitológica");
        creature.setSize(9);
        creature.setDangerLevel(2);
        creature.setHealthStatus("critical");

        Creature saved = creatureService.create(creature);

        assertThrows(IllegalStateException.class, () -> creatureService.delete(saved.getId()));
    }
}
