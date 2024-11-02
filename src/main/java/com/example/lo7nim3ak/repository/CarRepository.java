package com.example.lo7nim3ak.repository;

import com.example.lo7nim3ak.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CarRepository extends JpaRepository<Car, Long> {
}
