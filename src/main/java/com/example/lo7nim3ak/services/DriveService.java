package com.example.lo7nim3ak.services;

import com.example.lo7nim3ak.dto.request.RequestDrive;
import com.example.lo7nim3ak.dto.response.ResponseDrive;
import com.example.lo7nim3ak.entities.Drive;
import com.example.lo7nim3ak.entities.User;
import com.example.lo7nim3ak.repository.DriveRepository;
import com.example.lo7nim3ak.repository.UserRepository;
import com.example.lo7nim3ak.services.mappers.IDriveMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Transactional
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
    public void updateDrive (Drive drive){
        Optional<Drive> optionalDrive = driveRepository.findById(drive.getId());
        if (optionalDrive.isPresent()){
            Drive existingDrive = optionalDrive.get();
            existingDrive.setDeptime(drive.getDeptime());
            existingDrive.setDescription(drive.getDescription());
            existingDrive.setDestination(drive.getDestination());
            existingDrive.setSeating(drive.getSeating());
            existingDrive.setPickup(drive.getPickup());
            existingDrive.setPrice(drive.getPrice());
            driveRepository.save(existingDrive);
        }
        else {
            throw new RuntimeException("Course introuvable");
        }
    }
    public void deleteDrive (Long driveId){
        driveRepository.deleteById(driveId); ;
    }
    public Optional<Drive> findById (Long driveId){
        return driveRepository.findById(driveId);
    }
    public Optional<Drive> findByUserId (Long userId){
        return driveRepository.findByUserId(userId);
    }
    public Optional<Drive> findAllByUserId (Long userId){
        return driveRepository.findAllByUserId(userId);
    }


}






