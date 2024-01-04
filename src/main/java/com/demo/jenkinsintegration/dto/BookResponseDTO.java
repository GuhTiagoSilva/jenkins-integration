package com.demo.jenkinsintegration.dto;

import com.demo.jenkinsintegration.model.Book;

import java.util.UUID;

public record BookResponseDTO(UUID id,
                              String title,
                              String description,
                              int page,
                              double price) {
    public static BookResponseDTO from(Book book) {
        return new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getDescription(),
                book.getPage(),
                book.getPrice()
        );
    }
}
