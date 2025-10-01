package com.example.zoo_fantastico.controller;

import com.example.zoo_fantastico.model.Zone;
import com.example.zoo_fantastico.service.ZoneService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zones")
public class ZoneController {

    private final ZoneService zoneService;

    public ZoneController(ZoneService zoneService) {this.zoneService = zoneService; }

    @PostMapping
    public ResponseEntity<Zone> create(@Valid @RequestBody Zone zone) {
        Zone saved = zoneService.create(zone);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<Zone> all() { return zoneService.findAll(); }

    @GetMapping("/{id}")
    public Zone byId(@PathVariable Long id) { return zoneService.findById(id); }

    @PutMapping("/{id}")
    public Zone update(@PathVariable Long id, @Valid @RequestBody Zone updated) {
        return zoneService.update(id, updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        zoneService.delete(id);
        return ResponseEntity.noContent().build();
    }
}