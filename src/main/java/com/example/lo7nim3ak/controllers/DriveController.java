package com.example.lo7nim3ak.controllers;

import com.example.lo7nim3ak.dto.ReservationDto;
import com.example.lo7nim3ak.dto.request.RequestDrive;
import com.example.lo7nim3ak.dto.response.ResponseDrive;
import com.example.lo7nim3ak.entities.Drive;
import com.example.lo7nim3ak.services.DriveService;
import com.example.lo7nim3ak.services.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/drives")
public class DriveController {
    private final DriveService driveService;
    private final ReservationService reservationService;
    @PostMapping
    public ResponseDrive addDrive(@RequestBody RequestDrive request) {
        return driveService.createDrive(request);
    }
    @GetMapping
    public List<ResponseDrive> listDrives() {
        return driveService.getAllDrives();
    }
    @PutMapping("/editDrive")
    public void editDrive(@RequestBody Drive drive) {
        driveService.updateDrive(drive);
    }
    @DeleteMapping("/deleteDrive/{id}")
    public void deleteDrive(@PathVariable Long id) {
        driveService.deleteDrive(id);
    }
    @GetMapping("/findDriveById/{id}")
    public Optional<Drive> findDriveById(@PathVariable Long id) {
        return driveService.findById(id);
    }
    @GetMapping("/findListDriveByUserId/{id}")
    public List<Drive> findAllDriveByUserId(@PathVariable Long id) {
        return driveService.findAllByUserId(id);}
    @GetMapping("/drive/{driveId}/reservations")
    public ResponseEntity<List<ReservationDto>> getReservationsByDriveId(@PathVariable Long driveId) {
        List<ReservationDto> reservations = reservationService.getReservationsByDriveId(driveId);
        return ResponseEntity.ok(reservations);
    }

}
