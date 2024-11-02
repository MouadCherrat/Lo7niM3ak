package com.example.lo7nim3ak.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "reserved_seats", length = 100)
    private int seats;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_passenger")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "id_drive")
    private Drive drive;
    @Column(name = "status", length = 100)
    @Enumerated(value = EnumType.STRING)
    private Status status;
    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Bill bill;
}




