package com.example.lo7nim3ak.services;

import com.example.lo7nim3ak.entities.Car;
import com.example.lo7nim3ak.repository.CarRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    public Car createCar (Car car){
        return carRepository.save(car);
    }
    public List<Car> getCars() {
        return carRepository.findAll();
    }
    public void updateCar(Car car)  {
        Optional<Car> optionalVehicle = carRepository.findById(car.getId());
        if (optionalVehicle.isEmpty()) {
            throw new RuntimeException("Véhicule introuvable");
        } else {
            optionalVehicle.get().update(car);
            carRepository.save(optionalVehicle.get());
        }
    }
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }
    public Optional<Car> findById(Long id) {
        return carRepository.findById(id);
    }
    public Optional<Car> findByUserId(Long userId) {
        return carRepository.findByUserId(userId);
    }





}
