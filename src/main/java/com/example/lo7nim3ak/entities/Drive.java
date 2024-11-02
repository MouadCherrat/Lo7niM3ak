package com.example.lo7nim3ak.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Drive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pickup;
    private String destination;
    private Date deptime;
    private BigDecimal price;
    @Column(name = "disponible_seats", length = 100)
    private int seating;
    private String description;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_driver")
    private User user;
    @OneToMany(mappedBy = "drive")
    private List<Reservation> reservations;


}
