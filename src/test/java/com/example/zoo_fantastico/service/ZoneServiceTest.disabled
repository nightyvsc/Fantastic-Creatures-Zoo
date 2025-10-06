package com.example.zoo_fantastico.service;

import com.example.zoo_fantastico.exception.ResourceNotFoundException;
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
        toSave.setZoneType("Enchanted");
        toSave.setAreaMeters(5000.0);

        Zone saved = new Zone();
        saved.setId(1L);
        saved.setName(toSave.getName());
        saved.setZoneType(toSave.getZoneType());
        saved.setAreaMeters(toSave.getAreaMeters());

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
        existing.setZoneType("Old Type");
        existing.setAreaMeters(100.0);

        Zone updated = new Zone();
        updated.setName("New Zone");
        updated.setZoneType("New Type");
        updated.setAreaMeters(200.0);

        when(zoneRepository.findById(id)).thenReturn(Optional.of(existing));
        when(zoneRepository.save(any(Zone.class))).thenAnswer(inv -> inv.getArgument(0));

        Zone result = zoneService.update(id, updated);

        assertEquals("New Zone", result.getName());
        assertEquals("New Type", result.getZoneType());
        assertEquals(200.0, result.getAreaMeters());

        ArgumentCaptor<Zone> captor = ArgumentCaptor.forClass(Zone.class);
        verify(zoneRepository).save(captor.capture());
        Zone savedArg = captor.getValue();
        assertEquals("New Zone", savedArg.getName());
        assertEquals("New Type", savedArg.getZoneType());
        assertEquals(200.0, savedArg.getAreaMeters());
    }

    @Test
    void testUpdate_ShouldThrow_WhenNotFound() {
        when(zoneRepository.findById(123L)).thenReturn(Optional.empty());
        Zone updated = new Zone(); updated.setName("Does not matter");

        assertThrows(ResourceNotFoundException.class, () -> zoneService.update(123L, updated));
        verify(zoneRepository, never()).save(any());
    }

    // -------------------- delete --------------------
    @Test
    void testDelete_ShouldDelete_WhenNoCreatures() {
        Zone zone = new Zone();
        zone.setId(30L);
        zone.setCreatures(new ArrayList<>()); // empty creatures list

        when(zoneRepository.findById(30L)).thenReturn(Optional.of(zone));

        zoneService.delete(30L);

        verify(zoneRepository, times(1)).delete(zone);
    }

    @Test
    void testDelete_ShouldThrow_WhenHasCreatures() {
        Zone zone = new Zone();
        zone.setId(40L);
        // simulate non-empty creatures list
        zone.setCreatures(Arrays.asList(new com.example.zoo_fantastico.model.Creature()));

        when(zoneRepository.findById(40L)).thenReturn(Optional.of(zone));

        assertThrows(IllegalStateException.class, () -> zoneService.delete(40L));
        verify(zoneRepository, never()).delete(any());
    }

    @Test
    void testDelete_ShouldThrow_WhenNotFound() {
        when(zoneRepository.findById(77L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> zoneService.delete(77L));
        verify(zoneRepository, never()).delete(any());
    }
}