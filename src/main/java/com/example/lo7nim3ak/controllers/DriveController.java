package com.example.lo7nim3ak.controllers;

import com.example.lo7nim3ak.dto.request.RequestDrive;
import com.example.lo7nim3ak.dto.response.ResponseDrive;
import com.example.lo7nim3ak.services.DriveService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/drives")
@CrossOrigin(value = "*")
public class DriveController {
    private final DriveService driveService;
    @PostMapping("")
    public ResponseDrive addDrive(@RequestBody RequestDrive request) {
        return driveService.createDrive(request);
    }
    @GetMapping("")
    public List<ResponseDrive> listDrives() {
        return driveService.getAllDrives();
    }



}
