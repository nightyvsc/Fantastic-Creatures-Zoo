package com.example.zoo_fantastico.service;

import com.example.zoo_fantastico.model.Creature;
import com.example.zoo_fantastico.model.Zone;
import com.example.zoo_fantastico.repository.CreatureRepository;
import com.example.zoo_fantastico.repository.ZoneRepository;
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

    @Autowired
    private ZoneRepository zoneRepository;

    @Test
    void testCreateCreature_ShouldPersistInDatabase() {
        // Crear una zona primero
        Zone zone = new Zone();
        zone.setName("Zona Mágica");
        zone.setDescription("Área para criaturas místicas");
        zone.setCapacity(10);
        zone = zoneRepository.save(zone); // persistimos la zona primero

        Creature creature = new Creature();
        creature.setName("Unicornio");
        creature.setSpecies("Equino mágico");
        creature.setSize(3.4);
        creature.setDangerLevel(3);
        creature.setHealthStatus("healthy");
        creature.setZone(zone);

        Creature saved = creatureService.create(creature);

        Optional<Creature> found = creatureRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Unicornio", found.get().getName());
    }

    @Test
    void testUpdateCreature_ShouldModifyDatabaseRecord() {

        Zone zone = new Zone();
        zone.setName("Zona Volcánica");
        zone.setDescription("Área para aves míticas");
        zone.setCapacity(5);
        zone = zoneRepository.save(zone);


        Creature creature = new Creature();
        creature.setName("Fénix");
        creature.setSpecies("Ave mítica");
        creature.setSize(6.7);
        creature.setDangerLevel(4);
        creature.setHealthStatus("stable");
        creature.setZone(zone); // 👈 necesario

        Creature saved = creatureService.create(creature);


        Creature updated = new Creature();
        updated.setName("Fénix Dorado");
        updated.setSpecies("Ave legendaria");
        updated.setSize(7.7);
        updated.setDangerLevel(5);
        updated.setHealthStatus("healthy");
        updated.setZone(zone);

        Creature modified = creatureService.update(saved.getId(), updated);

        assertEquals("Fénix Dorado", modified.getName());
        assertEquals(5, modified.getDangerLevel());
    }


    @Test
    void testDeleteCreature_ShouldRemoveFromDatabase() {
        // Crear una zona primero (requerida por la FK)
        Zone zone = new Zone();
        zone.setName("Caverna de Fuego");
        zone.setDescription("Zona para criaturas extremadamente peligrosas");
        zone.setCapacity(3);
        zone = zoneRepository.save(zone);

        // Crear la criatura asociada a la zona
        Creature creature = new Creature();
        creature.setName("Dragón");
        creature.setSpecies("Reptil mágico");
        creature.setSize(6.7);
        creature.setDangerLevel(10);
        creature.setHealthStatus("healthy");
        creature.setZone(zone); // 👈 necesario para cumplir la restricción NOT NULL

        // Guardar criatura
        Creature saved = creatureService.create(creature);
        Long id = saved.getId();

        // Eliminar criatura
        creatureService.delete(id);

        // Verificar que ya no existe
        assertFalse(creatureRepository.findById(id).isPresent());
    }


    @Test
    void testDeleteCreature_CriticalHealth_ShouldThrowException() {
        // Crear una zona válida (requerida por la entidad Creature)
        Zone zone = new Zone();
        zone.setName("Lago Encantado");
        zone.setDescription("Zona para criaturas acuáticas");
        zone.setCapacity(5);
        zone = zoneRepository.save(zone);

        // Crear la criatura con estado crítico
        Creature creature = new Creature();
        creature.setName("Sirena");
        creature.setSpecies("Mitológica");
        creature.setSize(9);
        creature.setDangerLevel(2);
        creature.setHealthStatus("critical");
        creature.setZone(zone); // ✅ evita el error de zona nula

        // Guardar la criatura
        Creature saved = creatureService.create(creature);

        // Verificar que lanzar excepción al intentar eliminar una criatura crítica
        assertThrows(IllegalStateException.class, () -> creatureService.delete(saved.getId()));
    }

}
