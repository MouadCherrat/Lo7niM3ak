package com.example.lo7nim3ak.repository;

import com.example.lo7nim3ak.dto.request.RequestDrive;
import com.example.lo7nim3ak.dto.response.ResponseDrive;
import com.example.lo7nim3ak.entities.Drive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriveRepository extends JpaRepository<Drive, Long> {
    Optional<Drive> findByUserId (Long userId);
    Optional<Drive> findAllByUserId (Long userId);


}
