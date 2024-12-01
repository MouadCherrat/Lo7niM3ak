package com.example.lo7nim3ak.repository;

import com.example.lo7nim3ak.entities.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByReservationId(Long reservationId);

}
