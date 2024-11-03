package com.example.lo7nim3ak.controllers;

import com.example.lo7nim3ak.dto.request.RequestDrive;
import com.example.lo7nim3ak.dto.response.ResponseDrive;
import com.example.lo7nim3ak.entities.Drive;
import com.example.lo7nim3ak.services.DriveService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
@CrossOrigin(value = "*")
public class DriveController {
    private final DriveService driveService;
    @PostMapping("/documents")
    public ResponseDrive addDrive(@RequestBody RequestDrive request) {
        return driveService.createDrive(request);
    }
    @GetMapping("/documents")
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
    @GetMapping("/findDriveByAccountId/{id}")
    public Optional<Drive> findDriveByUserId(@PathVariable Long id) {
        return driveService.findByUserId(id);
    }
    @GetMapping("/findListDriveByUserId/{id}")
    public Optional<Drive> findAllDriveByUserId(@PathVariable Long id) {
        return driveService.findAllByUserId(id);
    }







}
