package com.demo.jenkinsintegration.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record BookInsertDTO(@NotBlank(message = "Title must be filled")
                            String title,
                            @NotBlank(message = "Description must be filled")
                            @Max(value = 1000, message = "You cannot inform more than 500 characters to the book description")
                            String description,
                            @Positive(message = "You must inform a positive number")
                            int page,
                            @Positive(message = "You must inform a positive number")
                            double price) {
}
