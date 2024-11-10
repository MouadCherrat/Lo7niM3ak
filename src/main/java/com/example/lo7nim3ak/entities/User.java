package com.example.lo7nim3ak.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@ToString
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

    @JsonIgnore
    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    @JsonIgnore
    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<Car> cars;

    @JsonIgnore
    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<Drive> drives;
    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations;

    @JsonIgnore
    @OneToMany(mappedBy = "sender")
    private List<Message> sentMessages;

    @JsonIgnore
    @OneToMany(mappedBy = "receiver")
    private List<Message> receivedMessages;

    public User(Long id) {
        this.id = id;
    }


}
