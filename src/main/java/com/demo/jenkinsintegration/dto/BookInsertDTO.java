package com.demo.jenkinsintegration.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record BookInsertDTO(@NotBlank(message = "Title must be filled")
                            String title,
                            @NotBlank(message = "Description must be filled")
                            String description,
                            @Positive(message = "You must inform a positive number")
                            int page,
                            @Positive(message = "You must inform a positive number")
                            double price) {
}
