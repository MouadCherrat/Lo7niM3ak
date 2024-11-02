package com.example.lo7nim3ak.services;

import com.example.lo7nim3ak.dto.request.RequestDrive;
import com.example.lo7nim3ak.dto.response.ResponseDrive;
import com.example.lo7nim3ak.entities.Drive;
import com.example.lo7nim3ak.entities.User;
import com.example.lo7nim3ak.repository.DriveRepository;
import com.example.lo7nim3ak.repository.UserRepository;
import com.example.lo7nim3ak.services.mappers.IDriveMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class DriveService {
    private final DriveRepository driveRepository;
    private final UserRepository userRepository;
    private final IDriveMapper iDriveMapper;


    public ResponseDrive createDrive(RequestDrive requestDrive) {
        Optional<User> driver = userRepository.findById(requestDrive.getDriverId());
        if (driver.isPresent()) {
            Drive drive = Drive.builder()
                    .pickup(requestDrive.getPickup())
                    .destination(requestDrive.getDestination())
                    .deptime(requestDrive.getDeptime())
                    .description(requestDrive.getDescription())
                    .user(driver.get())
                    .price(requestDrive.getPrice())
                    .seating(requestDrive.getSeating())
                    .build();
            ResponseDrive response = Stream.of(driveRepository.save(drive)).map(iDriveMapper::toDto).findFirst().get();
            return response;
        } else {
            throw new IllegalArgumentException("Driver unfounded!");
        }
    }
    public List<ResponseDrive> getAllDrives() {
        List<Drive> drives = driveRepository.findAll();
        List<ResponseDrive> dtoResponses = new ArrayList<>();
        for (Drive drive : drives) {
            ResponseDrive dtoResponse = Stream.of(driveRepository.save(drive)).map(iDriveMapper::toDto).findFirst().get();
            System.out.println(dtoResponse);
            dtoResponses.add(dtoResponse);
        }
        return dtoResponses;
    }

}






