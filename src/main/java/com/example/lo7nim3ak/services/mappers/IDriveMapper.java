package com.example.lo7nim3ak.services.mappers;

import com.example.lo7nim3ak.dto.request.RequestDrive;
import com.example.lo7nim3ak.dto.response.ResponseDrive;
import com.example.lo7nim3ak.entities.Drive;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IDriveMapper {
    IDriveMapper MAPPER = Mappers.getMapper(IDriveMapper.class);
    ResponseDrive toDto(Drive drive);
    Drive toEntity(RequestDrive request);
}

