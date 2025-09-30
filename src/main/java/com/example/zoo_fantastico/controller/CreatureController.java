package com.example.zoo_fantastico.controller;

import com.example.zoo_fantastico.model.Creature;
import com.example.zoo_fantastico.service.CreatureService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/creatures")
public class CreatureController {

    private final CreatureService creatureService;

    public CreatureController(CreatureService creatureService) {
        this.creatureService = creatureService;
    }

    @PostMapping
    public ResponseEntity<Creature> create(@Valid @RequestBody Creature creature) {
        Creature saved = creatureService.create(creature);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<Creature> all() { return creatureService.findAll(); }

    @GetMapping("/{id}")
    public Creature byId(@PathVariable Long id) { return creatureService.findById(id); }

    @PutMapping("/{id}")
    public Creature update(@PathVariable Long id, @Valid @RequestBody Creature updated) {
        return creatureService.update(id, updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        creatureService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
