package com.example.zoo_fantastico.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.NoArgsConstructor;
import lombok.Data;

@Entity
@Data
@NoArgsConstructor
public class Creature {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Column(nullable = false)
    private String name;

    @NotBlank @Column(nullable = false)
    private String species;

    @PositiveOrZero
    private double size;

    @Min(1) @Max(10)
    private int dangerLevel;

    @NotBlank
    private String healthStatus;

    @NotNull
    @JoinColumn(name = "zone_id")
    private Zone zone;
}
