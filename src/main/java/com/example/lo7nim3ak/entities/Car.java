package com.example.lo7nim3ak.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String manufacturer;
    private String model;
    private Integer number_of_seats;
    private String color;
    private String licence_plate;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "id_user")
    private User user;

}
