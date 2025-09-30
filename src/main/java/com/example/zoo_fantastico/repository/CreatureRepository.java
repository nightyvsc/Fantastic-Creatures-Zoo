package com.example.zoo_fantastico.repository;

import com.example.zoo_fantastico.model.Creature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatureRepository extends JpaRepository<Creature, Long> { }
