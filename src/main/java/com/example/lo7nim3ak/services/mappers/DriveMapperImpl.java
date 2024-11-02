package com.example.lo7nim3ak.services.mappers;

import com.example.lo7nim3ak.dto.request.RequestDrive;
import com.example.lo7nim3ak.dto.response.ResponseDrive;
import com.example.lo7nim3ak.entities.Drive;
import com.example.lo7nim3ak.entities.User;
import org.springframework.stereotype.Component;

@Component
public class DriveMapperImpl implements  IDriveMapper{
    @Override
    public ResponseDrive toDto(Drive drive) {
        if ( drive == null ) {
            return null;
        }
        ResponseDrive responseDrive = new ResponseDrive();
       // responseDrive.setDriverId(drive.getUser().getId());
        responseDrive.setDriverId(driveUsertId(drive));
        responseDrive.setId(drive.getId());
        responseDrive.setPrice(drive.getPrice());
        responseDrive.setPickup(drive.getPickup());
        responseDrive.setDestination(drive.getDestination());
        responseDrive.setSeating(drive.getSeating());
        responseDrive.setDescription(drive.getDescription());
        responseDrive.setDeptime(drive.getDeptime());
        return responseDrive;
    }
    @Override
    public Drive toEntity(RequestDrive request) {
        if(request == null){
            return null;
        }
        Drive drive = Drive.builder()
                .seating(request.getSeating())
                .pickup(request.getPickup())
                .description(request.getDescription())
                .deptime(request.getDeptime())
                .price(request.getPrice())
                .destination(request.getDestination())
                .build();
        return drive;
    }
    private Long driveUsertId(Drive drive) {
        if ( drive == null ) {
            return null;
        }
        User account = drive.getUser();
        if ( account == null ) {
            return null;
        }
        Long id = account.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

}
