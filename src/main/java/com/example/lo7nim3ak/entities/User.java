package com.example.lo7nim3ak.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String password;
    private String name;
    private String firstName;
    private String email;
    private String phone;
    private String role;
    @JsonIgnore
    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<Document> documents;
    @OneToMany(mappedBy = "user")
    private List<Review> reviews;
    @OneToMany(mappedBy = "user")
    private List<Car> cars;
    @OneToMany(mappedBy = "user")
    private List<Drive> drives;
    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations;




}
