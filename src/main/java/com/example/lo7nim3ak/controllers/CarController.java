package com.example.lo7nim3ak.controllers;

import com.example.lo7nim3ak.entities.Car;
import com.example.lo7nim3ak.services.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class CarController {

    private final CarService carService;

    @PostMapping("/cars")
    public Car add(@RequestBody Car car){
        return carService.createCar(car);
    }
    @GetMapping("/cars")
    public List<Car> listCars(){
        return carService.getCars();
    }

    @PutMapping("/editCar")
    public void editCar(@RequestBody Car Car){
        carService.updateCar(Car);
    }

    @DeleteMapping("/deleteCar/{id}")
    public void deleteCar(@PathVariable Long id){
        carService.deleteCar(id);
    }

    @GetMapping("/findCarById/{id}")
    public Optional<Car> findCarById(@PathVariable Long id){
        return carService.findById(id);
    }

    @GetMapping("/findCarByUserId/{id}")
    public Optional<Car> findCarByUserId(@PathVariable Long id){
        return carService.findByUserId(id);
    }

}


