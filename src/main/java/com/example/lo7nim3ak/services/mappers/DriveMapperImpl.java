package com.example.lo7nim3ak.services.mappers;

import com.example.lo7nim3ak.dto.request.RequestDrive;
import com.example.lo7nim3ak.dto.response.ResponseDrive;
import com.example.lo7nim3ak.entities.Drive;
import org.springframework.stereotype.Component;

@Component
public class DriveMapperImpl implements  IDriveMapper{
    @Override
    public ResponseDrive toDto(Drive drive) {
        if ( drive == null ) {
            return null;
        }
        ResponseDrive responseDrive = new ResponseDrive();
        responseDrive.setDriverId(drive.getUser().getId());
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
        return null;
    }
}
