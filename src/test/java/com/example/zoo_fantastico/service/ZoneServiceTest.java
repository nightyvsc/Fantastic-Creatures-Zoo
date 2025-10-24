package com.example.zoo_fantastico.service;

import com.example.zoo_fantastico.exception.ResourceNotFoundException;
import com.example.zoo_fantastico.exception.ZoneNotEmptyException;
import com.example.zoo_fantastico.model.Creature;
import com.example.zoo_fantastico.model.Zone;
import com.example.zoo_fantastico.repository.ZoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ZoneServiceTest {

    @Mock
    private ZoneRepository zoneRepository;

    @InjectMocks
    private ZoneService zoneService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // -------------------- create --------------------
    @Test
    void testCreate_ShouldSaveAndReturnZone() {
        Zone toSave = new Zone();
        toSave.setName("Magical Forest");
        toSave.setDescription("A lush area full of enchanted creatures");
        toSave.setCapacity(300);

        Zone saved = new Zone();
        saved.setId(1L);
        saved.setName(toSave.getName());
        saved.setDescription(toSave.getDescription());
        saved.setCapacity(toSave.getCapacity());

        when(zoneRepository.save(toSave)).thenReturn(saved);

        Zone result = zoneService.create(toSave);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Magical Forest", result.getName());
        verify(zoneRepository, times(1)).save(toSave);
    }

    // -------------------- findAll --------------------
    @Test
    void testFindAll_ShouldReturnAllZones() {
        Zone z1 = new Zone(); z1.setId(1L);
        Zone z2 = new Zone(); z2.setId(2L);
        when(zoneRepository.findAll()).thenReturn(Arrays.asList(z1, z2));

        List<Zone> zones = zoneService.findAll();

        assertEquals(2, zones.size());
        assertTrue(zones.contains(z1));
        assertTrue(zones.contains(z2));
        verify(zoneRepository, times(1)).findAll();
    }

    // -------------------- findById --------------------
    @Test
    void testFindById_ShouldReturnZone_WhenExists() {
        Zone zone = new Zone(); zone.setId(10L);
        when(zoneRepository.findById(10L)).thenReturn(Optional.of(zone));

        Zone found = zoneService.findById(10L);

        assertEquals(10L, found.getId());
        verify(zoneRepository, times(1)).findById(10L);
    }

    @Test
    void testFindById_ShouldThrow_WhenNotFound() {
        when(zoneRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> zoneService.findById(99L));
        verify(zoneRepository, times(1)).findById(99L);
    }

    // -------------------- update --------------------
    @Test
    void testUpdate_ShouldModifyFieldsAndSave_WhenExists() {
        Long id = 20L;
        Zone existing = new Zone();
        existing.setId(id);
        existing.setName("Old Zone");
        existing.setDescription("Old description");
        existing.setCapacity(100);

        Zone updated = new Zone();
        updated.setName("New Zone");
        updated.setDescription("Freshly renovated zone");
        updated.setCapacity(200);

        when(zoneRepository.findById(id)).thenReturn(Optional.of(existing));
        when(zoneRepository.save(any(Zone.class))).thenAnswer(inv -> inv.getArgument(0));

        Zone result = zoneService.update(id, updated);

        assertEquals("New Zone", result.getName());
        assertEquals("Freshly renovated zone", result.getDescription());
        assertEquals(200, result.getCapacity());

        ArgumentCaptor<Zone> captor = ArgumentCaptor.forClass(Zone.class);
        verify(zoneRepository).save(captor.capture());
        Zone savedArg = captor.getValue();
        assertEquals("New Zone", savedArg.getName());
        assertEquals("Freshly renovated zone", savedArg.getDescription());
        assertEquals(200, savedArg.getCapacity());
    }

    @Test
    void testUpdate_ShouldThrow_WhenNotFound() {
        when(zoneRepository.findById(123L)).thenReturn(Optional.empty());
        Zone updated = new Zone(); updated.setName("Does not matter");

        assertThrows(ResourceNotFoundException.class, () -> zoneService.update(123L, updated));
        verify(zoneRepository, never()).save(any());
    }

    // -------------------- delete 222 --------------------
    @Test
    void testDelete_ShouldDelete_WhenNoCreatures() {
        Zone zone = new Zone();
        zone.setId(30L);
        zone.setName("Empty Plains");
        zone.setCreatures(new ArrayList<>());

        when(zoneRepository.findById(30L)).thenReturn(Optional.of(zone));

        zoneService.delete(30L);

        verify(zoneRepository, times(1)).delete(zone);
    }

    @Test
    void testDelete_ShouldThrow_WhenHasCreatures() {
        Zone zone = new Zone();
        zone.setId(40L);
        zone.setName("Crowded Den");
        zone.setCreatures(Arrays.asList(new Creature()));

        when(zoneRepository.findById(40L)).thenReturn(Optional.of(zone));

        assertThrows(ZoneNotEmptyException.class, () -> zoneService.delete(40L));
        verify(zoneRepository, never()).delete(any());
    }

    @Test
    void testDelete_ShouldThrow_WhenNotFound() {
        when(zoneRepository.findById(77L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> zoneService.delete(77L));
        verify(zoneRepository, never()).delete(any());
    }
}