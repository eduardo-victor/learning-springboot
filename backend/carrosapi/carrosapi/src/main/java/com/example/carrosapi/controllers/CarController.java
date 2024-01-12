package com.example.carrosapi.controllers;

import com.example.carrosapi.dtos.CarRecordDto;
import com.example.carrosapi.models.CarModel;
import com.example.carrosapi.repositories.CarRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
public class CarController {

    @Autowired
    CarRepository carRepository;

    @PostMapping("/addcars")
    public ResponseEntity<CarModel> saveCar(@RequestBody @Valid CarRecordDto carRecordDto) {
        var carModel = new CarModel();
        BeanUtils.copyProperties(carRecordDto, carModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(carRepository.save(carModel));
    }

    @GetMapping("/cars")
    public ResponseEntity<List<CarModel>> getAllCars(){
        return ResponseEntity.status(HttpStatus.OK).body(carRepository.findAll());
    }

    @GetMapping("/cars/{id}")
    public ResponseEntity<Object> getOneCar(@PathVariable(value="id")UUID id){
        Optional<CarModel> carO = carRepository.findById(id);
        if(carO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found in our database.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(carO.get());
    }

    @PutMapping("/cars/{id}")
    public ResponseEntity<Object> updateCar(@PathVariable(value = "id") UUID id,
                                            @RequestBody @Valid CarRecordDto carRecordDto){
        Optional<CarModel> carO = carRepository.findById(id);
        if(carO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found in our database");
        }
        var carModel = carO.get();
        BeanUtils.copyProperties(carRecordDto, carModel);
        return ResponseEntity.status(HttpStatus.OK).body(carRepository.save(carModel));
    }


}
