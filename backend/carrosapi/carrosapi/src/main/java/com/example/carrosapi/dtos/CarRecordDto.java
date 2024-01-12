package com.example.carrosapi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CarRecordDto(@NotBlank String brand, @NotBlank String model, @NotNull Integer year,@NotNull Integer horsepower, @NotBlank String image) {

}
