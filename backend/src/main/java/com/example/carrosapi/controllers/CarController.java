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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class CarController {

    @Autowired
    CarRepository carRepository;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/addcars")
    public ResponseEntity<CarModel> saveCar(@RequestBody @Valid CarRecordDto carRecordDto) {
        var carModel = new CarModel();
        BeanUtils.copyProperties(carRecordDto, carModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(carRepository.save(carModel));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/cars")
    public ResponseEntity<List<CarModel>> getAllCars(){
        List<CarModel> carsList = carRepository.findAll();
        if(!carsList.isEmpty()){
            for (CarModel car : carsList){
                UUID id = car.getIdCar();
                car.add(linkTo(methodOn(CarController.class).getOneCar(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(carsList);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/cars/{id}")
    public ResponseEntity<Object> getOneCar(@PathVariable(value="id")UUID id){
        Optional<CarModel> carO = carRepository.findById(id);
        if(carO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found in our database.");
        }
        carO.get().add(linkTo(methodOn(CarController.class).getAllCars()).withRel("Cars List"));
        return ResponseEntity.status(HttpStatus.OK).body(carO.get());
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
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

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/cars/{id}")
    public ResponseEntity<Object> deleteCar(@PathVariable(value = "id")UUID id){
        Optional<CarModel> carO = carRepository.findById(id);
        if(carO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found in our database");
        }
        carRepository.delete(carO.get());
        return ResponseEntity.status(HttpStatus.OK).body("Car deleted successfully");
    }
}
