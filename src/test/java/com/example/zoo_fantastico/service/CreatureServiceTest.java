package com.example.zoo_fantastico.service;

import com.example.zoo_fantastico.exception.ResourceNotFoundException;
import com.example.zoo_fantastico.model.Creature;
import com.example.zoo_fantastico.repository.CreatureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class CreatureServiceTest {

    @Mock
    private CreatureRepository creatureRepository;

    @InjectMocks
    private CreatureService creatureService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // -------------------- findById (from teacher) --------------------

    @Test
    void testGetCreatureById_ShouldReturnCreature_WhenCreatureExists() {
        // Arrange
        Long creatureId = 1L;
        Creature expectedCreature = new Creature();
        expectedCreature.setId(creatureId);
        when(creatureRepository.findById(creatureId)).thenReturn(Optional.of(expectedCreature));

        // Act
        Creature actualCreature = creatureService.findById(creatureId);

        // Assert
        assertNotNull(actualCreature);
        assertEquals(expectedCreature, actualCreature);
        verify(creatureRepository, times(1)).findById(creatureId);
    }

    @Test
    void testGetCreatureById_ShouldThrowException_WhenCreatureDoesNotExist() {
        // Arrange
        Long creatureId = 2L;
        when(creatureRepository.findById(creatureId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> creatureService.findById(creatureId));
        verify(creatureRepository, times(1)).findById(creatureId);
    }

    // -------------------- create --------------------

    @Test
    void testCreate_ShouldSaveAndReturnCreature() {
        // Arrange
        Creature toSave = new Creature();
        toSave.setName("Phoenix");
        toSave.setSpecies("Firebird");
        toSave.setSize(2.5);
        toSave.setDangerLevel(7);
        toSave.setHealthStatus("stable");

        Creature saved = new Creature();
        saved.setId(100L);
        saved.setName(toSave.getName());
        saved.setSpecies(toSave.getSpecies());
        saved.setSize(toSave.getSize());
        saved.setDangerLevel(toSave.getDangerLevel());
        saved.setHealthStatus(toSave.getHealthStatus());

        when(creatureRepository.save(toSave)).thenReturn(saved);

        // Act
        Creature result = creatureService.create(toSave);

        // Assert
        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("Phoenix", result.getName());
        verify(creatureRepository, times(1)).save(toSave);
    }

    // -------------------- findAll --------------------

    @Test
    void testFindAll_ShouldReturnAllCreatures() {
        // Arrange
        Creature c1 = new Creature(); c1.setId(1L);
        Creature c2 = new Creature(); c2.setId(2L);
        when(creatureRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        // Act
        List<Creature> all = creatureService.findAll();

        // Assert
        assertEquals(2, all.size());
        assertTrue(all.contains(c1));
        assertTrue(all.contains(c2));
        verify(creatureRepository, times(1)).findAll();
    }

    // -------------------- update --------------------

    @Test
    void testUpdate_ShouldCopyFieldsAndSave_WhenCreatureExists() {
        // Arrange (existing entity in DB)
        Long id = 10L;
        Creature existing = new Creature();
        existing.setId(id);
        existing.setName("Old");
        existing.setSpecies("OldSpec");
        existing.setSize(1.0);
        existing.setDangerLevel(3);
        existing.setHealthStatus("stable");

        // Incoming payload with new values
        Creature updated = new Creature();
        updated.setName("New");
        updated.setSpecies("NewSpec");
        updated.setSize(4.2);
        updated.setDangerLevel(9);
        updated.setHealthStatus("injured");

        when(creatureRepository.findById(id)).thenReturn(Optional.of(existing));
        when(creatureRepository.save(any(Creature.class)))
                .thenAnswer(inv -> inv.getArgument(0)); // return the saved entity

        // Act
        Creature result = creatureService.update(id, updated);

        // Assert: id stays same, fields are updated
        assertEquals(id, result.getId());
        assertEquals("New", result.getName());
        assertEquals("NewSpec", result.getSpecies());
        assertEquals(4.2, result.getSize(), 0.0001);
        assertEquals(9, result.getDangerLevel());
        assertEquals("injured", result.getHealthStatus());

        // Verify we saved the mutated existing entity with updated values
        ArgumentCaptor<Creature> captor = ArgumentCaptor.forClass(Creature.class);
        verify(creatureRepository).save(captor.capture());
        Creature savedArg = captor.getValue();
        assertEquals(id, savedArg.getId());
        assertEquals("New", savedArg.getName());
        assertEquals("NewSpec", savedArg.getSpecies());
        assertEquals(4.2, savedArg.getSize(), 0.0001);
        assertEquals(9, savedArg.getDangerLevel());
        assertEquals("injured", savedArg.getHealthStatus());
    }

    @Test
    void testUpdate_ShouldThrow_WhenCreatureDoesNotExist() {
        // Arrange
        Long id = 999L;
        when(creatureRepository.findById(id)).thenReturn(Optional.empty());

        Creature incoming = new Creature();
        incoming.setName("DoesNotMatter");

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> creatureService.update(id, incoming));
        verify(creatureRepository, never()).save(any());
    }

    // -------------------- delete --------------------

    @Test
    void testDelete_ShouldDelete_WhenHealthNotCritical() {
        // Arrange
        Long id = 5L;
        Creature existing = new Creature();
        existing.setId(id);
        existing.setHealthStatus("stable");

        when(creatureRepository.findById(id)).thenReturn(Optional.of(existing));

        // Act
        creatureService.delete(id);

        // Assert
        verify(creatureRepository, times(1)).delete(existing);
    }

    @Test
    void testDelete_ShouldThrow_WhenHealthCritical() {
        // Arrange
        Long id = 6L;
        Creature existing = new Creature();
        existing.setId(id);
        existing.setHealthStatus("critical");

        when(creatureRepository.findById(id)).thenReturn(Optional.of(existing));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> creatureService.delete(id));
        verify(creatureRepository, never()).delete(any());
    }

    @Test
    void testDelete_ShouldThrow_WhenHealthCritical_IgnoresCase() {
        // Arrange
        Long id = 7L;
        Creature existing = new Creature();
        existing.setId(id);
        existing.setHealthStatus("CRITICAL"); // ensure equalsIgnoreCase works

        when(creatureRepository.findById(id)).thenReturn(Optional.of(existing));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> creatureService.delete(id));
        verify(creatureRepository, never()).delete(any());
    }
}