package com.example.zoo_fantastico.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;

import com.example.zoo_fantastico.exception.ResourceNotFoundException;
import com.example.zoo_fantastico.model.Creature;
import com.example.zoo_fantastico.repository.CreatureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CreatureServiceTest {
    @Mock
    private CreatureRepository creatureRepository;
    @InjectMocks
    private CreatureService creatureService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
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
        assertThrows(ResourceNotFoundException.class, () -> {
            creatureService.findById(creatureId);
        });
        verify(creatureRepository, times(1)).findById(creatureId);
    }
}
